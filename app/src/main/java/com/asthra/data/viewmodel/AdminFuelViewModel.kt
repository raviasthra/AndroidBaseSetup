package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.response.CustomerSaveDetailsResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.IAdminFuelRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class AdminFuelViewModel(
    private val iAdminRepository: IAdminFuelRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallSaveDetails(
        onCustomerSaveDetailsRequest: CustomerSaveDetailsRequest,
        onSuccess: OnSuccess<CustomerSaveDetailsResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iAdminRepository.onSaveDetails(onCustomerSaveDetailsRequest, onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}