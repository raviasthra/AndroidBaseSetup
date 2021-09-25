package com.asthra.di

import android.content.Context
import android.content.SharedPreferences
import com.asthra.data.api.http.HttpClientManager
import com.asthra.data.api.http.createApi
import com.asthra.data.api.service.*
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.repository.*
import com.asthra.data.viewmodel.*
import com.asthra.di.utility.Pref.SHARED_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val NETWORKING_MODULE = module {
    single { HttpClientManager.newInstance(getPreference(androidContext())) }
    single { get<HttpClientManager>().createApi<SplashApiService>() }
    single { get<HttpClientManager>().createApi<LoginApiService>() }
    single { get<HttpClientManager>().createApi<CustomerSaveDetailsService>() }
    single { get<HttpClientManager>().createApi<DashboardDetailsApiService>() }
    single { get<HttpClientManager>().createApi<AdminRedeemHistoryApiService>() }
}

val REPOSITORY_MODULE = module {
    single { ISplashRepository.getInstance(get()) }
    single { ILoginRepository.getInstance(get()) }
    single { IDashboardRepository.getInstance(get()) }
    single { IEmployeeRepository.getInstance(get()) }
    single { ICustomerHistoryRepository.getInstance(get()) }
    single { IClimeRepository.getInstance(get()) }
    single { ISearchByDateRepository.getInstance(get()) }
    single { IAdminRedeemHistoryRepository.getInstance(get()) }
}

val VIEW_MODEL_MODULE = module {
    viewModel { SplashViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel { LoginViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel { DashboardViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel { EmployeeViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel {
        CustomerHistoryViewModel(
            get(),
            IPreferenceManager.getPrefInstance(androidContext())
        )
    }
    viewModel { ClimeViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel { SearchByDateViewModel(get(), IPreferenceManager.getPrefInstance(androidContext())) }
    viewModel {
        AdminRedeemHistoryViewModel(
            get(),
            IPreferenceManager.getPrefInstance(androidContext())
        )
    }
}

val DB_MODULE = module {
    // single { CommunityDatabase.create(androidContext()) }
}

fun getPreference(androidContext: Context): SharedPreferences {
    return androidContext.getSharedPreferences(
        SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )
}