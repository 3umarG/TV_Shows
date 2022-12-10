package com.example.tvshows.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TvShowsViewModel : ViewModel() {
    private var repo = TvRepository()
    private var tvShowsMutable: MutableLiveData<Resource<TvShowsResponse>> = MutableLiveData()
    val tvShowsResponse = tvShowsMutable as LiveData<Resource<TvShowsResponse>>

    fun getMostPopular(page: Int) = viewModelScope.launch {
        tvShowsMutable.postValue(Resource.Loading())
        val response = repo.getMostPopular(page)
        tvShowsMutable.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<TvShowsResponse>): Resource<TvShowsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())

    }


}