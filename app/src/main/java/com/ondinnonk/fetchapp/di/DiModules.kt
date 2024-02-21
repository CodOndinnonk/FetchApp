package com.ondinnonk.fetchapp.di

import com.ondinnonk.fetchapp.MainViewModel
import com.ondinnonk.fetchapp.Repository
import com.ondinnonk.fetchapp.repositiry.ServerRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val repositoryModule = module {
    single { ServerRepository(get()) }
    factory {
        val server: ServerRepository = get()
        return@factory server.create()
    }
    factory { Repository(get()) }
}
