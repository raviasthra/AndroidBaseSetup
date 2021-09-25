package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.response.CustomerSaveDetailsResponse
import com.asthra.data.api.response.FuelManageResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.IEmployeeRepository
import com.asthra.di.toast
import com.asthra.di.unBlockUI
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import com.asthra.ui.admin.redeem.RedeemListAdapter
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_redeem_history.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EmployeeViewModel(
    private val iEmployeeRepository: IEmployeeRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallEmploySaveApi(
        onCustomerSaveDetailsRequest: CustomerSaveDetailsRequest,
        onSuccess: OnSuccess<CustomerSaveDetailsResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iEmployeeRepository.onCallSave(onCustomerSaveDetailsRequest, onSuccess, onError)
        }
    }

    fun onCallFuelSetting(
        onSuccess: OnSuccess<FuelManageResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iEmployeeRepository.onCallFuelManageDetails(onSuccess, onError)
        }
    }
    fun onCallRedeemList(
        onRedeemHistoryRequest: RedeemListRequest,
        onSuccess: OnSuccess<RedeemListResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iEmployeeRepository.onRedeemList(
                onRedeemHistoryRequest,
                onSuccess,
                onError
            )
        }
    }

    fun callCustomerDetails() {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}