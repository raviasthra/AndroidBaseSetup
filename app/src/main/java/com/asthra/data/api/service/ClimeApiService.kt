package com.asthra.data.api.service

import com.asthra.data.api.request.ClimeRequest
import com.asthra.data.api.response.ClimeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClimeApiService {

    @POST("api/users/add")
    suspend fun climeResponse(
        @Body registerRequest: ClimeRequest
    ): Response<ClimeResponse>

}