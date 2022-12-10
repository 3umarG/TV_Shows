package com.example.tvshows.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.ui.viewmodel.TvShowsViewModel

class MainActivity : AppCompatActivity() {
    lateinit var tvViewModel: TvShowsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvViewModel = ViewModelProvider(this)[TvShowsViewModel::class.java]


    }

}