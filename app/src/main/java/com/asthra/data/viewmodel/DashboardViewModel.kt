package com.asthra.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.ChangePasswordRequest
import com.asthra.data.api.request.CustomerListRequest
import com.asthra.data.api.request.DashboardRequest
import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.api.response.*
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.IDashboardRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val iDashboardRepository: IDashboardRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    var myLoading = MutableLiveData<Boolean>(false)
    var currentPage = MutableLiveData<Int>(1)
    var totalRecord = MutableLiveData<Int>(0)

    fun onCallDashboardDetailsCustomerList(
        onDashboardRequest: CustomerListRequest,
        onSuccess: OnSuccess<CustomerListResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iDashboardRepository.onCustomerList(
                onDashboardRequest,
                onSuccess,
                onError
            )
        }
    }

    fun onCallDashboardSettingDetails(
        onSuccess: OnSuccess<FuelManageResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iDashboardRepository.fuelSetting(
                onSuccess,
                onError
            )
        }
    }


    fun onCallSaveSettingsDetails(
        onSaveSettingsRequest: SaveSettingsRequest,
        onSuccess: OnSuccess<SaveSettingResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iDashboardRepository.saveFuelSetting(
                onSaveSettingsRequest,
                onSuccess,
                onError
            )
        }
    }

    fun onCallChangePassword(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<String>
    ) {
        viewModelScope.launch {
            iDashboardRepository.saveChangePassword(
                changePasswordRequest,
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