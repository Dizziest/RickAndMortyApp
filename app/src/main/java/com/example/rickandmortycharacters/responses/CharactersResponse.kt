package com.example.rickandmortycharacters.responses

import com.example.rickandmortycharacters.models.Character

data class CharactersResponse(
    val results: List<Character>
)