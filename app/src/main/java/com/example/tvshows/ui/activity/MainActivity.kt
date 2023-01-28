package com.example.tvshows.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.data.local.TvShowsDataBase
import com.example.tvshows.repository.TvRepository
import com.example.tvshows.ui.viewmodel.search.SearchViewModel
import com.example.tvshows.ui.viewmodel.search.SearchViewModelFactory
import com.example.tvshows.ui.viewmodel.tv_shows.TvShowsViewModel
import com.example.tvshows.ui.viewmodel.tv_shows.TvShowsViewModelFactory
import com.example.tvshows.ui.viewmodel.watched_list.WatchedListViewModel
import com.example.tvshows.ui.viewmodel.watched_list.WatchedListViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var tvViewModel: TvShowsViewModel
    private lateinit var tvShowsViewModelFactory: TvShowsViewModelFactory

    lateinit var watchedListViewModel: WatchedListViewModel
    private lateinit var watchedListViewModelFactory: WatchedListViewModelFactory

    lateinit var searchViewModel: SearchViewModel
    lateinit var searchViewModelFactory: SearchViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvShowsViewModelFactory =
            TvShowsViewModelFactory(
                repository = TvRepository(
                    TvShowsDataBase.createDatabase(this)
                ),
                context = applicationContext
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


        searchViewModelFactory = SearchViewModelFactory(
            repository = TvRepository(
                TvShowsDataBase.createDatabase(this)
            ),
            applicationContext
        )
        searchViewModel =
            ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]

    }

}