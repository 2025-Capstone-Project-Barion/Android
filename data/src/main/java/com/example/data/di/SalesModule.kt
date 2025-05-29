package com.example.data.di

// di/SalesModule.kt

import com.example.data.api.SalesApi
import com.example.data.repository.SalesRepositoryImpl
import com.example.domain.repository.SalesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Sales 모듈 의존성 주입 설정
 */
@Module
@InstallIn(SingletonComponent::class)
object SalesModule {

    @Provides
    @Singleton
    fun provideSalesApi(retrofit: Retrofit): SalesApi {
        return retrofit.create(SalesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSalesRepository(salesApi: SalesApi): SalesRepository {
        return SalesRepositoryImpl(salesApi)
    }
}