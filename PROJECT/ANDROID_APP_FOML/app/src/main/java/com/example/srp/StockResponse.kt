package com.example.srp

data class StockResponse(
    val stock: String,
    val cap: String,
    val next_day: Double,
    val next_week: Double,
    val next_month: Double,
    val accuracy: Double
)
