package com.example.rickandmortycharacters.responses

import com.example.rickandmortycharacters.models.Location
import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @SerializedName("results")
    val results: List<Location>
)