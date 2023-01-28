package com.example.tvshows.ui.handler

import com.example.tvshows.utils.Resource
import retrofit2.Response

class ResponseHandler<T> {

    fun handleResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful){
            response.body().let {
                return Resource.Success(it!!)
            }
        }else {
            return Resource.Error(message = response.message())
        }
    }

}