package com.ondinnonk.fetchapp

import android.app.Application
import com.ondinnonk.fetchapp.di.repositoryModule
import com.ondinnonk.fetchapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FetchApplication : Application() {

    companion object {
        const val LOG_TAG = "FetchApp"
    }

    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@FetchApplication)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                )
            )
        }
    }

}