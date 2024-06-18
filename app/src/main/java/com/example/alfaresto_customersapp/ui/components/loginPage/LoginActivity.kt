package com.example.alfaresto_customersapp.ui.components.loginPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.databinding.LoginPageBinding
import com.example.alfaresto_customersapp.ui.components.MainActivity
import com.example.alfaresto_customersapp.ui.components.registerPage.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity()  {
    private lateinit var binding: LoginPageBinding
    lateinit var auth: FirebaseAuth
    private val passwordPatterns = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LoginPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
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

            loginAuth(email, password)
        }
        val registerTextClicked: TextView = binding.registerTextView
        registerTextClicked.setOnClickListener {
            directToRegister(it)
        }
    }
}

fun directToRegister(view: View) {
    val intent = Intent(view.context.applicationContext, RegisterActivity::class.java)
    view.context.startActivity(intent)
}

fun LoginActivity.loginAuth(email: String, password: String) {
    auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, "Login Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
}