package com.example.rickandmortycharacters.di

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycharacters.adapters.CharacterAdapter
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import com.example.rickandmortycharacters.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { RickAndMortyRepository(get()) }

    factory { CharacterAdapter() }
    factory<RecyclerView.LayoutManager> { GridLayoutManager(androidContext(), 2) }

    viewModel { MainViewModel(get()) }
}