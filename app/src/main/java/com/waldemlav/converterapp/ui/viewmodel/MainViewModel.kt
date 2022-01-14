package com.waldemlav.converterapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemlav.converterapp.data.repository.MainRepository
import com.waldemlav.converterapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion.asStateFlow()

    fun convert(
        fromCurrency: String,
        toCurrency: String,
        amount: String
    ) {
        val fromAmount = amount.toDoubleOrNull()
        if (fromAmount == null || fromAmount <= 0) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch {
            _conversion.value = CurrencyEvent.Loading
            when (
                val exchangeResponse =
                    repository.getExchangeData(fromCurrency, toCurrency, amount)
            ) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(exchangeResponse.message!!)
                is Resource.Success -> {
                    val toAmount = exchangeResponse.data!!.result
                    _conversion.value = CurrencyEvent.Success(
                        "${roundIfInt(fromAmount)} $fromCurrency = " +
                                "${roundIfInt(toAmount)} $toCurrency"
                    )
                }
            }
        }
    }

    private fun roundIfInt(double: Double): String {
        return if (floor(double) == double) {
            double.roundToInt().toString()
        } else {
            double.toString()
        }
    }
}