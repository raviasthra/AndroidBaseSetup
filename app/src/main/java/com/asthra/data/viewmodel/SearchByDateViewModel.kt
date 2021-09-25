package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.SearchByDateRequest
import com.asthra.data.api.response.SearchByDateResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.ISearchByDateRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchByDateViewModel(
    private val iSearchByDateRepository: ISearchByDateRepository,
    private val pref: IPreferenceManager
) :
    ViewModel() {

    fun onCallSearchByDate(
        onSearchByDateRequest: SearchByDateRequest,
        onSuccess: OnSuccess<SearchByDateResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iSearchByDateRepository.onCallSearch(onSearchByDateRequest, onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}