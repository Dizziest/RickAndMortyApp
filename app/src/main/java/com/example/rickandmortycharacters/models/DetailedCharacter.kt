package com.example.rickandmortycharacters.models

import com.google.gson.annotations.SerializedName

data class DetailedCharacter(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String?,
    @SerializedName("species")
    val species: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("origin")
    val origin: Location?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("episode")
    val episodes: List<String>?
)