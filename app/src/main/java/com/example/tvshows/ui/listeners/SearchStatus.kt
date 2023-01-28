package com.example.tvshows.ui.listeners

interface SearchStatus<T> {
    fun onLoading()

    fun onSuccess(response :T)

    fun onError()
}