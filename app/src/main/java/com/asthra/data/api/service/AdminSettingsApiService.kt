package com.asthra.data.api.service

import com.asthra.data.api.request.RedeemHistoryRequest
import com.asthra.data.api.response.RedeemHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdminSettingsApiService {

    @POST("api/users/add")
    suspend fun adminRedeemHistory(
        @Body registerRequest: RedeemHistoryRequest
    ): Response<RedeemHistoryResponse>

}