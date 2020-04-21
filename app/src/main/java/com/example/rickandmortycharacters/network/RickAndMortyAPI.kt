package com.example.rickandmortycharacters.network

import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.models.DetailedCharacter
import com.example.rickandmortycharacters.models.Episode
import com.example.rickandmortycharacters.responses.CharactersResponse
import com.example.rickandmortycharacters.responses.EpisodesResponse
import com.example.rickandmortycharacters.responses.LocationsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: String,
        @Query("name") name: String?
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): DetailedCharacter

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: String
    ): EpisodesResponse

    @GET("location/")
    suspend fun getLocations(
        @Query("page") page: String
    ): LocationsResponse

    @GET("episode/{id}")
    suspend fun getMultipleEpisodesByIds(
        @Path("id") id: List<String>
    ): List<Episode>
}