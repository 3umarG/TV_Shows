package com.example.tvshows.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.utils.Resource

class MainActivity : AppCompatActivity() {
    private lateinit var tvViewModel: TvShowsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvViewModel = ViewModelProvider(this)[TvShowsViewModel::class.java]

        tvViewModel.getMostPopular(0)

        tvViewModel.tvShowsResponse.observe(this) { resources ->
            if (resources is Resource.Success) {
                val res = resources as Resource.Success
                Toast.makeText(this, "Total Results : ${res.data?.total}", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun testMostPopular() {
        tvViewModel.getMostPopular(0)
        tvViewModel.tvShowsResponse.observe(this) { resource ->
            if (resource is Resource.Loading) {

            }

        }
    }
}