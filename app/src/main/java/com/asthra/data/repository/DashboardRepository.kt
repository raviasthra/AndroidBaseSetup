package com.asthra.data.repository

import com.asthra.data.api.request.ChangePasswordRequest
import com.asthra.data.api.request.CustomerListRequest
import com.asthra.data.api.request.DashboardRequest
import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.api.response.*
import com.asthra.data.api.service.DashboardDetailsApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DashboardRepository(private val api: DashboardDetailsApiService) : IDashboardRepository {

    override suspend fun onCustomerList(
        dashboardRequest: CustomerListRequest,
        onSuccess: OnSuccess<CustomerListResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.customerList(dashboardRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun fuelSetting(
        onSuccess: OnSuccess<FuelManageResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.getFuelManageDetails()
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun saveFuelSetting(
        onSaveSettingsRequest: SaveSettingsRequest,
        onSuccess: OnSuccess<SaveSettingResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.getSaveFuelDetails(onSaveSettingsRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun saveChangePassword(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.getSaveChangePassword(changePasswordRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }


    override suspend fun onClear() {

    }
}

interface IDashboardRepository {
    suspend fun onCustomerList(
        dashboardRequest: CustomerListRequest,
        onSuccess: OnSuccess<CustomerListResponse>,
        onError: OnError<String>
    )

    suspend fun fuelSetting(
        onSuccess: OnSuccess<FuelManageResponse>,
        onError: OnError<String>
    )

    suspend fun saveFuelSetting(
        onSaveSettingsRequest: SaveSettingsRequest,
        onSuccess: OnSuccess<SaveSettingResponse>,
        onError: OnError<String>
    )

    suspend fun saveChangePassword(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: DashboardDetailsApiService): IDashboardRepository =
            DashboardRepository(api)
    }
}