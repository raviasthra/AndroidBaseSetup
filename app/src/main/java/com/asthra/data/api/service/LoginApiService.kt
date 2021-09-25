package com.asthra.data.api.service

import com.asthra.data.api.request.LoginRequest
import com.asthra.data.api.request.SplashRequest
import com.asthra.data.api.response.LoginResponse
import com.asthra.data.api.response.SplashResponse
import com.asthra.di.utility.ApiUrls.GET_CORE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApiService {

    @GET(GET_CORE)
    suspend fun getCoreData(): Response<SplashResponse>

    @POST("api/login")
    suspend fun onLoginRequest(
        @Body request: LoginRequest
    ): Response<LoginResponse>

//    @POST("api/users/add")
//    suspend fun register(
//        @Body registerRequest: RegisterRequest
//    ): Response<RegisterResponse>

}