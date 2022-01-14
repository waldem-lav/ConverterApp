package com.waldemlav.converterapp.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waldemlav.converterapp.data.network.CurrencyApiService
import com.waldemlav.converterapp.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.exchangerate.host/")
    }

    @Singleton
    @Provides
    fun provideCurrencyApiService(retrofit: Retrofit.Builder): CurrencyApiService {
        return retrofit.build().create(CurrencyApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApiService): MainRepository {
        return MainRepository(api)
    }
}