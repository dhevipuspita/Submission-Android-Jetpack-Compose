package com.dhevi.ibox.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhevi.ibox.data.IboxRepository
import com.dhevi.ibox.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: IboxRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderIbox() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderIbox()
                .collect { orderIbox ->
                    val totalRequiredPoint =
                        orderIbox.sumOf { it.ibox.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderIbox, totalRequiredPoint))
                }
        }
    }

    fun updateOrderIbox(iboxId: Long, count: Int) {
        viewModelScope.launch {
            repository.updateOrderIbox(iboxId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderIbox()
                    }
                }
        }
    }
}