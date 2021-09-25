package com.asthra.data.repository

import com.asthra.data.api.request.CustomerHistoryRequest
import com.asthra.data.api.response.CustomerHistoryResponse
import com.asthra.data.api.service.CustomerHistoryApiService
import com.asthra.data.api.service.CustomerSaveDetailsService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CustomerHistoryRepository(private val api: CustomerHistoryApiService) :
    ICustomerHistoryRepository {
    override suspend fun onCallCustomerHistory(
        onRequest: CustomerHistoryRequest,
        onSuccess: OnSuccess<CustomerHistoryResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.customerHistory(onRequest)
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

interface ICustomerHistoryRepository {
    suspend fun onCallCustomerHistory(
        onRequest: CustomerHistoryRequest,
        onSuccess: OnSuccess<CustomerHistoryResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: CustomerHistoryApiService): ICustomerHistoryRepository =
            CustomerHistoryRepository(api)
    }
}