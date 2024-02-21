package com.ondinnonk.fetchapp.repositiry

import com.google.gson.annotations.SerializedName

data class FetchItemModel(
    @field:SerializedName("id")
    val id: Long,
    @field:SerializedName("listId")
    val listId: Long,
    @field:SerializedName("name")
    val name: String?
)