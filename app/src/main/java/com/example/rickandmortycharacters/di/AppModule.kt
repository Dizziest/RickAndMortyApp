package com.example.rickandmortycharacters.di

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycharacters.adapters.CharacterAdapter
import com.example.rickandmortycharacters.adapters.EpisodeAdapter
import com.example.rickandmortycharacters.adapters.LocationAdapter
import com.example.rickandmortycharacters.adapters.OnCharacterListener
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import com.example.rickandmortycharacters.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { RickAndMortyRepository(get()) }


    factory { EpisodeAdapter() }
    factory { LocationAdapter() }
    factory<RecyclerView.LayoutManager> { GridLayoutManager(androidContext(), 2) }
    factory { LinearLayoutManager(androidContext()) }

    viewModel { MainViewModel() }
    viewModel { CharacterListViewModel(get()) }
    viewModel { EpisodesViewModel(get()) }
    viewModel { LocationsViewModel(get()) }
    viewModel { CharacterViewModel(get()) }
}