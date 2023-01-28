package com.example.tvshows.ui.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.ui.handler.ResponseHandler
import com.example.tvshows.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: TvRepository) : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<TvShowsResponse>> = MutableLiveData()
    var searchLiveData = mutableLiveData as LiveData<Resource<TvShowsResponse>>

    private val responseHandler: ResponseHandler<TvShowsResponse> =
        ResponseHandler()

    fun search(q: String, page: Int) {
        viewModelScope.launch {
            mutableLiveData.postValue(Resource.Loading())

            val response = repository.search(q, page)
            mutableLiveData.postValue(responseHandler.handleResponse(response))
        }
    }

}