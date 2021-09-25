package com.asthra.data.api.service

import com.asthra.data.api.request.*
import com.asthra.data.api.response.RedeemHistoryResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.api.response.RedeemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdminRedeemHistoryApiService {

    @POST("api/get-redeemed-history")
    suspend fun redeemHistory(
        @Body registerRequest: RedeemHistoryRequest
    ): Response<RedeemHistoryResponse>


    @POST("api/get-redeem-list")
    suspend fun redeemList(
        @Body registerRequest: RedeemListRequest
    ): Response<RedeemListResponse>


    @POST("api/redeem")
    suspend fun redeemValue(
        @Body registerRequest: RedeemRequest
    ): Response<RedeemResponse>

}