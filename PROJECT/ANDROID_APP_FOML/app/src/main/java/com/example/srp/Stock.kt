package com.example.srp

data class Stock(
    val symbol: String,
    val name: String,
    val changePercent: Double,
    val accuracy: Int,
    val isTopPick: Boolean = false
)