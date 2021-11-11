package com.codevalley.envisionandroidassignment.viewModel.splashViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashViewModel  : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<Int> = MutableStateFlow(3)
    val mutableStateFlow: MutableStateFlow<Int> = _mutableStateFlow

    fun startTimer() {
        viewModelScope.launch {
            delay(3000)
            _mutableStateFlow.emit(0)
        }
    }
}