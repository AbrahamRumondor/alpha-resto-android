package com.example.alfaresto_customersapp.ui.components.loginPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityLoginBinding
import com.example.alfaresto_customersapp.ui.components.MainActivity
import com.example.alfaresto_customersapp.ui.components.registerPage.RegisterActivity
import com.example.alfaresto_customersapp.ui.util.Constants.passwordPatterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    var firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                binding.etEmail.error = getString(R.string.email_pass_empty)
                binding.etPassword.error = getString(R.string.email_pass_empty)
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = getString(R.string.email_not_valid)
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!passwordPatterns.matcher(password).matches()) {
                binding.etEmail.error = getString(R.string.password_not_valid)
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            loginAuth(email, password)

        }
        val registerTextClicked: TextView = binding.tvRegister
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
    firebaseFirestore = FirebaseFirestore.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, R.string.login_success, Toast.LENGTH_SHORT).show()
                //addAddress(email, address)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        }
}