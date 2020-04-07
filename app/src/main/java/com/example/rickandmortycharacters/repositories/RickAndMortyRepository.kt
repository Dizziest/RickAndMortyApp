package com.example.rickandmortycharacters.repositories

import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.network.RickAndMortyAPI

class RickAndMortyRepository(private val api: RickAndMortyAPI) {

    suspend fun getCharacters(page: Int, name: String?): List<Character> {
        return api.getCharacters(page.toString(), name).results
    }

}