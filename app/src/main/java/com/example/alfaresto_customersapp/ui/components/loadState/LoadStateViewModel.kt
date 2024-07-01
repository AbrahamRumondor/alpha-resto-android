package com.example.alfaresto_customersapp.ui.components.loadState

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class LoadStateViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    protected fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}