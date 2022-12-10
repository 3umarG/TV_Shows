package com.example.tvshows.repository

import com.example.tvshows.data.remote.RetrofitInstance

class TvRepository {
    suspend fun getMostPopular(page: Int) = RetrofitInstance.api.getMostPopular(page)
}