package com.example.tvshows.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.databinding.ItemSingleEpisodeBinding
import com.example.tvshows.pojo.Episode

class EpisodesAdapter(private val episodesList: List<Episode>) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {


    class EpisodeViewHolder(private val binding: ItemSingleEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode) {
            var title = "S"
            var season: String = episode.season.toString()
            if (season.length == 1) {
                season = "0$season"
            }
            var episodeNumber: String = episode.episode.toString()
            if (episodeNumber.length == 1) {
                episodeNumber = "0$episodeNumber"
            }
            episodeNumber = "E$episodeNumber"
            title = "$title$season$episodeNumber"

            binding.episodeTitle.text = title
            binding.episodeAirDate.text = "Air Date: ${episode.air_date.substring(0,10)}"
            binding.episodeName.text = episode.name
        }

        companion object {
            fun create(view: ViewGroup): EpisodeViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = ItemSingleEpisodeBinding.inflate(inflater, view, false)
                return EpisodeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodesList[position])
    }

    override fun getItemCount(): Int {
        return episodesList.size
    }
}