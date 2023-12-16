package com.dhevi.ibox.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dhevi.ibox.R
import com.dhevi.ibox.di.Injection
import com.dhevi.ibox.model.OrderIbox
import com.dhevi.ibox.ui.ViewModelFactory
import com.dhevi.ibox.ui.common.UiState
import com.dhevi.ibox.ui.components.IboxItem
import com.dhevi.ibox.ui.theme.IboxAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Ibox Store",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.searchIbox(searchText)
                }
            ),
            placeholder = {
                Text(stringResource(id = R.string.search))
            },

            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .heightIn(min = 20.dp)
                .shadow(56.dp)
        )

        viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getAllIbox()
                }

                is UiState.Success -> {
                    HomeContent(
                        orderIbox = uiState.data,
                        searchText = searchText,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                    )
                }

                is UiState.Error -> {
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    orderIbox: List<OrderIbox>,
    searchText: String,
    modifier: Modifier,
    navigateToDetail: (Long) -> Unit
) {
    val filterIbox = orderIbox.filter {
        it.ibox.title.contains(searchText, ignoreCase = true)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(filterIbox) { data ->
            IboxItem(
                image = data.ibox.image,
                title = data.ibox.title,
                requiredPoint = data.ibox.price,
                modifier = Modifier.clickable {
                    navigateToDetail(data.ibox.id)
                }
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    IboxAppTheme {
        HomeScreen(navigateToDetail = {})
    }
}
