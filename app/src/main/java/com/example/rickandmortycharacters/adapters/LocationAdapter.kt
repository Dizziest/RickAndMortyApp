package com.example.rickandmortycharacters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.models.Location
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.android.synthetic.main.item_location.view.arrow_button
import kotlinx.android.synthetic.main.item_location.view.card_view
import kotlinx.android.synthetic.main.item_location.view.expandable_view

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val locations by lazy { mutableListOf<Location>() }

    fun setLocations(locations: List<Location>){
        if (locations.isNotEmpty()){
            this.locations.clear()
        }
        this.locations.addAll(locations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_location, parent, false)

        return LocationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(location: Location){
            with(itemView){
                text_location_name.text = location.name
                text_location_type.text = "Type: " + location.type
                text_location_dimension.text = "Dimension: " + location.dimension
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