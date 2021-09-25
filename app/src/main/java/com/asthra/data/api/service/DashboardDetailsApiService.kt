package com.asthra.data.api.service

import com.asthra.data.api.request.ChangePasswordRequest
import com.asthra.data.api.request.CustomerListRequest
import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.api.response.ChangePasswordResponse
import com.asthra.data.api.response.CustomerListResponse
import com.asthra.data.api.response.FuelManageResponse
import com.asthra.data.api.response.SaveSettingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DashboardDetailsApiService {

    @POST("/api/get-history")
    suspend fun customerList(
        @Body dashboardRequest: CustomerListRequest
    ): Response<CustomerListResponse>

    @POST("api/get-fuel-types")
    suspend fun getFuelManageDetails(): Response<FuelManageResponse>

    @POST("api/update-fuel-status")
    suspend fun getSaveFuelDetails(
        @Body onsaveSettingsRequest: SaveSettingsRequest
    ): Response<SaveSettingResponse>

    @POST("api/change-password")
    suspend fun getSaveChangePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

}