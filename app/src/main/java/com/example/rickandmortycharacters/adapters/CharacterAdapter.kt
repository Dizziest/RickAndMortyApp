package com.example.rickandmortycharacters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.models.Character
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(onCharacterListener: OnCharacterListener) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {


    private val characters by lazy { mutableListOf<Character>()}
    private val onCharacterListener = onCharacterListener

    fun setCharacters(characters: List<Character>){
        if (characters.isNotEmpty()){
            this.characters.clear()
        }
        this.characters.addAll(characters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_character, parent, false)

        return CharacterViewHolder(itemView, onCharacterListener)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int){
        val character = characters[position]
        holder.bind(character)
    }

    fun getSelectedCharacter(position: Int): Character?{
        if (characters != null && characters.isNotEmpty()){
            return characters[position]
        }
        return null
    }

    class CharacterViewHolder (itemView: View, onCharacterListener: OnCharacterListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val onCharacterListener = onCharacterListener

        fun bind(character: Character){
            with(itemView){
               Glide.with(this)
                   .load(character.image)
                   .into(image_view_avatar)

                text_name.text = character.name
            }
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onCharacterListener.onCharacterClick(adapterPosition)
        }

    }
}