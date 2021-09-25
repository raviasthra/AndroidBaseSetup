package com.asthra.data.api.service

import com.asthra.data.api.request.SplashRequest
import com.asthra.data.api.response.SplashResponse
import com.asthra.di.utility.ApiUrls.GET_CORE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SplashApiService {

    @GET(GET_CORE)
    suspend fun getCoreData(): Response<SplashResponse>

    @POST("api/check")
    suspend fun callTestAPI(
        @Body onSplashRequest: SplashRequest
    ): Response<SplashResponse>

//    @POST("api/users/add")
//    suspend fun register(
//        @Body registerRequest: RegisterRequest
//    ): Response<RegisterResponse>

}