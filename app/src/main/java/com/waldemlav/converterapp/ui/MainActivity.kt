package com.waldemlav.converterapp.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.waldemlav.converterapp.databinding.ActivityMainBinding
import com.waldemlav.converterapp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertBtn.setOnClickListener {
            hideKeyboard()
            convert()
        }

        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.conversion.collect { event ->
                    when (event) {
                        is MainViewModel.CurrencyEvent.Success -> {
                            binding.pb.isVisible = false
                            binding.tvResult.setTextColor(Color.BLACK)
                            binding.tvResult.text = event.resultText
                        }
                        is MainViewModel.CurrencyEvent.Failure -> {
                            binding.pb.isVisible = false
                            binding.tvResult.setTextColor(Color.RED)
                            binding.tvResult.text = event.errorText
                        }
                        is MainViewModel.CurrencyEvent.Loading -> {
                            binding.pb.isVisible = true
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun convert() {
        viewModel.convert(
            fromCurrency = binding.spnFromCurrency.selectedItem.toString(),
            toCurrency = binding.spnToCurrency.selectedItem.toString(),
            amount = binding.etFrom.text.toString()
        )
    }

    private fun hideKeyboard() {
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type
            .ime())
    }
}