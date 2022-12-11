package com.example.tvshows.repository

import com.example.tvshows.data.remote.RetrofitInstance

class TvRepository {
    suspend fun getMostPopular(page: Int) = RetrofitInstance.api.getMostPopular(page)

    suspend fun getShowDetails(id: Int) = RetrofitInstance.api.getShowDetails(id)
}