package com.example.rickandmortycharacters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.models.DetailedCharacter
import com.example.rickandmortycharacters.models.Episode
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterViewModel(private val repository: RickAndMortyRepository) : ViewModel() {
    private val mEpisodes = MutableLiveData<List<Episode>>()
    private val mCharacter = MutableLiveData<DetailedCharacter>()
    private val mEpisodesIds = MutableLiveData<List<String>>()
    private val mError = MutableLiveData<Throwable>()
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    var pageNumber = 1

    fun getCharacterById(id: Int){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching { repository.getCharacterById(id) }
            }
            result.onSuccess { mCharacter.value = it }
            result.onFailure { mError.value = it }
            mCharacter.value?.episodes?.let { trimEpisodeIdsFromUrl(it) }
        }
    }

    fun getMultipleEpisodesByIds(ids: List<String>){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching { repository.getMultipleEpisodesByIds(ids) }
            }
            result.onSuccess { mEpisodes.value = it }
            result.onFailure { mError.value = it }
        }
    }

    private fun trimEpisodeIdsFromUrl(urls: List<String>){
        val list = mutableListOf<String>()
        if (urls != null){
            urls.forEach{
                list.add(it.substring(40))
            }
            println(list)
        }
        mEpisodesIds.value = list
    }

    fun searchNextPage(){
        if (!isQueryExhausted().value!!){
            var currentEpisodes: MutableList<Episode>? = mEpisodes.value?.toMutableList()
            viewModelScope.launch {
                val result1 = withContext(Dispatchers.IO) {
                    runCatching { repository.getEpisodes(pageNumber + 1) }
                }
                result1.onSuccess { mEpisodes.value = it }
                result1.onFailure { mError.value = it }

                checkLastQuery(mEpisodes.value?.toList())
                mEpisodes.value?.let { currentEpisodes?.addAll(it) }
                mEpisodes.postValue(currentEpisodes)
                pageNumber++
            }
        }
    }

    fun getEpisodesIdsLiveData(): LiveData<List<String>>{
        return mEpisodesIds
    }

    fun getEpisodesLiveData(): LiveData<List<Episode>> {
        return mEpisodes
    }

    fun getErrorLiveData(): LiveData<Throwable> {
        return mError
    }

    fun getCharacterLiveData(): LiveData<DetailedCharacter>{
        return mCharacter
    }

    private fun isQueryExhausted(): LiveData<Boolean> {
        return mIsQueryExhausted
    }

    private fun checkLastQuery(episodes: List<Episode>?){
        if (episodes != null){
            if (episodes.size % 20 != 0){
                mIsQueryExhausted.value = true
            }
        } else {
            mIsQueryExhausted.value = true
        }
    }
}