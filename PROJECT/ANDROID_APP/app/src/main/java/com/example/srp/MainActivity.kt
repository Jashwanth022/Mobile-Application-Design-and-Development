package com.example.srp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var watchlistAdapter: WatchlistAdapter
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load theme preference before super.onCreate
        val sharedPrefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", true)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        val mainLayout = findViewById<View>(R.id.mainLayout)
        val homeContent = findViewById<View>(R.id.homeContent)
        val otherContent = findViewById<FrameLayout>(R.id.otherContent)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // 1. Initial Fade-in Animation
        mainLayout.animate().alpha(1f).setDuration(1000).start()

        // 2. Setup Features
        fetchData()
        setupMiniChart()
        setupNavigation()

        // 3. Bottom Navigation Logic
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    homeContent.visibility = View.VISIBLE
                    otherContent.visibility = View.GONE
                    true
                }
                R.id.nav_watchlist -> {
                    homeContent.visibility = View.GONE
                    otherContent.visibility = View.VISIBLE
                    loadWatchlist(otherContent)
                    true
                }
                R.id.nav_dashboard -> {
                    homeContent.visibility = View.GONE
                    otherContent.visibility = View.VISIBLE
                    loadDashboard(otherContent)
                    true
                }
                R.id.nav_settings -> {
                    homeContent.visibility = View.GONE
                    otherContent.visibility = View.VISIBLE
                    loadSettings(otherContent)
                    true
                }
                else -> false
            }
        }

        setupTrendingClicks()
    }

    private fun loadDashboard(container: FrameLayout) {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_dashboard, container, false)
        container.addView(view)

        val chart = view.findViewById<LineChart>(R.id.dashboardChart)
        setupDashboardChart(chart)
    }

    private fun setupDashboardChart(chart: LineChart) {
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 18200f))
        entries.add(Entry(1f, 18350f))
        entries.add(Entry(2f, 18100f))
        entries.add(Entry(3f, 18500f))
        entries.add(Entry(4f, 18400f))
        entries.add(Entry(5f, 18700f))
        entries.add(Entry(6f, 18650f))

        val dataSet = LineDataSet(entries, "NIFTY 50")
        dataSet.color = Color.parseColor("#00E676")
        dataSet.setCircleColor(Color.WHITE)
        dataSet.lineWidth = 3f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#00E676")
        dataSet.fillAlpha = 30
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(false)

        chart.data = LineData(dataSet)
        chart.description.isEnabled = false
        chart.legend.textColor = Color.WHITE
        
        chart.xAxis.apply {
            textColor = Color.WHITE
            setDrawGridLines(false)
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        }
        chart.axisLeft.apply {
            textColor = Color.WHITE
            setDrawGridLines(true)
            gridColor = Color.parseColor("#33FFFFFF")
        }
        chart.axisRight.isEnabled = false
        
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.animateX(1000)
    }

    private fun loadSettings(container: FrameLayout) {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_settings, container, false)
        container.addView(view)

        val switchDarkMode = view.findViewById<SwitchMaterial>(R.id.switchDarkMode)
        val sharedPrefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        
        // Set initial state of switch
        switchDarkMode?.isChecked = sharedPrefs.getBoolean("dark_mode", true)

        switchDarkMode?.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Setup Logout Button Click (UI only)
        view.findViewById<android.widget.Button>(R.id.btnLogout)?.setOnClickListener {
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadWatchlist(container: FrameLayout) {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_watchlist, container, false)
        container.addView(view)

        val rvWatchlist = view.findViewById<RecyclerView>(R.id.rvWatchlist)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyWatchlist)

        watchlistAdapter = WatchlistAdapter(emptyList()) { stock ->
            openDetail(stock.symbol, "+${stock.changePercent}%", "${stock.accuracy}%")
        }

        rvWatchlist.layoutManager = LinearLayoutManager(this)
        rvWatchlist.adapter = watchlistAdapter

        lifecycleScope.launch {
            db.watchlistDao().getAllWatchlist().collectLatest { stocks ->
                if (stocks.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    rvWatchlist.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    rvWatchlist.visibility = View.VISIBLE
                    watchlistAdapter.updateData(stocks)
                }
            }
        }
    }

    private fun setupTrendingClicks() {
        findViewById<View>(R.id.cardReliance).setOnClickListener {
            openDetail("RELIANCE.NS", "+1.2%", "72%")
        }
        findViewById<View>(R.id.cardTcs).setOnClickListener {
            openDetail("TCS.NS", "-0.5%", "68%")
        }
        findViewById<View>(R.id.cardHdfc).setOnClickListener {
            openDetail("HDFC.NS", "+2.1%", "65%")
        }
    }

    private fun openDetail(symbol: String, change: String, accuracy: String) {
        val intent = Intent(this, StockDetailActivity::class.java)
        intent.putExtra("STOCK_NAME", symbol)
        intent.putExtra("STOCK_CHANGE", change)
        intent.putExtra("STOCK_ACCURACY", accuracy)
        startActivity(intent)
    }

    private fun fetchData() {
        api.getPredictions().enqueue(object : Callback<List<StockResponse>> {
            override fun onResponse(
                call: Call<List<StockResponse>>,
                response: Response<List<StockResponse>>
            ) {
                val data = response.body()
                if (data != null) {
                    setupTopPick(data)
                }
            }

            override fun onFailure(call: Call<List<StockResponse>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupTopPick(data: List<StockResponse>) {
        val topStock = data.maxByOrNull { it.next_day }
        
        val tvName = findViewById<TextView>(R.id.tvHomeTopStockName)
        val tvReturn = findViewById<TextView>(R.id.tvHomeTopStockReturn)
        
        topStock?.let { stock ->
            tvName.text = stock.stock
            tvReturn.text = "Expected Return: ${if (stock.next_day >= 0) "+" else ""}${stock.next_day}%"
            
            findViewById<View>(R.id.topPickCard).setOnClickListener {
                openDetail(stock.stock, if (stock.next_day >= 0) "+${stock.next_day}%" else "${stock.next_day}%", "${stock.accuracy.toInt()}%")
            }
        }
    }

    private fun setupMiniChart() {
        val miniChart = findViewById<LineChart>(R.id.miniChart)
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, 100f))
        entries.add(Entry(1f, 110f))
        entries.add(Entry(2f, 105f))
        entries.add(Entry(3f, 120f))
        entries.add(Entry(4f, 115f))
        entries.add(Entry(5f, 130f))

        val dataSet = LineDataSet(entries, "Market Trend")
        dataSet.color = Color.parseColor("#4CAF50")
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#4CAF50")
        dataSet.fillAlpha = 50

        miniChart.data = LineData(dataSet)
        miniChart.description.isEnabled = false
        miniChart.legend.isEnabled = false
        miniChart.xAxis.isEnabled = false
        miniChart.axisLeft.isEnabled = false
        miniChart.axisRight.isEnabled = false
        miniChart.setTouchEnabled(false)
        miniChart.animateX(1500)
    }

    private fun setupNavigation() {
        findViewById<View>(R.id.btnDay).setOnClickListener {
            startActivity(Intent(this, DayActivity::class.java))
        }
        findViewById<View>(R.id.btnWeek).setOnClickListener {
            startActivity(Intent(this, WeekActivity::class.java))
        }
        findViewById<View>(R.id.btnMonth).setOnClickListener {
            startActivity(Intent(this, MonthActivity::class.java))
        }
    }
}