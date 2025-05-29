package com.example.data.di

import com.example.data.api.OrderApi
import com.example.data.repository.OrderRepositoryImpl
import com.example.domain.repository.OrderRepository
import com.example.domain.usecase.order.DeleteOrderUseCase
import com.example.domain.usecase.order.GetAllOrdersUseCase
import com.example.domain.usecase.order.GetOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object OrderModule {

    @Provides
    @ViewModelScoped
    fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideOrderRepository(
        api: OrderApi
    ): OrderRepository {
        return OrderRepositoryImpl(api)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllOrdersUseCase(
        repository: OrderRepository
    ): GetAllOrdersUseCase {
        return GetAllOrdersUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetOrderUseCase(
        repository: OrderRepository
    ): GetOrderUseCase {
        return GetOrderUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteOrderUseCase(
        repository: OrderRepository
    ): DeleteOrderUseCase {
        return DeleteOrderUseCase(repository)
    }
}