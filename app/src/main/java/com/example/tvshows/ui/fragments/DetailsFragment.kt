package com.example.tvshows.ui.fragments

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.tvshows.R
import com.example.tvshows.adapters.CarousalSliderAdapter
import com.example.tvshows.adapters.EpisodesAdapter
import com.example.tvshows.databinding.EpisodesBottomSheetLayoutBinding
import com.example.tvshows.databinding.FragmentDetailsBinding
import com.example.tvshows.pojo.TvShowDetails
import com.example.tvshows.ui.BounceEdgeEffectFactory
import com.example.tvshows.ui.activity.MainActivity
import com.example.tvshows.ui.viewmodel.tv_shows.TvShowsViewModel
import com.example.tvshows.utils.Resource
import com.example.tvshows.utils.TVUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_details.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: TvShowsViewModel
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var viewPager: ViewPager2
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var episodesBottomSheetLayoutBinding: EpisodesBottomSheetLayoutBinding
    private var isAddedOrNot : Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).tvViewModel
        loadingVisibility()
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
                    resources.data?.let {
                        setupAdapter(it)
                        Glide.with(this).load(it.tvShow.image_thumbnail_path)
                            .placeholder(R.drawable.progress_animation).into(binding.ivPoster)
                        loadDetails(it)
                    }
                    setupViewPager()
                    binding.ivBack.setOnClickListener {
                        findNavController().popBackStack()
                    }
                    resources.data?.tvShow?.pictures?.size?.let { setUpIndicator(it) }
                    binding.viewPager.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            setupActiveIndicator(position)
                        }
                    })
                    binding.btnEpisodes.setOnClickListener {
                        if (bottomSheetDialog == null) {
                            // Bottom Sheet Dialog
                            bottomSheetDialog = BottomSheetDialog(requireActivity())

                            // Episodes Bottom Sheet Layout Binding
                            episodesBottomSheetLayoutBinding =
                                EpisodesBottomSheetLayoutBinding.inflate(
                                    LayoutInflater.from(requireContext()),
                                    view.findViewById(R.id.episodesBottomSheet),
                                    false
                                )


                            // Set Content View to BottomSheetDialog
                            bottomSheetDialog?.setContentView(
                                episodesBottomSheetLayoutBinding.root
                            )
                            episodesBottomSheetLayoutBinding.recyclerViewEpisodes.apply {
                                adapter = EpisodesAdapter(resources.data?.tvShow?.episodes!!)
                                edgeEffectFactory = BounceEdgeEffectFactory()
                            }

                            episodesBottomSheetLayoutBinding.tvTitleOfEpisodes.text =
                                resources.data?.tvShow?.name
                            episodesBottomSheetLayoutBinding.closeBottomSheet.setOnClickListener {
                                bottomSheetDialog?.dismiss()
                            }

                        }

                        // Starting Point
                        val frameLayout = bottomSheetDialog?.findViewById<FrameLayout>(
                            com.google.android.material.R.id.design_bottom_sheet
                        )
                        if (frameLayout != null) {
                            val bottomSheetBehavior = BottomSheetBehavior.from(frameLayout)
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            bottomSheetBehavior.peekHeight =
                                Resources.getSystem().displayMetrics.heightPixels
                        }

                        // End Point
                        bottomSheetDialog?.show()
                    }

                    binding.floatingAdd.setOnClickListener {
                        addToWatchedList(it as FloatingActionButton)
                    }
                }
                is Resource.Error -> {
                    if (resources.message == TVUtils.NO_INTERNET_CONNECTION){
                        binding.scrollView.visibility = View.GONE
                        binding.floatingAdd.visibility = View.GONE
                        binding.lottieNoInternetDetails.visibility = View.VISIBLE
                    }
                    binding.progressBarLoadingDetails.visibility = View.GONE
                }
                is Resource.Loading -> {
                    loadingVisibility()
                }
            }

        }


    }

    private fun addToWatchedList(fab: FloatingActionButton) {
        // Update the Icon

        fab.setImageResource(R.drawable.ic_baseline_check_24)

        viewModel.insertTvShowToWatchedList(args.tvShow)
        Toast.makeText(context, "Show added successfully", Toast.LENGTH_LONG).show()
    }

    private fun setupViewPager() {
        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
        }
    }

    private fun setupAdapter(data: TvShowDetails) {
        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollView)
        val viewPagerAdapter = data.tvShow.pictures.toTypedArray().let {
            CarousalSliderAdapter(
                it,
                viewPager
            )
        }
        viewPager.adapter = viewPagerAdapter
    }


    private fun setUpIndicator(count: Int) {
        binding.layoutIndicator.removeAllViews()
        binding.layoutIndicator.visibility = View.GONE
        val indicators = mutableListOf<ImageView>()
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 8, 0)
        for (i in 0 until count) {
            indicators.add(i, ImageView(activity?.applicationContext))
            indicators[i].setImageDrawable(
                ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.indicator_inactive
                )
            )
            indicators[i].layoutParams = layoutParams
            binding.layoutIndicator.addView(indicators[i])
        }
        binding.layoutIndicator.visibility = View.VISIBLE
        setupActiveIndicator(0)
    }

    private fun setupActiveIndicator(position: Int) {
        val childCount = binding.layoutIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutIndicator.getChildAt(i) as ImageView
            if (position == i) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity?.applicationContext!!, R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity?.applicationContext!!, R.drawable.indicator_inactive
                    )
                )
            }

        }
    }


    private fun loadingVisibility() {
        binding.apply {
            progressBarLoadingDetails.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            floatingAdd.visibility = View.GONE
            binding.lottieNoInternetDetails.visibility = View.GONE
        }
    }

    private fun successVisibility() {
        binding.apply {
            progressBarLoadingDetails.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            floatingAdd.visibility = View.VISIBLE
            binding.lottieNoInternetDetails.visibility = View.GONE
        }
    }

    private fun loadDetails(tvShow: TvShowDetails) {
        binding.apply {
            textShowName.text = tvShow.tvShow.name
            textStatus.apply {
                text = tvShow.tvShow.status
                if (tvShow.tvShow.status == "Ended") {
                    this.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorThemeExtra
                        )
                    )
                } else if (tvShow.tvShow.status == "Running") {
                    this.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorTextOther
                        )
                    )
                }
            }
            textNetwork.text = tvShow.tvShow.network
            textDate.text = "Started on: ${tvShow.tvShow.start_date}"
            textDescription.text = tvShow.tvShow.description
            textReadMore.setOnClickListener {
                if ((it as TextView).text == "Read more") {
                    // Expand
                    textDescription.apply {
                        maxLines = Int.MAX_VALUE
                        ellipsize = null
                    }
                    (it as TextView).text = getString(R.string.read_less)
                } else {
                    // Down
                    textDescription.apply {
                        maxLines = 4
                        ellipsize = TextUtils.TruncateAt.END
                    }
                    (it as TextView).text = getString(R.string.read_more)
                }
            }
            textRate.text = String.format(
                "%.2f",
                tvShow.tvShow.rating.toDouble()
            )
            textTime.text = "${tvShow.tvShow.runtime} Min"
            textGenre.text = if (tvShow.tvShow.genres.isNotEmpty()) {
                tvShow.tvShow.genres[0]
            } else {
                "N/A"
            }
            btnWebsite.setOnClickListener {
                val webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(tvShow.tvShow.url))
                startActivity(webIntent)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            scrollView.visibility = View.GONE
            progressBarLoadingDetails.visibility = View.GONE
        }
    }
}