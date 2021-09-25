package com.asthra.data.api.service

import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.api.request.SearchByDateRequest
import com.asthra.data.api.response.SaveSettingResponse
import com.asthra.data.api.response.SearchByDateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SaveSettingApiService {

    @POST("/api/search_by_date")
    suspend fun saveSettings(
        @Body saveSettingsRequest: SaveSettingsRequest
    ): Response<SaveSettingResponse>

}