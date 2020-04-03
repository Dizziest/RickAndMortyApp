package com.example.rickandmortycharacters.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortycharacters.GetCharactersUseCase
import com.example.rickandmortycharacters.models.Character

class MainViewModel(private val getCharactersUseCase: GetCharactersUseCase) : ViewModel() {

    val characters = MutableLiveData<List<Character>>()
    val error = MutableLiveData<Throwable>()

    fun getCharacters() {
        getCharactersUseCase(viewModelScope) { result ->
            result.onSuccess { characters.value = it }
            result.onFailure { error.value = it }
        }
    }
}