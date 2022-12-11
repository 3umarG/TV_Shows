package com.example.tvshows.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.tvshows.R
import com.example.tvshows.databinding.CarousalItemBinding


class CarousalSliderAdapter(
    private val imagesPath: Array<String>,
    private val viewPager: ViewPager2,
) :
    RecyclerView.Adapter<CarousalSliderAdapter.CarousalViewHolder>() {

    class CarousalViewHolder(private val binding: CarousalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(view: ViewGroup): CarousalViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = CarousalItemBinding.inflate(inflater, view, false)
                return CarousalViewHolder(binding)
            }
        }

        fun bind(path: String) {
            Glide.with(itemView).load(path)
                .placeholder(R.drawable.progress_animation)
                .into(binding.imgCarousalItem)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarousalViewHolder {
        return CarousalViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CarousalViewHolder, position: Int) {
        val path = imagesPath[position]
        holder.bind(path)
        if (position == imagesPath.size - 1) {
            viewPager.post {
                imagesPath.toMutableList().addAll(imagesPath)
                notifyItemChanged(imagesPath.size - 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return imagesPath.size
    }
}