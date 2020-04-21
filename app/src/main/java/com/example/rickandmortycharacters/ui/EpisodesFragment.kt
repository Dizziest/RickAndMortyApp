package com.example.rickandmortycharacters.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.rickandmortycharacters.viewmodels.EpisodesViewModel
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.adapters.EpisodeAdapter
import com.example.rickandmortycharacters.models.Episode
import kotlinx.android.synthetic.main.episodes_fragment.recycler_view
import kotlinx.android.synthetic.main.item_episode.*
import kotlinx.android.synthetic.main.locations_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class EpisodesFragment : Fragment(){

    private val adapter: EpisodeAdapter by inject()
    private val layoutManager: LinearLayoutManager by inject()
    private val viewModel: EpisodesViewModel by viewModel()

    companion object {
        fun newInstance() =
            EpisodesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.episodes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        observeEpisodes()
        observeError()
        viewModel.getEpisodes(1)

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

    private fun initRecyclerView(){
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)){
                    viewModel.searchNextPage()
                }
            }
        })
    }

    private fun showErrorMessage(message: String){
        Toast.makeText(
            activity,
            "Error : $message",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showEpisodes(episodes: List<Episode>){
        adapter.setEpisodes(episodes)
    }

}
