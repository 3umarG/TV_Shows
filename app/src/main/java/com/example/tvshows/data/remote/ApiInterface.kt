package com.example.tvshows.data.remote

import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.utils.TVUtils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(TVUtils.MOST_POPULAR_END_POINT)
    suspend fun getMostPopular(
        @Query("page") page: Int
    ): Response<TvShowsResponse>



}