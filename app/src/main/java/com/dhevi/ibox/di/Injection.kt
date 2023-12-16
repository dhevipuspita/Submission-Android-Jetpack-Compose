package com.dhevi.ibox.di

import com.dhevi.ibox.data.IboxRepository


object Injection {
    fun provideRepository(): IboxRepository {
        return IboxRepository.getInstance()
    }
}