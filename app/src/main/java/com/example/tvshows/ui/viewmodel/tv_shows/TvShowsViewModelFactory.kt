package com.example.tvshows.ui.viewmodel.tv_shows

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.repository.TvRepository

class TvShowsViewModelFactory(private val repository: TvRepository , private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TvShowsViewModel(repository , context) as T
    }
}