package com.example.atslyzer.retroFit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {

        Retrofit.Builder()
            .baseUrl("https://atslyzer.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val apiInterface: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}