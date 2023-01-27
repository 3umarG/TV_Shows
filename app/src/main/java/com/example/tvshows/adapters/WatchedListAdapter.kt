package com.example.tvshows.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvshows.R
import com.example.tvshows.databinding.TvShowContainerBinding
import com.example.tvshows.pojo.TvShow
import kotlinx.android.synthetic.main.tv_show_container.view.*

class WatchedListAdapter(
    private val watchedList: List<TvShow>,
    private val context: Context
) : RecyclerView.Adapter<WatchedListAdapter.WatchedListViewHolder>() {

    private var onDeleteClickListener: ((TvShow) -> Unit)? = null

    fun setOnDeleteClick(listener: (TvShow) -> Unit) {
        onDeleteClickListener = listener
    }

    class WatchedListViewHolder(private val binding: TvShowContainerBinding) :
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
            fun create(parent: ViewGroup): WatchedListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TvShowContainerBinding.inflate(inflater, parent, false)
                return WatchedListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedListViewHolder {
        return WatchedListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WatchedListViewHolder, position: Int) {
        holder.bind(watchedList[position], context)
        holder.itemView.ivDelete.setOnClickListener {
            onDeleteClickListener?.let {
                it(watchedList[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return watchedList.size
    }

    fun getWatchedList(): List<TvShow> = watchedList
}