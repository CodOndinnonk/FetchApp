package com.ondinnonk.fetchapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ondinnonk.fetchapp.repositiry.FetchItemModel
import com.ondinnonk.fetchapp.utils.Coroutineble
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class MainViewModel(private val repository: Repository) : ViewModel(), KoinComponent, Coroutineble {

    private val mutableItemFlow: MutableStateFlow<List<FetchItemModel>> = MutableStateFlow<List<FetchItemModel>>(listOf())
    val itemFlow = mutableItemFlow.asStateFlow()

    init {
        launch {
            mutableItemFlow.emitAll(
                repository.itemsFlow
                    .prepareList()
            )
        }
        reloadList()
    }

    fun reloadList() {
        launch {
            repository.reloadList()
                .onFailure {
                    Log.e(FetchApplication.LOG_TAG, it.message ?: "Failed to reload list")
                    //later better to show info to user
                }
        }
    }

}

fun Flow<List<FetchItemModel>>.prepareList(): Flow<List<FetchItemModel>> {
    return this.map { it.prepareList() }
}

/**
 * Since we process name as text after sorting we ca get such examples I_1, I_2, I_30, I_4
 */
fun List<FetchItemModel>.prepareList(): List<FetchItemModel> {
    return filter { it.name.isNullOrBlank().not() } //get only filled names
        .sortedWith(
            compareBy<FetchItemModel>
            { it.listId }  //sort by listId
                .thenBy
                { it.name } //sort by name
        )
}