package com.example.tvshows.ui.viewmodel.watched_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.repository.TvRepository
import kotlinx.coroutines.launch

class WatchedListViewModel(private val repository: TvRepository) : ViewModel() {

    fun getAllWatchedList() = repository.getAllWatchedList()

    fun deleteTvShow(tvShow: TvShow) = viewModelScope.launch {
        repository.deleteTvShowFromWatchedList(tvShow)
    }

    fun insertTvShow(tvShow: TvShow) = viewModelScope.launch {
        repository.addTvShowToWatchedList(tvShow)
    }

}