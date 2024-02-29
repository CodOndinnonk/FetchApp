package com.ondinnonk.fetchapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ondinnonk.fetchapp.repositiry.FetchItemModel
import com.ondinnonk.fetchapp.utils.Coroutineble
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet
import org.koin.core.component.KoinComponent

class MainViewModel(private val repository: Repository) : ViewModel(), KoinComponent, Coroutineble {

    private val uiStateFlow: MutableStateFlow<UiModel> = MutableStateFlow(UiModel.UiModelLoading)
    val uiState = uiStateFlow.asStateFlow()
    private var isShowNullValues = MutableStateFlow(false)

    init {
        launch {
            isShowNullValues.collectLatest {
                updateListData(it)
            }
        }
        reloadList()
    }

    private fun updateListData(isShowNullValues: Boolean) {
        launch {
            uiStateFlow.emitAll(
                repository.itemsFlow
                    .prepareList(isShowNullValues)
                    .map { UiModel.UiModelSuccess(it) }
            )
        }
    }

    fun reloadList() {
        launch {
            repository.reloadList()
                .onFailure {
                    Log.e(FetchApplication.LOG_TAG, it.message ?: "Failed to reload list")
                    uiStateFlow.tryEmit(UiModel.UiModelFail(it.message ?: "Failed to reload list"))
                }
        }
    }

    fun switchNullValues() {
        isShowNullValues.updateAndGet { it.not() }.also { isShowNullValues.tryEmit(it) }
    }

}

fun Flow<List<FetchItemModel>>.prepareList(isShowNullValues: Boolean): Flow<List<FetchItemModel>> {
    return this.map { it.prepareList(isShowNullValues) }
}

/**
 * Since we process name as text after sorting we ca get such examples I_1, I_2, I_30, I_4
 */
fun List<FetchItemModel>.prepareList(isShowNullValues: Boolean): List<FetchItemModel> {
    val list = if (isShowNullValues) filter { it.name == null } else filter { it.name.isNullOrBlank().not() } //get only filled names
    return list
        .sortedWith(
            compareBy<FetchItemModel>
            { it.listId }  //sort by listId
                .thenBy
                { it.name } //sort by name
        )
}

sealed class UiModel() {
    object UiModelLoading : UiModel()
    data class UiModelSuccess(val list: List<FetchItemModel>) : UiModel()
    data class UiModelFail(val error: String) : UiModel()
}
