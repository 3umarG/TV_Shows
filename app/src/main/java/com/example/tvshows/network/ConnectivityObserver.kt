package com.example.tvshows.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe() : Flow<Status>

    enum class Status {
        AVAILABLE , UNAVAILABLE , LOSING , LOST
    }
}