package com.example.tvshows.ui.viewmodel.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshows.network.ConnectivityObserver
import com.example.tvshows.network.InternetConnectivityObserver
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.ui.handler.ResponseHandler
import com.example.tvshows.utils.Resource
import com.example.tvshows.utils.TVUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: TvRepository, val context: Context) : ViewModel() {

    private var mutableLiveData: MutableLiveData<Resource<TvShowsResponse>> = MutableLiveData()
    var searchLiveData = mutableLiveData as LiveData<Resource<TvShowsResponse>>

    private val connectivityObserver: ConnectivityObserver = InternetConnectivityObserver(context)

    private val responseHandler: ResponseHandler<TvShowsResponse> =
        ResponseHandler()

    fun search(q: String, page: Int) {

        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.AVAILABLE) {
                    mutableLiveData.postValue(Resource.Loading())

                    val response = repository.search(q, page)
                    mutableLiveData.postValue(responseHandler.handleResponse(response))
                } else {
                    mutableLiveData.postValue(Resource.Error(message = TVUtils.NO_INTERNET_CONNECTION))
                }
            }

        }
    }

}