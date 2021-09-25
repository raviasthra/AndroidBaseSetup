package com.asthra.data.repository

import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.response.CustomerSaveDetailsResponse
import com.asthra.data.api.service.CustomerSaveDetailsService
import com.asthra.data.api.service.SaveSettingApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AdminFuelRepository(private val api: CustomerSaveDetailsService) : IAdminFuelRepository {

    override suspend fun onSaveDetails(
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

    override suspend fun onClear() {
        TODO("Not yet implemented")
    }

}


interface IAdminFuelRepository {

    suspend fun onSaveDetails(
        onCustomerSaveDetailsRequest: CustomerSaveDetailsRequest,
        onSuccess: OnSuccess<CustomerSaveDetailsResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: CustomerSaveDetailsService): IAdminFuelRepository =
            AdminFuelRepository(api)
    }
}