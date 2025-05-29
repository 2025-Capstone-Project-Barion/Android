// :data/src/main/java/com/example/data/di/StaffDataModule.kt
package com.example.data.di

import com.example.data.api.StaffApi
import com.example.data.repository.StaffRepositoryImpl
import com.example.domain.repository.StaffRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StaffDataModule {

    @Binds
    @Singleton
    abstract fun bindStaffRepository(
        staffRepositoryImpl: StaffRepositoryImpl
    ): StaffRepository

    companion object {
        @Provides
        @Singleton
        fun provideStaffApi(retrofit: Retrofit): StaffApi {
            return retrofit.create(StaffApi::class.java)
        }
    }
}