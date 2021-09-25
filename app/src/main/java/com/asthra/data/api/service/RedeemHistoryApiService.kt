package com.asthra.data.api.service

import com.asthra.data.api.request.ClimeRequest
import com.asthra.data.api.request.SearchByDateRequest
import com.asthra.data.api.response.SearchByDateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RedeemHistoryApiService {

    @POST("/api/search_by_date")
    suspend fun searchByDate(
        @Body registerRequest: SearchByDateRequest
    ): Response<SearchByDateResponse>

}