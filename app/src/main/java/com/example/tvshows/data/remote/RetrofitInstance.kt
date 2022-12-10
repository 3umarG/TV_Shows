package com.example.tvshows.data.remote

import com.example.tvshows.utils.TVUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(TVUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiInterface by lazy {
        retrofitBuilder.create(ApiInterface::class.java)
    }
}