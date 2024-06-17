package com.example.alfaresto_customersapp.ui.components.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.databinding.RegisterPageBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding
    lateinit var auth: FirebaseAuth
    private val passwordPatterns = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()
            val reEnterPassword = binding.reEnterPasswordTextInput.text.toString()

            if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
                binding.emailTextInput.error = "Email or password is empty"
                binding.passwordTextInput.error = "Email or password is empty"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailTextInput.error = "Email is not valid"
                binding.emailTextInput.requestFocus()
                return@setOnClickListener
            }

            if (!passwordPatterns.matcher(password).matches()) {
                binding.passwordTextInput.error = "Password is not valid"
                binding.passwordTextInput.requestFocus()
                return@setOnClickListener
            }

            if (password != reEnterPassword) {
                binding.reEnterPasswordTextInput.error = "Password is not match"
                binding.reEnterPasswordTextInput.requestFocus()
                return@setOnClickListener
            }

            registerAuth(email, password)
        }
    }
}

fun RegisterActivity.registerAuth(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
            }
        }
}