package com.example.tvshows.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.adapters.WatchedListAdapter
import com.example.tvshows.databinding.FragmentWatchedListBinding
import com.example.tvshows.pojo.TvShow
import com.example.tvshows.ui.BounceEdgeEffectFactory
import com.example.tvshows.ui.listeners.WatchedListListener
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.listeners.RecyclerViewSwipeListener
import com.example.tvshows.ui.viewmodel.WatchedListViewModel
import com.google.android.material.snackbar.Snackbar

class WatchedListFragment : Fragment(), WatchedListListener, RecyclerViewSwipeListener {


    private lateinit var binding: FragmentWatchedListBinding
    private lateinit var viewModel: WatchedListViewModel
    private lateinit var watchedListAdapter: WatchedListAdapter
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // TODO : our functionality
            onSwipe(viewHolder)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWatchedListBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).watchedListViewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        navigation()
    }


    override fun onDelete(tvShow: TvShow) {
        viewModel.deleteTvShow(tvShow)
        loadData()
        Snackbar.make(requireView(), "${tvShow.name} Deleted !!", Snackbar.LENGTH_LONG).apply {
            setAction("UNDO") {
                viewModel.insertTvShow(tvShow)
                loadData()
            }
        }.show()
    }

    override fun loadData() {
        viewModel.getAllWatchedList().observe(viewLifecycleOwner) { watchedList ->
            watchedListAdapter = WatchedListAdapter(watchedList, requireContext())
            watchedListAdapter.setOnDeleteClick {
                onDelete(it)
            }
            binding.rvWatchedList.apply {
                adapter = watchedListAdapter
                layoutManager = LinearLayoutManager(context)
                edgeEffectFactory = BounceEdgeEffectFactory()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvWatchedList)
        }
    }

    override fun navigation() {
        binding.ivBackWatchedList.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onSwipe(viewHolder: RecyclerView.ViewHolder) {
        val tvShow = watchedListAdapter.getWatchedList()[viewHolder.absoluteAdapterPosition]
        onDelete(tvShow)
    }


}