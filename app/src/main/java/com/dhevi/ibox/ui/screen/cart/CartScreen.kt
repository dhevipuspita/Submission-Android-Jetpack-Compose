package com.dhevi.ibox.ui.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dhevi.ibox.di.Injection
import com.dhevi.ibox.ui.ViewModelFactory
import com.dhevi.ibox.ui.common.UiState
import com.dhevi.ibox.ui.components.CartItem
import com.dhevi.ibox.ui.components.OrderButton
import com.dhevi.ibox.R
import com.dhevi.ibox.ui.screen.home.HomeScreen
import com.dhevi.ibox.ui.theme.IboxAppTheme

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    onOrderButtonClicked: (String) -> Unit,
) {

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAddedOrderIbox()
            }
            is UiState.Success -> {
                CartContent(
                    uiState.data,
                    onProductCountChanged = { iboxId, count ->
                        viewModel.updateOrderIbox(iboxId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }
            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shareMessage = stringResource(
        R.string.share_message,
        state.orderIbox.count(),
        state.totalRequiredPoint
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.menu_cart),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(weight = 1f)
        ) {
            items(state.orderIbox, key = { it.ibox.id }) { item ->
                CartItem(
                    iboxId = item.ibox.id,
                    image = item.ibox.image,
                    title = item.ibox.title,
                    totalPoint = item.ibox.price * item.count,
                    count = item.count,
                    onProductCountChanged = onProductCountChanged,
                )
                Divider()
            }
        }
        OrderButton(
            text = stringResource(R.string.total_order, state.totalRequiredPoint),
            enabled = state.orderIbox.isNotEmpty(),
            onClick = {
                onOrderButtonClicked(shareMessage)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun CartScreenPreview() {
    IboxAppTheme {
        CartScreen(onOrderButtonClicked = {})
    }
}