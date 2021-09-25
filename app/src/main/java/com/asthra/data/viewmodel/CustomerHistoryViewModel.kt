package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.CustomerHistoryRequest
import com.asthra.data.api.response.CustomerHistoryResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.ICustomerHistoryRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CustomerHistoryViewModel(
    private val customerHistoryViewModel: ICustomerHistoryRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallCustomerApi(
        onCustomerHistoryRequest: CustomerHistoryRequest,
        onSuccess: OnSuccess<CustomerHistoryResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            customerHistoryViewModel.onCallCustomerHistory(
                onCustomerHistoryRequest,
                onSuccess,
                onError
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}