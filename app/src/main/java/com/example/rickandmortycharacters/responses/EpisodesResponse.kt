package com.example.rickandmortycharacters.responses

import com.example.rickandmortycharacters.models.Episode
import com.google.gson.annotations.SerializedName

data class EpisodesResponse(
    @SerializedName("results")
    val results: List<Episode>
)