package com.asthra.data.api.service

import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.response.CustomerSaveDetailsResponse
import com.asthra.data.api.response.FuelManageResponse
import com.asthra.data.api.response.RedeemListResponse
import com.asthra.data.api.response.SplashResponse
import com.asthra.di.utility.ApiUrls
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CustomerSaveDetailsService {

    @POST("api/insert-data")
    suspend fun customerSaveDetails(
        @Body customerSaveDetailsRequest: CustomerSaveDetailsRequest
    ): Response<CustomerSaveDetailsResponse>

    @POST("api/get-fuel-types")
    suspend fun getFuelManageDetails(): Response<FuelManageResponse>

    @POST("api/get-redeem-list")
    suspend fun redeemList(
        @Body registerRequest: RedeemListRequest
    ): Response<RedeemListResponse>
}