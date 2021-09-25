package com.asthra.data.api.service

import com.asthra.data.api.request.SearchByDateRequest
import com.asthra.data.api.response.SearchByDateResponse
import com.asthra.data.api.response.SplashResponse
import com.asthra.di.utility.ApiUrls.GET_CORE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SearchByDateApiService {

//    @GET(GET_CORE)
//    suspend fun getCoreData(): Response<SplashResponse>
//
//    @GET("api")
//    suspend fun callGetCore(
//        /*//@Query("type") type: String = "getcoreconfig"*/
//    ): Response<SplashResponse>

    @POST("/api/search_by_date")
    suspend fun searchByDate(
        @Body registerRequest: SearchByDateRequest
    ): Response<SearchByDateResponse>

}