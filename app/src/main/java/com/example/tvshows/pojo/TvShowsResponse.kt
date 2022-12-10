package com.example.tvshows.pojo

data class TvShowsResponse(
    val page: Int,
    val pages: Int,
    val total: String,
    val tv_shows: List<TvShow>
)