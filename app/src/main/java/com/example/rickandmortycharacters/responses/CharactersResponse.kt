package com.example.rickandmortycharacters.responses

import com.example.rickandmortycharacters.models.Character
import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    @SerializedName("results")
    val results: List<Character>
)