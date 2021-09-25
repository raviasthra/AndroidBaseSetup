package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.ClimeRequest
import com.asthra.data.api.response.ClimeResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.IClimeRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ClimeViewModel(
    private val iClimeRepository: IClimeRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallClimeDetails(
        onClimeRequest: ClimeRequest,
        onSuccess: OnSuccess<ClimeResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iClimeRepository.onClimeData(onClimeRequest, onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}