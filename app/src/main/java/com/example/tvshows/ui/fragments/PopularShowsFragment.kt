package com.example.tvshows.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.adapters.TvShowsRecyclerViewAdapter
import com.example.tvshows.databinding.FragmentPopularShowsBinding
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.ui.BounceEdgeEffectFactory
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.utils.Resource
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class PopularShowsFragment : Fragment() {
    private lateinit var binding: FragmentPopularShowsBinding
    private lateinit var viewModel: TvShowsViewModel
    private lateinit var rvAdapter: TvShowsRecyclerViewAdapter
    private var currentPage = 1
    private var nextPage = currentPage + 1
    private var currentList: MutableList<TvShow> = mutableListOf()
    private var newList: MutableList<TvShow> = mutableListOf()
    private var isLoadingMore = false
    private var flag = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flag = true
    }

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

        setupRecyclerViewAndAdapter()
        viewModel.tvShowsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTvShows.visibility = View.VISIBLE
                    currentList = (resource.data?.tv_shows as MutableList<TvShow>?)!!
                    newList.addAll(currentList)
                    rvAdapter.differ.submitList(newList)
                    if (isLoadingMore) {
                        binding.rvTvShows.adapter?.notifyItemChanged(rvAdapter.differ.currentList.size)
                        isLoadingMore = false
                        binding.progressBarLoadingMore.visibility = View.GONE
                    }
                }
                is Resource.Loading -> {
                    if (isLoadingMore) {
                        binding.progressBarLoadingMore.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvTvShows.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTvShows.visibility = View.GONE
                    Toast.makeText(context, "Error !!", Toast.LENGTH_LONG).show()
                }
            }

        }
        binding.rvTvShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.rvTvShows.canScrollVertically(1)) {
                    isLoadingMore = true
//                    binding.progressBarLoadingMore.visibility = View.VISIBLE
                    viewModel.getMostPopular(nextPage)
                    currentPage = nextPage
                    nextPage++
                }
            }
        })

        if (flag) {
            viewModel.getMostPopular(currentPage)
        } else {
            Toast.makeText(context, "No More Load !!", Toast.LENGTH_LONG).show()
        }

    }

    private fun setupRecyclerViewAndAdapter() {
        rvAdapter = activity?.applicationContext?.let { TvShowsRecyclerViewAdapter(it) }!!
        rvAdapter.setOnItemClickListener {
            val action =
                PopularShowsFragmentDirections.actionPopularShowsFragmentToDetailsFragment(it.id)
            findNavController().navigate(action)
        }
        binding.rvTvShows.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            edgeEffectFactory = BounceEdgeEffectFactory()
        }
    }

    override fun onPause() {
        super.onPause()
        flag = false

    }

}