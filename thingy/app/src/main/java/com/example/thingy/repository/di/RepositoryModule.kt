package com.example.thingy.repository.di

import com.example.thingy.network.ApiService
import com.example.thingy.repository.AxonRepositoryInterface
import com.example.thingy.repository.AxonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAxonRepository(
        apiService: ApiService,
    ): AxonRepositoryInterface {
        return AxonRepository(apiService)
    }

}