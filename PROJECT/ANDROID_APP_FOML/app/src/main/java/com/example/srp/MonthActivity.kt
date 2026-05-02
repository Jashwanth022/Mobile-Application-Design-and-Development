package com.example.srp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthActivity : AppCompatActivity() {

    private lateinit var rvStocks: RecyclerView
    private lateinit var lineChart: LineChart
    private lateinit var detailsContainer: View
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    
    private var allStocks = listOf<Stock>()
    private var filteredStocks = listOf<Stock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before super.onCreate
        val sharedPrefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", true)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)

        rvStocks = findViewById(R.id.rvStocks)
        lineChart = findViewById(R.id.lineChart)
        detailsContainer = findViewById(R.id.detailsContainer)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)

        setupChart()
        setupTabs()
        setupSearch()
        fetchData()
    }

    private fun fetchData() {
        ApiService.create().getPredictions().enqueue(object : Callback<List<StockResponse>> {
            override fun onResponse(call: Call<List<StockResponse>>, response: Response<List<StockResponse>>) {
                if (response.isSuccessful) {
                    val stockResponses = response.body() ?: emptyList()
                    allStocks = stockResponses.map {
                        Stock(it.stock, it.cap, it.next_month, it.accuracy.toInt())
                    }
                    filteredStocks = allStocks
                    setupRecyclerView()
                } else {
                    Toast.makeText(this@MonthActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StockResponse>>, t: Throwable) {
                Toast.makeText(this@MonthActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        rvStocks.layoutManager = LinearLayoutManager(this)
        rvStocks.adapter = StockAdapter(filteredStocks) { stock ->
            val intent = Intent(this, StockDetailActivity::class.java)
            intent.putExtra("STOCK_NAME", stock.symbol)
            intent.putExtra("STOCK_CHANGE", if (stock.changePercent >= 0) "+${stock.changePercent}%" else "${stock.changePercent}%")
            intent.putExtra("STOCK_ACCURACY", "${stock.accuracy}%")
            startActivity(intent)
        }
    }

    private fun setupSearch() {
        btnSearch.setOnClickListener {
            if (etSearch.visibility == View.GONE) {
                etSearch.visibility = View.VISIBLE
                etSearch.requestFocus()
            } else {
                etSearch.visibility = View.GONE
                etSearch.text.clear()
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterStocks(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterStocks(query: String) {
        filteredStocks = if (query.isEmpty()) {
            allStocks
        } else {
            allStocks.filter { 
                it.symbol.contains(query, ignoreCase = true) || 
                it.name.contains(query, ignoreCase = true) 
            }
        }
        rvStocks.adapter = StockAdapter(filteredStocks) {
            findViewById<TabLayout>(R.id.tabLayout).getTabAt(1)?.select()
        }
    }

    private fun setupChart() {
        val entries = ArrayList<Entry>()
        val dummyPrices = listOf(140.0, 145.0, 142.0, 150.5, 155.0, 162.0, 170.0)
        
        dummyPrices.forEachIndexed { index, price ->
            entries.add(Entry(index.toFloat(), price.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Monthly Growth")
        dataSet.color = Color.parseColor("#00E676")
        dataSet.setCircleColor(Color.WHITE)
        dataSet.lineWidth = 3f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#00E676")
        dataSet.fillAlpha = 50
        dataSet.setDrawValues(false)

        lineChart.data = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisRight.isEnabled = false
        lineChart.legend.textColor = Color.WHITE
        lineChart.invalidate()
    }

    private fun setupTabs() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showView(rvStocks)
                    1 -> showView(lineChart)
                    2 -> showView(detailsContainer)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showView(view: View) {
        rvStocks.visibility = View.GONE
        lineChart.visibility = View.GONE
        detailsContainer.visibility = View.GONE
        view.visibility = View.VISIBLE
    }
}
