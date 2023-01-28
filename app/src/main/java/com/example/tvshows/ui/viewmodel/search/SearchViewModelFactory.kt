package com.example.tvshows.ui.viewmodel.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.repository.TvRepository

class SearchViewModelFactory(private val repository: TvRepository , private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository , context) as T
    }
}