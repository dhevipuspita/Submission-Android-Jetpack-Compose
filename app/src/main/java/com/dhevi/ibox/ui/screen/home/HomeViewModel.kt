package com.dhevi.ibox.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhevi.ibox.data.IboxRepository
import com.dhevi.ibox.model.OrderIbox
import com.dhevi.ibox.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: IboxRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<OrderIbox>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderIbox>>>
        get() = _uiState

    fun getAllIbox() {
        viewModelScope.launch {
            repository.getAllIbox()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderIbox ->
                    _uiState.value = UiState.Success(orderIbox)
                }
        }
    }

    fun searchIbox(query: String) {
        viewModelScope.launch {
            repository.searchIbox(query)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { searchedProducts ->
                    _uiState.value = UiState.Success(searchedProducts)
                }
        }
    }

}