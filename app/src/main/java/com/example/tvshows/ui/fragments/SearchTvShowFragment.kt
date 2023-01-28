package com.example.tvshows.ui.fragments

import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.adapters.TvShowsRecyclerViewAdapter

import com.example.tvshows.databinding.FragmentSearchTvShowBinding
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.listeners.SearchStatus
import com.example.tvshows.ui.viewmodel.search.SearchViewModel
import com.example.tvshows.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchTvShowFragment : Fragment(), SearchStatus<TvShowsResponse> {


    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchTvShowBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewModel = (activity as MainActivity).searchViewModel
        binding = FragmentSearchTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null
        binding.editTextSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                if (editable.toString().isNotEmpty()) {
                    viewModel.search(editable.toString(), 1)
                }
            }
        }

        setupRecyclerView()
        binding.ivBackSearch.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    onLoading()
                }
                is Resource.Success -> {
                    onSuccess(it.data!!)
                }
                is Resource.Error -> {
                    onError()
                }
            }
        }


    }

    private fun setupRecyclerView() {
        val searchAdapter = TvShowsRecyclerViewAdapter(requireContext())
        binding.rvSearch?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onLoading() {
        binding.apply {
            rvSearch?.visibility = View.GONE
            progressBarSearch?.visibility = View.VISIBLE
            progressBarLoadingMoreSearch?.visibility = View.GONE
        }
    }

    override fun onSuccess(response: TvShowsResponse) {
        binding.apply {
            rvSearch?.visibility = View.VISIBLE
            progressBarSearch?.visibility = View.GONE
            progressBarLoadingMoreSearch?.visibility = View.GONE
        }
        loadDataToRecyclerView(response.tv_shows)
    }

    private fun loadDataToRecyclerView(data: List<TvShow>) {
        val searchAdapter = TvShowsRecyclerViewAdapter(requireContext())
        searchAdapter.differ.submitList(data)
        searchAdapter.setOnItemClickListener {
            val actions = SearchTvShowFragmentDirections.actionSearchTvShowFragmentToDetailsFragment(it.id!! , it)
            findNavController().navigate(actions)
        }
        binding.rvSearch?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onError() {

    }

}