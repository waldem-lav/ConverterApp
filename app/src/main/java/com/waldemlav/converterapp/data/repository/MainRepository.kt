package com.waldemlav.converterapp.data.repository

import com.waldemlav.converterapp.data.network.CurrencyApiService
import com.waldemlav.converterapp.data.network.model.ExchangeDto
import com.waldemlav.converterapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: CurrencyApiService
) {

    suspend fun getExchangeData(
        fromCurrency: String,
        toCurrency: String,
        amount: String
    ): Resource<ExchangeDto> {
        return try {
            val response = apiService.getExchangeData(fromCurrency, toCurrency, amount)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}