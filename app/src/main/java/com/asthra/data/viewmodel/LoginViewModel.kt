package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.LoginRequest
import com.asthra.data.api.response.LoginResponse
import com.asthra.data.api.response.SplashResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.ILoginRepository
import com.asthra.data.repository.ISplashRepository
import com.asthra.data.repository.LoginRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel (
    private val loginRepository: ILoginRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallLoginApi(onLoginRequest: LoginRequest, onSuccess: OnSuccess<LoginResponse>, onError: OnError<String>) {
        viewModelScope.launch {
            loginRepository.onCallLogin(onLoginRequest, onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}