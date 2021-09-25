package com.asthra.data.repository

import com.asthra.data.api.request.LoginRequest
import com.asthra.data.api.response.LoginResponse
import com.asthra.data.api.response.SplashResponse
import com.asthra.data.api.service.LoginApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginRepository(private val api: LoginApiService) : ILoginRepository {
    override suspend fun onCallLogin(
        onLoginRequest: LoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.onLoginRequest(onLoginRequest)
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

interface ILoginRepository {

    suspend fun onCallLogin(
        onCallLoginRequest: LoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: LoginApiService): ILoginRepository =
            LoginRepository(api)
    }
}