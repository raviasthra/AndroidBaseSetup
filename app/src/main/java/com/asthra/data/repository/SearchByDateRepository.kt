package com.asthra.data.repository

import com.asthra.data.api.request.SearchByDateRequest
import com.asthra.data.api.response.SearchByDateResponse
import com.asthra.data.api.service.LoginApiService
import com.asthra.data.api.service.SearchByDateApiService
import com.asthra.di.utility.OnError
import com.asthra.di.utility.OnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchByDateRepository(private val api: SearchByDateApiService) : ISearchByDateRepository {
    override suspend fun onCallSearch(
        onSearchByDateRequest: SearchByDateRequest,
        onSuccess: OnSuccess<SearchByDateResponse>,
        onError: OnError<String>
    ) {

        withContext(Dispatchers.IO) {
            try {
                val response = api.searchByDate(onSearchByDateRequest)
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

interface ISearchByDateRepository {

    suspend fun onCallSearch(
        onSearchByDateRequest: SearchByDateRequest,
        onSuccess: OnSuccess<SearchByDateResponse>,
        onError: OnError<String>
    )

    suspend fun onClear()

    companion object Factory {
        fun getInstance(api: SearchByDateApiService): ISearchByDateRepository =
            SearchByDateRepository(api)
    }
}