package com.example.tvshows.ui.viewmodel.tv_shows

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshows.network.ConnectivityObserver
import com.example.tvshows.network.InternetConnectivityObserver
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.pojo.TvShowDetails
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.utils.Resource
import com.example.tvshows.utils.TVUtils
import kotlinx.coroutines.launch
import retrofit2.Response

class TvShowsViewModel(
    private val repo: TvRepository,
    private val context: Context
) : ViewModel() {

    private val connectivityObserver: ConnectivityObserver = InternetConnectivityObserver(context)

    // Popular Movies
    private var tvShowsMutable: MutableLiveData<Resource<TvShowsResponse>> = MutableLiveData()
    val tvShowsResponse = tvShowsMutable as LiveData<Resource<TvShowsResponse>>

    fun getMostPopular(page: Int) = viewModelScope.launch {
        connectivityObserver.observe().collect { status ->
            if (status == ConnectivityObserver.Status.AVAILABLE) {
                tvShowsMutable.postValue(Resource.Loading())
                val response = repo.getMostPopular(page)
                tvShowsMutable.postValue(handleResponse(response))
            }else{
                tvShowsMutable.postValue(Resource.Error(message = TVUtils.NO_INTERNET_CONNECTION))
            }
        }
    }

    private fun handleResponse(response: Response<TvShowsResponse>): Resource<TvShowsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }


    // Details
    private var tvShowDetailsMutable: MutableLiveData<Resource<TvShowDetails>> = MutableLiveData()
    val tvShowDetails = tvShowDetailsMutable as LiveData<Resource<TvShowDetails>>

    fun getShowDetails(id: Int) = viewModelScope.launch {
        tvShowDetailsMutable.postValue(Resource.Loading())
        val response = repo.getShowDetails(id)
        tvShowDetailsMutable.postValue(handleDetailsResponse(response))
    }

    private fun handleDetailsResponse(response: Response<TvShowDetails>): Resource<TvShowDetails> {
        if (response.isSuccessful) {
            response.body()?.let { details ->
                return Resource.Success(details)
            }
        }
        return Resource.Error(message = response.message())
    }


    fun insertTvShowToWatchedList(tvShow: TvShow) = viewModelScope.launch {
        repo.addTvShowToWatchedList(tvShow)
    }

    fun deleteTvShowFromWatchedList(tvShow: TvShow) = viewModelScope.launch {
        repo.deleteTvShowFromWatchedList(tvShow)
    }

    fun getAllWatchedList(): LiveData<List<TvShow>> = repo.getAllWatchedList()


}