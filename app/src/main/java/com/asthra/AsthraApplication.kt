package com.asthra

import android.app.Application
import com.facebook.stetho.Stetho
import com.asthra.di.NETWORKING_MODULE
import com.asthra.di.REPOSITORY_MODULE
import com.asthra.di.VIEW_MODEL_MODULE
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AsthraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AsthraApplication)
            loadKoinModules(REQUIRED__MODULE)
        }
    }

    companion object {
        val REQUIRED__MODULE = listOf(
            NETWORKING_MODULE,
            VIEW_MODEL_MODULE,
            REPOSITORY_MODULE
        )
    }
}