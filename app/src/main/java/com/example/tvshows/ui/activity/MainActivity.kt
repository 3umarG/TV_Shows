package com.example.tvshows.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.data.local.TvShowsDataBase
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.ui.viewmodel.TvShowsViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var tvViewModel: TvShowsViewModel
    private lateinit var tvShowsViewModelFactory: TvShowsViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvShowsViewModelFactory =
            TvShowsViewModelFactory(
                repository = TvRepository(
                    TvShowsDataBase.createDatabase(this)
                )
            )
        tvViewModel = ViewModelProvider(this, tvShowsViewModelFactory)[TvShowsViewModel::class.java]


    }

}