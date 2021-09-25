package com.asthra.data.repository

import android.util.Log
import com.asthra.data.api.request.RedeemHistoryRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.request.RedeemResponse
import com.asthra.data.api.response.RedeemHistoryResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.api.response.RedeemRequest
import com.asthra.data.api.service.AdminRedeemHistoryApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AdminRedeemHistoryRepository(private val api: AdminRedeemHistoryApiService) :
    IAdminRedeemHistoryRepository {

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

    override suspend fun onRedeemHistory(
        onRedeemHistoryRequest: RedeemHistoryRequest,
        onSuccess: OnSuccess<RedeemHistoryResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.redeemHistory(onRedeemHistoryRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun onRedeem(
        onRedeemHistoryRequest: RedeemRequest,
        onSuccess: OnSuccess<RedeemResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.redeemValue(onRedeemHistoryRequest)
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


interface IAdminRedeemHistoryRepository {

    suspend fun onRedeemList(
        onRedeemHistoryRequest: RedeemListRequest,
        onSuccess: OnSuccess<RedeemListResponse>,
        onError: OnError<String>
    )

    suspend fun onRedeemHistory(
        onRedeemHistoryRequest: RedeemHistoryRequest,
        onSuccess: OnSuccess<RedeemHistoryResponse>,
        onError: OnError<String>
    )

    suspend fun onRedeem(
        onRedeemHistoryRequest: RedeemRequest,
        onSuccess: OnSuccess<RedeemResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: AdminRedeemHistoryApiService): IAdminRedeemHistoryRepository =
            AdminRedeemHistoryRepository(api)
    }
}