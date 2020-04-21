package com.example.rickandmortycharacters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortycharacters.models.Episode
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodesViewModel(private val repository: RickAndMortyRepository) : ViewModel() {

    private val mEpisodes = MutableLiveData<List<Episode>>()
    private val mError = MutableLiveData<Throwable>()
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    var pageNumber = 1

    fun getEpisodes(page: Int){
        pageNumber = page
        mIsQueryExhausted.value = false
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching{ repository.getEpisodes(page) }
            }
            result.onSuccess { mEpisodes.value = it }
            result.onFailure { mError.value = it }
        }

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

    fun getEpisodesLiveData(): LiveData<List<Episode>>{
        return mEpisodes
    }

    fun getErrorLiveData(): LiveData<Throwable>{
        return mError
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
