package com.dhevi.ibox.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dhevi.ibox.R
import com.dhevi.ibox.di.Injection
import com.dhevi.ibox.ui.ViewModelFactory
import com.dhevi.ibox.ui.common.UiState
import com.dhevi.ibox.ui.components.OrderButton
import com.dhevi.ibox.ui.components.ProductCounter
import com.dhevi.ibox.ui.theme.IboxAppTheme

@Composable
fun DetailScreen(
    iboxId: Long,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getIboxById(iboxId)
            }
            is UiState.Success -> {
                val data = uiState.data
                println("DetailScreen: Product ID: ${data.ibox.id}, Detail: ${data.ibox.detail}")
                val navController = rememberNavController()

                DetailContent(
                    image = data.ibox.image,
                    title = data.ibox.title,
                    price = data.ibox.price,
                    color = data.ibox.color,
                    detail = data.ibox.detail,
                    count = data.count,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToCart(data.ibox, count)
                        navigateToCart()
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    price: Long,
    color: String,
    detail: String,
    count: Int,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    var totalPoint by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = color,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = colorResource(id = R.color.purple_200)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource (R.string.required_point, price),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(LightGray))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProductCounter(
                1,
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            totalPoint = (price * orderCount).toInt()
            OrderButton(
                text = stringResource(R.string.add_to_cart, totalPoint),
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                }
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    IboxAppTheme {
        DetailContent(
            R.drawable.ipad_mini,
            "iPad Mini Generasi 5 256GB",
            24999000,
            "Purple Glossy",
            "iPad mini dirancang dengan cermat dan begitu memikat. Penutup yang sepenuhnya baru dengan layar baru yang lebih besar membentang dari ujung ke ujung, serta tepian yang tipis dan sudut melengkung yang elegan.",
            1,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}