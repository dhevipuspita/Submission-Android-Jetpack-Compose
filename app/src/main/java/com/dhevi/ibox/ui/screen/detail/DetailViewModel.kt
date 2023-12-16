package com.dhevi.ibox.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhevi.ibox.data.IboxRepository
import com.dhevi.ibox.model.OrderIbox
import com.dhevi.ibox.model.Ibox
import com.dhevi.ibox.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: IboxRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<OrderIbox>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderIbox>>
        get() = _uiState

    fun getIboxById(iboxId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderIboxById(iboxId))
        }
    }

    fun addToCart(ibox: Ibox, count: Int) {
        viewModelScope.launch {
            repository.updateOrderIbox(ibox.id, count)
        }
    }
}