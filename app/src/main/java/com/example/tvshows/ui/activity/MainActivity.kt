package com.example.tvshows.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.data.local.TvShowsDataBase
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.ui.viewmodel.TvShowsViewModelFactory
import com.example.tvshows.ui.viewmodel.WatchedListViewModel
import com.example.tvshows.ui.viewmodel.WatchedListViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var tvViewModel: TvShowsViewModel
    private lateinit var tvShowsViewModelFactory: TvShowsViewModelFactory

    lateinit var watchedListViewModel: WatchedListViewModel
    private lateinit var watchedListViewModelFactory: WatchedListViewModelFactory
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


        watchedListViewModelFactory =
            WatchedListViewModelFactory(
                repository = TvRepository(
                    TvShowsDataBase.createDatabase(this)
                )
            )
        watchedListViewModel =
            ViewModelProvider(this, watchedListViewModelFactory)[WatchedListViewModel::class.java]

    }

}