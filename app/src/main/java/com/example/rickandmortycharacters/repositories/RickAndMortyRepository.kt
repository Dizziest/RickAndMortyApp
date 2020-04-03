package com.example.rickandmortycharacters.repositories

import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.network.RickAndMortyAPI

class RickAndMortyRepository(private val api: RickAndMortyAPI) {

    suspend fun getCharacters(): List<Character> {
        return api.getCharacters().results
    }
}