package com.example.datavisualizationapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart

    private lateinit var autoCompleteDay: MaterialAutoCompleteTextView
    private lateinit var etAmount: EditText
    private lateinit var tvTotal: TextView
    private lateinit var tvHighest: TextView

    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val expenses = mutableListOf(120f, 180f, 90f, 250f, 300f, 140f, 200f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinner()
        setupCharts()

        updateCharts()
        updateSummary()

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            updateData()
        }
    }

    private fun initViews() {
        barChart = findViewById(R.id.barChart)
        lineChart = findViewById(R.id.lineChart)
        pieChart = findViewById(R.id.pieChart)
        autoCompleteDay = findViewById(R.id.spinnerDay)
        etAmount = findViewById(R.id.etAmount)
        tvTotal = findViewById(R.id.tvTotal)
        tvHighest = findViewById(R.id.tvHighest)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, days)
        autoCompleteDay.setAdapter(adapter)
        autoCompleteDay.setText(days[0], false)
    }

    private fun setupCharts() {
        // Shared Chart Styling
        val charts = listOf(barChart, lineChart, pieChart)
        charts.forEach { chart ->
            chart.description.isEnabled = false
            chart.legend.isEnabled = true
            chart.animateY(1000)
        }

        // Bar Chart Styling
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(days)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
        }
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false

        // Line Chart Styling
        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(days)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
        }
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.isEnabled = false

        // Pie Chart Styling
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setEntryLabelColor(Color.BLACK)
    }

    private fun updateData() {
        val input = etAmount.text.toString()
        val selectedDay = autoCompleteDay.text.toString()

        if (input.isEmpty()) {
            etAmount.error = getString(R.string.enter_amount)
            return
        }

        val value = input.toFloatOrNull()
        if (value == null || value < 0) {
            etAmount.error = getString(R.string.invalid_amount)
            return
        }

        val index = days.indexOf(selectedDay)
        if (index != -1) {
            expenses[index] = value
            updateCharts()
            updateSummary()
            etAmount.text.clear()
            etAmount.error = null
            Toast.makeText(this, "Updated $selectedDay", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCharts() {
        updateBarChart()
        updateLineChart()
        updatePieChart()
    }

    private fun updateBarChart() {
        val entries = expenses.mapIndexed { i, v -> BarEntry(i.toFloat(), v) }

        val dataSet = BarDataSet(entries, getString(R.string.daily_expenses))
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 10f

        barChart.data = BarData(dataSet)
        barChart.invalidate()
    }

    private fun updateLineChart() {
        val entries = expenses.mapIndexed { i, v -> Entry(i.toFloat(), v) }
        val themeColor = "#6200EE".toColorInt()

        val dataSet = LineDataSet(entries, getString(R.string.spending_trend)).apply {
            color = themeColor
            setCircleColor(themeColor)
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(true)
            valueTextSize = 10f
            setDrawFilled(true)
            fillAlpha = 50
            fillColor = themeColor
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        lineChart.data = LineData(dataSet)
        lineChart.invalidate()
    }

    private fun updatePieChart() {
        val entries = mutableListOf<PieEntry>()

        expenses.forEachIndexed { i, v ->
            if (v > 0) entries.add(PieEntry(v, days[i]))
        }

        val dataSet = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 12f
            valueTextColor = Color.BLACK
            sliceSpace = 3f
        }

        pieChart.data = PieData(dataSet)
        pieChart.centerText = "Expense\nBreakdown"
        pieChart.setCenterTextSize(16f)
        pieChart.invalidate()
    }

    private fun updateSummary() {
        val total = expenses.sum()
        val max = expenses.maxOrNull() ?: 0f
        val index = expenses.indexOf(max)

        val totalStr = String.format(Locale.US, "%.2f", total)
        val maxStr = String.format(Locale.US, "%.2f", max)

        tvTotal.text = getString(R.string.total_expense_format, totalStr)
        tvHighest.text = getString(R.string.highest_spending_format, days[index], maxStr)
    }
}