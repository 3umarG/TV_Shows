package com.example.tvshows.ui.listeners

import com.example.tvshows.pojo.TvShow

interface WatchedListListener {
    fun onDelete(tvShow: TvShow)

    fun loadData()

    fun navigation()
}