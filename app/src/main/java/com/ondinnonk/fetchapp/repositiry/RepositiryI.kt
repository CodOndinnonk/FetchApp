package com.ondinnonk.fetchapp.repositiry

interface RepositiryI {
    suspend fun getRecords(): Result<List<FetchItemModel>>
}
