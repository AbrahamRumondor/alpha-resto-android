package com.example.alfaresto_customersapp.ui.components.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.RegisterPageBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.util.Constants

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding
    private lateinit var viewModel: RegisterViewModel
    private val passwordPatterns = Constants.passwordPatterns

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.registerButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val image = "-"
            val password = binding.passwordTextInput.text.toString()
            val name = binding.nameTextInput.text.toString()
            val noTelp = binding.noTelpTextInput.text.toString()
            val reEnterPassword = binding.reEnterPasswordTextInput.text.toString()

            if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
                showValidationError(getString(R.string.email_pass_empty))
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showValidationError(getString(R.string.email_not_valid))
                return@setOnClickListener
            }

            if (!passwordPatterns.matcher(password).matches()) {
                showValidationError(getString(R.string.password_not_valid))
                return@setOnClickListener
            }

            if (password != reEnterPassword) {
                showValidationError(getString(R.string.password_not_match))
                return@setOnClickListener
            }

            viewModel.registerUser(email, image, name, noTelp, password) { success ->
                if (success) {
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
                    directToLogin()
                } else {
                    Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginTextView.setOnClickListener {
            directToLogin()
        }
    }

    private fun showValidationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun directToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}