package com.example.tvshows.repository

import com.example.tvshows.data.local.TvShowsDataBase
import com.example.tvshows.data.remote.RetrofitInstance
import com.example.tvshows.pojo.TvShow

class TvRepository(private val db: TvShowsDataBase) {
    suspend fun getMostPopular(page: Int) = RetrofitInstance.api.getMostPopular(page)

    suspend fun getShowDetails(id: Int) = RetrofitInstance.api.getShowDetails(id)

    suspend fun addTvShowToWatchedList(tvShow: TvShow) = db.getDao().insertTvShow(tvShow)

    suspend fun deleteTvShowFromWatchedList(tvShow: TvShow) = db.getDao().deleteTvShow(tvShow)

    fun getAllWatchedList() = db.getDao().getTvShowsWatchedList()
}