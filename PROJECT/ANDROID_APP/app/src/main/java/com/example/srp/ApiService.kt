package com.example.srp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("predict")
    fun getPredictions(): Call<List<StockResponse>>
}