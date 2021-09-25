package com.asthra.data.api.http

import android.content.SharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.asthra.BuildConfig
import com.asthra.data.api.http.HttpClientManager.Companion.HEADER_KEY_AUTHORIZATION
import com.asthra.di.utility.Pref.TOKEN
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface HttpClientManager {

    var client: OkHttpClient
    var gsonConverterFactory: GsonConverterFactory
    var retrofit: Retrofit

    companion object {
        const val HEADER_KEY_ACCEPT = "Accept"
        const val HEADER_KEY_AUTHORIZATION = TOKEN
        const val HEADER_VALUE_TYPE_APPLICATION_JSON = "application/json"

        fun newInstance(sharedPreferences: SharedPreferences): HttpClientManager =
            HttpClientManagerImpl(sharedPreferences)
    }
}

inline fun <reified T> HttpClientManager.createApi(): T {
    return this.retrofit.create()
}

private class HttpClientManagerImpl(var sharedPreferences: SharedPreferences) : HttpClientManager {

    override var client: OkHttpClient = OkHttpClient.Builder()

        .addInterceptor {
            val accessToken = sharedPreferences.getString(TOKEN, "")!!
            if (accessToken == "") {
                it.proceed(
                    it.request().newBuilder().addHeader(
                        HEADER_KEY_AUTHORIZATION,
                        ""
                    ).build()
                )
            } else {
                it.proceed(
                    it.request().newBuilder().addHeader(
                        HEADER_KEY_AUTHORIZATION,
                        accessToken
                    ).build()
                )
            }
        }
        /*.addInterceptor {
            val url = it.request().url.toString()
            println(url)
            if (!url.contains(API.METHOD_INSTALLATION)) {
                val installationid =
                    sharedPreferences.getString(Preference.INSTALLATION_ID, "")!!
                it.proceed(
                    it.request().newBuilder().addHeader(
                        Preference.INSTALLATION_ID, installationid
                    ).build()
                )
            } else it.proceed(
                it.request().newBuilder().build()
            )
        }*/

        .apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(StethoInterceptor())
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                addInterceptor(httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .callTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    override var gsonConverterFactory: GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().create())


    override var retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(client)
        .build()
}
