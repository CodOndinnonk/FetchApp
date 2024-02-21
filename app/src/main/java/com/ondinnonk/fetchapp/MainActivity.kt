package com.ondinnonk.fetchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ondinnonk.fetchapp.repositiry.FetchItemModel
import com.ondinnonk.fetchapp.ui.theme.FetchAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val list by viewModel.itemFlow.collectAsStateWithLifecycle()
                    itemsList(list = list)
                }
            }
        }
    }
}

@Composable
fun itemsList(modifier: Modifier = Modifier, list: List<FetchItemModel>) {
    LazyColumn(modifier = modifier) {
        items(list) { listItem(it) }
    }
}

@Composable
fun listItem(model: FetchItemModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        Row() {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "list id = " + model.listId,
                fontSize = 16.sp,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = model.name.orEmpty(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "id = " + model.id,
                fontSize = 8.sp
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    FetchAppTheme {
        itemsList(
            list = listOf(
                FetchItemModel(id = 1, name = "NAME 1", listId = 2),
                FetchItemModel(id = 2, name = "NAME 2", listId = 3),
                FetchItemModel(id = 3, name = "NAME 3", listId = 4)
            ),
        )
    }
}