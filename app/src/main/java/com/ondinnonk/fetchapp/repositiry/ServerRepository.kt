package com.ondinnonk.fetchapp.repositiry

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServerRepository(private val context: Context) {

    companion object {
        const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    }

    private var retrofit: Retrofit
    private var httpClient: OkHttpClient

    init {
        httpClient = buildOkHttpClient()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()
    }

    fun create(): ServerApi = retrofit.create(ServerApi::class.java)

    private fun buildOkHttpClient(): OkHttpClient {
        // cache for data(1MB)
        val cacheSize = 10 * 1024
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClient.readTimeout(30, TimeUnit.SECONDS)

        return okHttpClient.cache(cache).build()
    }

}