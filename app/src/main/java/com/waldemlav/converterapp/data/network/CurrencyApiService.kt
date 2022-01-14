package com.waldemlav.converterapp.data.network

import com.waldemlav.converterapp.data.network.model.ExchangeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("convert")
    suspend fun getExchangeData(
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String,
        @Query("amount") amount: String,
        // Round numbers to decimal place
        @Query("places") decimalPlace: String = "2"
    ): Response<ExchangeDto>
}