package com.example.alfaresto_customersapp.ui.components.loginPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.LoginPageBinding
import com.example.alfaresto_customersapp.ui.components.MainActivity
import com.example.alfaresto_customersapp.ui.components.loginPage.repository.AuthRepositoryImpl
import com.example.alfaresto_customersapp.ui.components.registerPage.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepository = AuthRepositoryImpl()
        viewModel = LoginViewModel(authRepository)

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()

            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerTextView.setOnClickListener {
            directToRegister(it)
        }
    }

    private fun directToRegister(view: android.view.View) {
        val intent = Intent(view.context.applicationContext, RegisterActivity::class.java)
        view.context.startActivity(intent)
    }
}
