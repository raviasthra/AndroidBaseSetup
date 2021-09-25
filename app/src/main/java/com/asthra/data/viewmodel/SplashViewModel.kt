package com.asthra.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asthra.data.api.request.SplashRequest
import com.asthra.data.api.response.SplashResponse
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.ISplashRepository
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import com.google.gson.JsonObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject

class SplashViewModel(
    private val splashRepository: ISplashRepository,
    private val pref: IPreferenceManager
) : ViewModel() {

    fun onCallSplashApi(onSplashRequest: SplashRequest, onSuccess: OnSuccess<SplashResponse>, onError: OnError<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("", "")
        viewModelScope.launch {
            splashRepository.onCallGetCore(onSplashRequest, onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}