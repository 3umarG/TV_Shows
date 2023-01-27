package com.example.tvshows.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.repository.TvRepository

class WatchedListViewModelFactory(private val repository: TvRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WatchedListViewModel(repository) as T
    }
}