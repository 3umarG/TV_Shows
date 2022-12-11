package com.example.tvshows.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvshows.R
import com.example.tvshows.databinding.TvShowContainerBinding
import com.example.tvshows.pojo.TvShow

class TvShowsRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<TvShowsRecyclerViewAdapter.TvShowViewHolder>() {

    private var onItemClickListener: ((TvShow) -> Unit)? = null

    private val differCallback = object : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    class TvShowViewHolder(private val binding: TvShowContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShow, context: Context) {
            binding.tvShowName.text = tvShow.name
            binding.tvShowNetwork.text = tvShow.network
            binding.tvShowStatus.text = tvShow.status
            binding.tvShowStarted.text = tvShow.start_date
            if (tvShow.status == "Ended") {
                binding.tvShowStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorThemeExtra
                    )
                )
            } else if (tvShow.status == "Running") {
                binding.tvShowStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorTextOther
                    )
                )
            }
            Glide.with(itemView).load(tvShow.image_thumbnail_path)
                .placeholder(R.drawable.progress_animation)
                .into(binding.imageTvShow)
        }

        companion object {
            fun create(view: ViewGroup): TvShowViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = TvShowContainerBinding.inflate(inflater, view, false)
                return TvShowViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        return TvShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = differ.currentList[position]
        holder.bind(tvShow, context)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(tvShow)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (TvShow) -> Unit) {
        onItemClickListener = listener
    }




}