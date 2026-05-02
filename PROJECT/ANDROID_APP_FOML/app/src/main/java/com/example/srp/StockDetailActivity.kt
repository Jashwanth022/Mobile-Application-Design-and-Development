package com.example.srp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class StockDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var isFavorite = false

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
        setContentView(R.layout.activity_stock_detail)

        db = AppDatabase.getDatabase(this)

        val stockName = intent.getStringExtra("STOCK_NAME") ?: "Unknown"
        val change = intent.getStringExtra("STOCK_CHANGE") ?: "+0.00%"
        val accuracy = intent.getStringExtra("STOCK_ACCURACY") ?: "0%"

        findViewById<TextView>(R.id.tvDetailName).text = stockName
        val tvChange = findViewById<TextView>(R.id.tvDetailChange)
        tvChange.text = change
        findViewById<TextView>(R.id.tvDetailAccuracy).text = "AI Confidence: $accuracy"

        if (change.contains("-")) {
            tvChange.setTextColor(ContextCompat.getColor(this, R.color.negative_red))
        } else {
            tvChange.setTextColor(ContextCompat.getColor(this, R.color.positive_green))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val btnWatchlist = findViewById<android.widget.Button>(R.id.btnWatchlistAction) // Need to check ID in XML
        
        checkWatchlistStatus(stockName, btnWatchlist)

        btnWatchlist.setOnClickListener {
            toggleWatchlist(stockName, change, accuracy, btnWatchlist)
        }

        // Animate detail layout
        val detailLayout = findViewById<View>(R.id.detailLayout)
        detailLayout.animate().alpha(1f).setDuration(500).start()

        setupChart()
    }

    private fun checkWatchlistStatus(symbol: String, button: android.widget.Button) {
        lifecycleScope.launch {
            isFavorite = db.watchlistDao().isInWatchlist(symbol)
            updateWatchlistButton(button)
        }
    }

    private fun toggleWatchlist(symbol: String, change: String, accuracy: String, button: android.widget.Button) {
        lifecycleScope.launch {
            val entity = WatchlistEntity(
                symbol = symbol,
                name = symbol, // Using symbol as name for now
                changePercent = change.replace("%", "").replace("+", "").toDoubleOrNull() ?: 0.0,
                accuracy = accuracy.replace("%", "").toIntOrNull() ?: 0
            )

            if (isFavorite) {
                db.watchlistDao().removeFromWatchlist(entity)
                Toast.makeText(this@StockDetailActivity, "Removed from Watchlist", Toast.LENGTH_SHORT).show()
            } else {
                db.watchlistDao().addToWatchlist(entity)
                Toast.makeText(this@StockDetailActivity, "Added to Watchlist", Toast.LENGTH_SHORT).show()
            }
            isFavorite = !isFavorite
            updateWatchlistButton(button)
        }
    }

    private fun updateWatchlistButton(button: android.widget.Button) {
        if (isFavorite) {
            button.text = "REMOVE FROM WATCHLIST"
            button.setBackgroundColor(Color.DKGRAY)
        } else {
            button.text = "ADD TO WATCHLIST"
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.top_pick_background))
        }
    }

    private fun setupChart() {
        val chart = findViewById<LineChart>(R.id.detailChart)
        val entries = ArrayList<Entry>()
        for (i in 0..10) {
            entries.add(Entry(i.toFloat(), (100..150).random().toFloat()))
        }

        val dataSet = LineDataSet(entries, "Price Movement")
        dataSet.color = Color.parseColor("#00E676")
        dataSet.setCircleColor(Color.WHITE)
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#00E676")
        dataSet.fillAlpha = 50

        chart.data = LineData(dataSet)
        chart.description.isEnabled = false
        chart.xAxis.textColor = Color.WHITE
        chart.axisLeft.textColor = Color.WHITE
        chart.axisRight.isEnabled = false
        chart.animateXY(1000, 1000)
    }
}