package com.dhevi.ibox.ui.screen.cart

import com.dhevi.ibox.model.OrderIbox

data class CartState(
    val orderIbox: List<OrderIbox>,
    val totalRequiredPoint: Long
)