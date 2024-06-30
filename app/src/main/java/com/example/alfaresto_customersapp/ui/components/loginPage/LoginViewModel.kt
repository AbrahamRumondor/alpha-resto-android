package com.example.alfaresto_customersapp.ui.components.loginPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alfaresto_customersapp.ui.components.loginPage.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun login(email: String, password: String) {
        authRepository.login(email, password) { success ->
            if (success) {
                _loginResult.postValue(true)
            } else {
                _loginResult.postValue(false)
            }
        }
}
}
