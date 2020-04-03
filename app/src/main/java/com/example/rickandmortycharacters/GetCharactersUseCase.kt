package com.example.rickandmortycharacters

import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetCharactersUseCase(private val repository: RickAndMortyRepository) {

    operator fun invoke(
        coroutineScope: CoroutineScope,
        onResult: (Result<List<Character>>) -> Unit
    ){
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO){
                kotlin.runCatching { repository.getCharacters() }
            }

            onResult(result)
        }
    }

}