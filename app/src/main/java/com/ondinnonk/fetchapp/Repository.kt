package com.ondinnonk.fetchapp

import com.ondinnonk.fetchapp.repositiry.FetchItemModel
import com.ondinnonk.fetchapp.repositiry.RepositiryI
import com.ondinnonk.fetchapp.repositiry.ServerApi
import com.ondinnonk.fetchapp.utils.Coroutineble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

class Repository(private val serverApi: ServerApi) : RepositiryI, Coroutineble {

    private val itemsFlowEmit: MutableSharedFlow<List<FetchItemModel>> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val itemsFlow: Flow<List<FetchItemModel>> = itemsFlowEmit

    override suspend fun getRecords(): Result<List<FetchItemModel>> {
        //we can create domain model and map to it here
        return kotlin.runCatching {
            val result = withContext(Dispatchers.IO) {
                serverApi.getItemsList()
            }

            return if (result.isSuccessful) {
                result.body()?.let { body ->
                    Result.success(body)
                } ?: kotlin.run {
                    val details =
                        "Failed to get items list. Request success but body is invalid. ${result.message()}; ${result.errorBody()}"
                    Result.failure(Exception(details))
                }
            } else {
                val details =
                    "Failed to get items list. Request failed. Code = ${result.code()}; Msg = ${result.message()}; ${result.errorBody()}"
                Result.failure(Exception(details))
            }
        }
    }

    /**
     * Fetch data from server and push it to [itemsFlow]
     */
    suspend fun reloadList(): Result<Unit> {
        getRecords()
            .onSuccess {
                itemsFlowEmit.tryEmit(it)
                return Result.success(Unit)
            }
            .onFailure { return Result.failure(it) }
        throw Exception("Never reach point")
    }

}