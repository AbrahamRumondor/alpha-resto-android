package com.example.alfaresto_customersapp.ui.components.loginPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.LoginPageBinding
import com.example.alfaresto_customersapp.ui.components.MainActivity
import com.example.alfaresto_customersapp.ui.components.loginPage.repository.AuthRepositoryImpl
import com.example.alfaresto_customersapp.ui.components.registerPage.RegisterPage

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isLoggedIn()) {
            navigateToMainActivity()
            return
        }

        val authRepository = AuthRepositoryImpl()
        viewModel = LoginViewModel(authRepository)

        binding.btnLogin.setOnClickListener {
            validateLogin()
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                saveLoginStatus(true)
                navigateToMainActivity()
            } else {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            goToRegisterPage(it)
        }
    }

    private fun validateLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        Log.d("LoginActivity", "Email: $email, Password: $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.email_pass_empty, Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.email_not_valid, Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.login(email, password)
    }

    private fun goToRegisterPage(view: View) {
        val intent = Intent(view.context.applicationContext, RegisterPage::class.java)
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}

