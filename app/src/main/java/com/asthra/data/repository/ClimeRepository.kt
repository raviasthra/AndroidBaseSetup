package com.asthra.data.repository

import com.asthra.data.api.request.ClimeRequest
import com.asthra.data.api.response.ClimeResponse
import com.asthra.data.api.service.ClimeApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ClimeRepository(private val api: ClimeApiService) : IClimeRepository {
    override suspend fun onClimeData(
        onClimeRequest: ClimeRequest,
        onSuccess: OnSuccess<ClimeResponse>,
        onError: OnError<String>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.climeResponse(onClimeRequest)
                response.body().let {
                    withContext(Dispatchers.Main) { onSuccess(it!!) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("") }
            }
        }
    }

    override suspend fun onClear() {
        TODO("Not yet implemented")
    }
}

interface IClimeRepository {
    suspend fun onClimeData(
        onClimeRequest: ClimeRequest,
        onSuccess: OnSuccess<ClimeResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: ClimeApiService): IClimeRepository =
            ClimeRepository(api)
    }
}