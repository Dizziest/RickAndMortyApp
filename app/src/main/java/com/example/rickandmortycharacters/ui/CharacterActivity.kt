package com.example.rickandmortycharacters.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.adapters.EpisodeAdapter
import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.models.DetailedCharacter
import com.example.rickandmortycharacters.models.Episode
import com.example.rickandmortycharacters.viewmodels.CharacterViewModel
import com.example.rickandmortycharacters.viewmodels.EpisodesViewModel
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.episodes_fragment.*
import kotlinx.android.synthetic.main.episodes_fragment.recycler_view
import kotlinx.android.synthetic.main.item_episode.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class CharacterActivity : AppCompatActivity(), View.OnClickListener {

    private val adapter: EpisodeAdapter by inject()
    private val layoutManager: LinearLayoutManager by inject()
    private val viewModel: CharacterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        initRecyclerView()
        getIncomingIntent()
        subscribeObservers()
    }

    private fun observeCharacter(){
        viewModel.getCharacterLiveData().observe(this){
            showCharacter(it)
        }
    }

    private fun observeEpisodesIds(){
        viewModel.getEpisodesIdsLiveData().observe(this){
            viewModel.getMultipleEpisodesByIds(it)
        }
    }

    private fun observeEpisodes(){
        viewModel.getEpisodesLiveData().observe(this){
            showEpisodes(it)
        }
    }

    private fun observeError(){
        viewModel.getErrorLiveData().observe(this){ throwable ->
            throwable.message?.let { showErrorMessage(it) }
        }
    }

    private fun subscribeObservers(){
        observeCharacter()
        observeEpisodes()
        observeError()
        observeEpisodesIds()
    }

    private fun initRecyclerView(){
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        /*recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)){
                    viewModel.searchNextPage()
                }
            }
        })*/
    }

    private fun showErrorMessage(message: String){
        Toast.makeText(
            this,
            "Error : $message",
            Toast.LENGTH_SHORT
        ).show()

        Log.d("Error", message)
    }

    private fun showEpisodes(episodes: List<Episode>){
        adapter.setEpisodes(episodes)
    }

    private fun showCharacter(character: DetailedCharacter){
        Glide.with(this)
            .load(character.image)
            .into(characterImage)

        characterName.text = character.name
        characterStatus.text = "Status: " + character.status
        characterSpecies.text = "Species: " + character.species
        characterGender.text = "Gender: " + character.gender
        characterOrigin.text = "Origin: " + character.origin?.name

        arrow_button.setOnClickListener(this)
    }

    private fun getIncomingIntent(){
        val id = intent.extras?.getInt("id")
        if (id != null) {
            viewModel.getCharacterById(id)
        }
    }

    override fun onClick(v: View?) {
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
