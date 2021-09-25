package com.asthra.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.RedeemHistoryRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.request.RedeemResponse
import com.asthra.data.api.response.RedeemHistoryResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.api.response.RedeemRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.IAdminRedeemHistoryRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AdminRedeemHistoryViewModel(
    private val iAdminRedeemHistoryRepository: IAdminRedeemHistoryRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    var myLoading = MutableLiveData<Boolean>(false)
    var currentPage = MutableLiveData<Int>(1)
    var totalRecord = MutableLiveData<Int>(0)

    fun onCallRedeemList(
        onRedeemHistoryRequest: RedeemListRequest,
        onSuccess: OnSuccess<RedeemListResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iAdminRedeemHistoryRepository.onRedeemList(
                onRedeemHistoryRequest,
                onSuccess,
                onError
            )
        }
    }

    fun onCallRedeemHistory(
        onRedeemHistoryRequest: RedeemHistoryRequest,
        onSuccess: OnSuccess<RedeemHistoryResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iAdminRedeemHistoryRepository.onRedeemHistory(
                onRedeemHistoryRequest,
                onSuccess,
                onError
            )
        }
    }

    fun onCallRedeem(
        onRedeemHistoryRequest: RedeemRequest,
        onSuccess: OnSuccess<RedeemResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iAdminRedeemHistoryRepository.onRedeem(
                onRedeemHistoryRequest,
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