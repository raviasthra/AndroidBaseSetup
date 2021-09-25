package com.asthra.data.repository

import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.response.CustomerSaveDetailsResponse
import com.asthra.data.api.response.FuelManageResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.api.service.CustomerSaveDetailsService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class EmployeeRepository(private val api: CustomerSaveDetailsService) : IEmployeeRepository {
    override suspend fun onCallSave(
        onCustomerSaveDetailsRequest: CustomerSaveDetailsRequest,
        onSuccess: OnSuccess<CustomerSaveDetailsResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.customerSaveDetails(onCustomerSaveDetailsRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun onCallFuelManageDetails(
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

    override suspend fun onRedeemList(
        onRedeemHistoryRequest: RedeemListRequest,
        onSuccess: OnSuccess<RedeemListResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.redeemList(onRedeemHistoryRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("" + e.printStackTrace()) }
            }
        }
    }

    override suspend fun onClear() {

    }
}

interface IEmployeeRepository {

    suspend fun onCallSave(
        onCustomerSaveDetailsRequest: CustomerSaveDetailsRequest,
        onSuccess: OnSuccess<CustomerSaveDetailsResponse>,
        onError: OnError<String>
    )

    suspend fun onCallFuelManageDetails(
        onSuccess: OnSuccess<FuelManageResponse>,
        onError: OnError<String>
    )

    suspend fun onRedeemList(
        onRedeemHistoryRequest: RedeemListRequest,
        onSuccess: OnSuccess<RedeemListResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: CustomerSaveDetailsService): IEmployeeRepository =
            EmployeeRepository(api)
    }
}