package com.dhevi.ibox.data

import com.dhevi.ibox.model.FakeIboxDataSource
import com.dhevi.ibox.model.OrderIbox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class IboxRepository {

    private val orderIbox = mutableListOf<OrderIbox>()

    init {
        if (orderIbox.isEmpty()) {
            FakeIboxDataSource.dummyIboxes.forEach {
                orderIbox.add(OrderIbox(it, 0))
            }
        }
    }

    fun getAllIbox(): Flow<List<OrderIbox>> {
        return flowOf(orderIbox)
    }

    fun searchIbox(query: String): Flow<List<OrderIbox>>    {
        return flow {
            val searchResults = orderIbox.filter {
                it.ibox.title.contains(query, ignoreCase = true)
            }
            emit(searchResults)
        }
    }

    fun getOrderIboxById(iboxId: Long): OrderIbox {
        return orderIbox.first {
            it.ibox.id == iboxId
        }
    }

    fun updateOrderIbox(iboxId: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderIbox.indexOfFirst { it.ibox.id == iboxId }
        val result = if (index >= 0) {
            val orderIbox = orderIbox[index]
            this.orderIbox[index] =
                orderIbox.copy(ibox = orderIbox.ibox, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderIbox(): Flow<List<OrderIbox>> {
        return getAllIbox()
            .map { orderIboxes ->
                orderIboxes.filter { orderIbox ->
                    orderIbox.count != 0
                }
            }
    }

    companion object {
        @Volatile
        private var instance: IboxRepository? = null

        fun getInstance(): IboxRepository =
            instance ?: synchronized(this) {
                IboxRepository().apply {
                    instance = this
                }
            }
    }
}