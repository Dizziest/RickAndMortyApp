package com.example.rickandmortycharacters.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycharacters.viewmodels.LocationsViewModel
import com.example.rickandmortycharacters.R
import com.example.rickandmortycharacters.adapters.EpisodeAdapter
import com.example.rickandmortycharacters.adapters.LocationAdapter
import com.example.rickandmortycharacters.models.Location
import com.example.rickandmortycharacters.viewmodels.EpisodesViewModel
import kotlinx.android.synthetic.main.episodes_fragment.*
import kotlinx.android.synthetic.main.locations_fragment.*
import kotlinx.android.synthetic.main.locations_fragment.recycler_view
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LocationsFragment : Fragment() {

    private val adapter: LocationAdapter by inject()
    private val layoutManager: LinearLayoutManager by inject()
    private val viewModel: LocationsViewModel by viewModel()

    companion object {
        fun newInstance() =
            LocationsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.locations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        observeLocations()
        observeError()
        viewModel.getLocations(1)
    }

    private fun initRecyclerView() {
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

    private fun observeLocations(){
        viewModel.getLocationsLiveData().observe(this){
            showLocations(it)
        }
    }

    private fun observeError(){
        viewModel.getErrorLiveData().observe(this){ throwable->
            throwable.message?.let { showErrorMessage(it) }
        }
    }

    private fun showErrorMessage(message: String){
        Toast.makeText(
            activity,
            "Error : $message",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLocations(locations: List<Location>){
        adapter.setLocations(locations)
    }

}
