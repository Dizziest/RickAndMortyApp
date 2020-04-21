package com.example.rickandmortycharacters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortycharacters.models.Character
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterListViewModel(private val repository: RickAndMortyRepository) : ViewModel() {

    private val mCharacters = MutableLiveData<List<Character>>()
    private val mError = MutableLiveData<Throwable>()
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    var pageNumber = 1
    var mName: String? = ""

    fun getCharacters(page: Int, name: String?) {
        mIsQueryExhausted.value = false
        pageNumber = page
        mName = name
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching { repository.getCharacters(page, name) }
            }
            result.onSuccess { mCharacters.value = it }
            result.onFailure { mError.value = it }
            checkLastQuery(mCharacters.value?.toList())
        }
    }

    fun getCharactersLiveData() : LiveData<List<Character>> {
        return mCharacters
    }

    fun getErrorLiveData(): LiveData<Throwable> {
        return mError
    }

    fun searchNextPage(){
        if (!isQueryExhausted().value!!){
            var currentCharacters: MutableList<Character>? = mCharacters.value?.toMutableList()
            viewModelScope.launch {
                val result1 = withContext(Dispatchers.IO) {
                    runCatching { repository.getCharacters(pageNumber + 1, mName) }
                }
                result1.onSuccess { mCharacters.value = it }
                result1.onFailure { mError.value = it }

                checkLastQuery(mCharacters.value?.toList())
                mCharacters.value?.let { currentCharacters?.addAll(it) }
                mCharacters.postValue(currentCharacters)
                pageNumber++
            }
        }
    }

    private fun isQueryExhausted(): LiveData<Boolean> {
        return mIsQueryExhausted
    }

    private fun checkLastQuery(characters: List<Character>?){
        if (characters != null){
            if (characters.size % 20 != 0){
                mIsQueryExhausted.value = true
            }
        } else {
            mIsQueryExhausted.value = true
        }
    }
}
