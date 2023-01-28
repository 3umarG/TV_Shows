package com.example.tvshows.ui.fragments

import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.adapters.TvShowsRecyclerViewAdapter

import com.example.tvshows.databinding.FragmentSearchTvShowBinding
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.pojo.TvShowsResponse
import com.example.tvshows.ui.BounceEdgeEffectFactory
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.listeners.SearchStatus
import com.example.tvshows.ui.viewmodel.search.SearchViewModel
import com.example.tvshows.utils.Resource
import com.example.tvshows.utils.TVUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchTvShowFragment : Fragment(), SearchStatus<TvShowsResponse> {


    private lateinit var searchAdapter: TvShowsRecyclerViewAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchTvShowBinding
    private var currentPage = 1
    private var nextPage = currentPage + 1
    private var currentList: MutableList<TvShow> = mutableListOf()
    private var newList: MutableList<TvShow> = mutableListOf()
    private var isLoadingMore = false
    private var q: String = ""


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
                q = editable.toString()
                if (editable.toString().isNotEmpty()) {
                    viewModel.search(q, currentPage)
                } else {
                    // IsEmpty Text Field
                    // Reset for all values that affect on search
                    currentList = mutableListOf()
                    newList = mutableListOf()
                    currentPage = 1
                    nextPage = currentPage + 1
                }
            }
        }

        setupRecyclerView()
        binding.ivBackSearch.setOnClickListener {
            findNavController().popBackStack()
        }


        // New API Call when reach the end ...
        binding.rvSearch?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.rvSearch!!.canScrollVertically(1)) {
                    isLoadingMore = true
                    viewModel.search(q, nextPage)
                    currentPage = nextPage
                    nextPage++
                }
            }
        })



        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    if (isLoadingMore) {
                        binding.progressBarLoadingMoreSearch?.visibility = View.VISIBLE
                    } else {
                        onLoading()
                    }
                }
                is Resource.Success -> {
                    if (q == "") {
                        currentList = mutableListOf()
                        newList = mutableListOf()
                        loadDataToRecyclerView(currentList)
                    }
                    onSuccess(it.data!!)
                }
                is Resource.Error -> {
                    if (it.message == TVUtils.NO_INTERNET_CONNECTION) {
                        println("STATUS ::: NO INTERNET CONNECTION")
                        binding.lottieNoInternetSearch?.visibility = View.VISIBLE
                    }
                    onError()
                }
            }
        }


    }

    private fun setupRecyclerView() {
        searchAdapter = TvShowsRecyclerViewAdapter(requireContext())
        binding.rvSearch?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
            edgeEffectFactory = BounceEdgeEffectFactory()
        }
        searchAdapter.setOnItemClickListener {
            val actions =
                SearchTvShowFragmentDirections.actionSearchTvShowFragmentToDetailsFragment(
                    it.id!!,
                    it
                )
            findNavController().navigate(actions)
        }
    }

    override fun onLoading() {
        binding.apply {
            rvSearch?.visibility = View.GONE
            progressBarSearch?.visibility = View.VISIBLE
            progressBarLoadingMoreSearch?.visibility = View.GONE
            lottieNoInternetSearch?.visibility = View.GONE

        }
    }

    override fun onSuccess(response: TvShowsResponse) {
        binding.apply {
            progressBarSearch?.visibility = View.GONE
            progressBarLoadingMoreSearch?.visibility = View.GONE
            lottieNoInternetSearch?.visibility = View.GONE
        }
        if (response.tv_shows.isEmpty()) {
            // Not Found Matches ...
            binding.apply {
                rvSearch?.visibility = View.GONE
                lottieNoMatchesSearch?.visibility = View.VISIBLE
            }
        } else {
            // Have DATA ...
            binding.lottieNoMatchesSearch?.visibility = View.GONE
            binding.rvSearch?.visibility = View.VISIBLE
            newList = response.tv_shows.toMutableList()
            if (!currentList.containsAll(newList)) {
                // You reach the end of pagination
                currentList.addAll(newList)
                loadDataToRecyclerView(currentList)
            } else {
                Toast.makeText(requireContext(), "You reach to the end", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadDataToRecyclerView(data: List<TvShow>) {

        searchAdapter.differ.submitList(data)
        if (isLoadingMore) {
            binding.rvSearch?.adapter?.notifyItemChanged(searchAdapter.differ.currentList.size)
            isLoadingMore = false
            binding.progressBarLoadingMoreSearch?.visibility = View.GONE
        }
    }

    override fun onError() {
        binding.apply {
            rvSearch?.visibility = View.GONE
        }
    }

}