package com.example.rickandmortycharacters.repositories

import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.models.DetailedCharacter
import com.example.rickandmortycharacters.models.Episode
import com.example.rickandmortycharacters.models.Location
import com.example.rickandmortycharacters.network.RickAndMortyAPI

class RickAndMortyRepository(private val api: RickAndMortyAPI) {

    suspend fun getCharacters(page: Int, name: String?): List<Character> {
        return api.getCharacters(page.toString(), name).results
    }

    suspend fun getCharacterById(id: Int): DetailedCharacter {
        return api.getCharacterById(id)
    }

    suspend fun getEpisodes(page: Int): List<Episode>{
        return api.getEpisodes(page.toString()).results
    }

    suspend fun getMultipleEpisodesByIds(ids: List<String>): List<Episode>{
        return api.getMultipleEpisodesByIds(ids)
    }

    suspend fun getLocations(page: Int): List<Location>{
        return api.getLocations(page.toString()).results
    }
}