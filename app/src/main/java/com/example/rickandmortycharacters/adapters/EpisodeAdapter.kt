package com.example.rickandmortycharacters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.models.Episode
import kotlinx.android.synthetic.main.item_episode.*
import kotlinx.android.synthetic.main.item_episode.view.*
import kotlinx.android.synthetic.main.item_episode.view.arrow_button
import kotlinx.android.synthetic.main.item_episode.view.card_view
import kotlinx.android.synthetic.main.item_episode.view.expandable_view

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private val episodes by lazy { mutableListOf<Episode>() }

    fun setEpisodes(episodes: List<Episode>){
        if (episodes.isNotEmpty()){
            this.episodes.clear()
        }
        this.episodes.addAll(episodes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_episode, parent, false)

        return EpisodeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]
        holder.bind(episode)
    }

    class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        fun bind(episode: Episode){
            with(itemView){
                text_episode_name.text = episode.name
                text_episode_number.text = episode.episode_number
                text_episode_airDate.text = episode.air_date
            }
            itemView.arrow_button.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            with(itemView) {
                if (expandable_view.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(card_view, AutoTransition())
                    expandable_view.visibility = View.VISIBLE
                    arrow_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                } else {
                    TransitionManager.beginDelayedTransition(card_view, AutoTransition())
                    expandable_view.visibility = View.GONE
                    arrow_button.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                }
            }
        }

    }

}