package com.asthra.data.api.service


import com.asthra.data.api.request.CustomerHistoryRequest
import com.asthra.data.api.response.CustomerHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerHistoryApiService {


    @POST("api/customer_history")
    suspend fun customerHistory(
        @Body customerHistoryRequest: CustomerHistoryRequest
    ): Response<CustomerHistoryResponse>

}