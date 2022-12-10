package com.example.tvshows.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshows.R
import com.example.tvshows.adapters.TvShowsRecyclerViewAdapter
import com.example.tvshows.databinding.FragmentPopularShowsBinding
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.utils.Resource

class PopularShowsFragment : Fragment() {
    private lateinit var binding: FragmentPopularShowsBinding
    private lateinit var viewModel: TvShowsViewModel
    private lateinit var rvAdapter: TvShowsRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopularShowsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).tvViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMostPopular(0)

        setupRecyclerViewAndAdapter()

        viewModel.tvShowsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTvShows.visibility = View.VISIBLE
                    val tvShows = resource.data?.tv_shows
                    rvAdapter.differ.submitList(tvShows)
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvTvShows.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTvShows.visibility = View.GONE
                    Toast.makeText(context, "Error !!", Toast.LENGTH_LONG).show()
                }
            }

        }


    }

    private fun setupRecyclerViewAndAdapter() {
        rvAdapter = activity?.applicationContext?.let { TvShowsRecyclerViewAdapter(it) }!!
        binding.rvTvShows.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}