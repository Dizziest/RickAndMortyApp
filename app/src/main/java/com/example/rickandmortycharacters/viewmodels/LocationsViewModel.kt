package com.example.rickandmortycharacters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortycharacters.models.Location
import com.example.rickandmortycharacters.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationsViewModel(private val repository: RickAndMortyRepository) : ViewModel() {

    private val mLocations = MutableLiveData<List<Location>>()
    private val mError = MutableLiveData<Throwable>()
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    private var pageNumber = 1

    fun getLocations(page: Int){
        pageNumber = page
        mIsQueryExhausted.value = false
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                runCatching { repository.getLocations(page) }
            }
            result.onSuccess { mLocations.value = it }
            result.onFailure { mError.value = it }
        }
    }


    fun searchNextPage(){
        if (!isQueryExhausted().value!!){
            var currentLocations: MutableList<Location>? = mLocations.value?.toMutableList()
            viewModelScope.launch {
                val result1 = withContext(Dispatchers.IO) {
                    runCatching { repository.getLocations(pageNumber + 1) }
                }
                result1.onSuccess { mLocations.value = it }
                result1.onFailure { mError.value = it }

                checkLastQuery(mLocations.value?.toList())
                mLocations.value?.let { currentLocations?.addAll(it) }
                mLocations.postValue(currentLocations)
                pageNumber++
            }
        }
    }

    fun getLocationsLiveData() : LiveData<List<Location>>{
        return mLocations
    }

    fun getErrorLiveData() : LiveData<Throwable>{
        return mError
    }

    private fun isQueryExhausted() : LiveData<Boolean>{
        return mIsQueryExhausted
    }

    private fun checkLastQuery(locations: List<Location>?){
        if (locations != null){
            if (locations.size % 20 != 0){
                mIsQueryExhausted.value = true
            }
        } else {
            mIsQueryExhausted.value = true
        }
    }
}
