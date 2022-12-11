package com.example.tvshows.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tvshows.R
import com.example.tvshows.databinding.FragmentDetailsBinding
import com.example.tvshows.databinding.FragmentPopularShowsBinding
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.utils.Resource

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: TvShowsViewModel
    private val args by navArgs<DetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).tvViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.tvShowId
        viewModel.getShowDetails(id)
        viewModel.tvShowDetails.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Success -> {
                    binding.progressBarLoadingDetails.visibility = View.GONE
                    binding.tvInfo.apply {
                        visibility = View.VISIBLE
                        text = resources.data?.tvShow?.name
                    }
                }
                is Resource.Error -> {
                    binding.progressBarLoadingDetails.visibility = View.GONE
                    binding.tvInfo.apply {
                        visibility = View.VISIBLE
                        text = "Error"
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarLoadingDetails.visibility = View.VISIBLE
                    binding.tvInfo.visibility = View.GONE
                }
            }

        }

    }


}