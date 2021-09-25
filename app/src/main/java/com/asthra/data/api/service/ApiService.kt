package com.asthra.data.api.service

import com.asthra.data.api.response.SplashResponse
import com.asthra.di.utility.ApiUrls
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiUrls.GET_CORE)
    suspend fun getCoreData(): Response<SplashResponse>

}