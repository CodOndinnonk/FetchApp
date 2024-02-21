package com.ondinnonk.fetchapp.repositiry

import retrofit2.Response
import retrofit2.http.GET

interface ServerApi {
    @GET("hiring.json")
    suspend fun getItemsList(): Response<List<FetchItemModel>>
}