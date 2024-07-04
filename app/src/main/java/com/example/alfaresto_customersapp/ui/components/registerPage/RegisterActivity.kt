package com.example.alfaresto_customersapp.ui.components.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityRegisterBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.util.Constants

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val passwordPatterns = Constants.passwordPatterns

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            storeAndValidation()
        }

        binding.loginTextView.setOnClickListener {
            goToLoginPage()
        }
    }

    private fun storeAndValidation() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        val reEnterPassword = binding.etReeenterPassword.text.toString()

        if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
            showValidationError(getString(R.string.email_pass_empty))
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showValidationError(getString(R.string.email_not_valid))
            return
        }

        if (!passwordPatterns.matcher(password).matches()) {
            showValidationError(getString(R.string.password_not_valid))
            return
        }

        if (password != reEnterPassword) {
            showValidationError(getString(R.string.password_not_match))
            return
        }

        viewModel.registerUser(email, name, phone, password) { success ->
            if (success) {
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
                goToLoginPage()
            } else {
                Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showValidationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun goToLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}