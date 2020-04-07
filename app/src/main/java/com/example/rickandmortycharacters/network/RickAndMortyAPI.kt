package com.example.rickandmortycharacters.network

import com.example.rickandmortycharacters.responses.CharactersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: String,
        @Query("name") name: String?
    ): CharactersResponse

}