package com.asthra.data.repository

import com.asthra.data.api.request.SplashRequest
import com.asthra.data.api.response.SplashResponse
import com.asthra.data.api.service.SplashApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SplashRepository(private val api: SplashApiService) : ISplashRepository {
    override suspend fun onCallGetCore(
        onRequest: SplashRequest,
        onSuccess: OnSuccess<SplashResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.callTestAPI(onRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun onClear() {

    }
}

interface ISplashRepository {

    suspend fun onCallGetCore(
        onRequest: SplashRequest,
        onSuccess: OnSuccess<SplashResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: SplashApiService): ISplashRepository =
            SplashRepository(api)
    }
}
