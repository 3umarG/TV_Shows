package com.example.tvshows.ui.fragments

import android.content.res.Resources
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.tvshows.adapters.CarousalSliderAdapter
import com.example.tvshows.databinding.FragmentDetailsBinding
import com.example.tvshows.pojo.TvShowDetails
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.viewmodel.TvShowsViewModel
import com.example.tvshows.utils.Resource
import kotlin.math.abs
import kotlin.math.max

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: TvShowsViewModel
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var viewPager: ViewPager2


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

        viewPager = binding.viewPager
        val id = args.tvShowId
        viewModel.getShowDetails(id)

        viewModel.tvShowDetails.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Success -> {
                    successVisibility()
                    binding.tvInfo.text = resources.data?.tvShow?.name
                    resources.data?.let {
                        setupAdapter(it)
                    }
                    setupViewPager()
//                    setUpTransformer()
                }
                is Resource.Error -> {
                    binding.progressBarLoadingDetails.visibility = View.GONE
                    binding.tvInfo.apply {
                        visibility = View.VISIBLE
                        text = "Error"
                    }
                }
                is Resource.Loading -> {
                    loadingVisibility()
                }
            }

        }


    }

    private fun setupViewPager() {
        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding

//            offscreenPageLimit = 3  // Render the left and right items
//            (getChildAt(0) as RecyclerView).overScrollMode =
//                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }
    }

    private fun setupAdapter(data: TvShowDetails) {
        val viewPagerAdapter = data.tvShow.pictures.toTypedArray().let {
            CarousalSliderAdapter(
                it,
                viewPager
            )
        }
        viewPager.adapter = viewPagerAdapter
    }


    /*
    private fun setUpTransformer() {
        // Padding
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))


        // Zoom Effect :
        compositePageTransformer.addTransformer(MarginPageTransformer((30 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }


        viewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    
    class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = horzMargin - vertMargin / 2
                        if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
*/

    private fun loadingVisibility() {
        binding.progressBarLoadingDetails.visibility = View.VISIBLE
        binding.viewPager.visibility = View.GONE
        binding.tvInfo.visibility = View.GONE
    }

    private fun successVisibility() {
        binding.apply {
            progressBarLoadingDetails.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
            binding.tvInfo.visibility = View.VISIBLE
        }
    }


//    companion object {
//        private const val MIN_SCALE = 0.75f
//        private const val MIN_ALPHA = 0.5f
//    }
}