package com.asthra.data.api.service

import com.asthra.data.api.request.ClimeRequest
import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.api.response.ClimeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdminFuelApiService {

    @POST("api/users/add")
    suspend fun saveCustomer(
        @Body registerRequest: ClimeRequest
    ): Response<CustomerSaveDetailsRequest>

}