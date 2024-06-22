package com.example.alfaresto_customersapp.ui.components.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.RegisterPageBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.util.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding
    lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val passwordPatterns = Constants.passwordPatterns

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.registerButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val image = "-"
            val password = binding.passwordTextInput.text.toString()
            val name = binding.nameTextInput.text.toString()
            val noTelp = binding.noTelpTextInput.text.toString()
            val reEnterPassword = binding.reEnterPasswordTextInput.text.toString()

            if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
                binding.emailTextInput.error = getString(R.string.email_pass_empty)
                binding.passwordTextInput.error = getString(R.string.email_pass_empty)
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailTextInput.error = getString(R.string.email_not_valid)
                binding.emailTextInput.requestFocus()
                return@setOnClickListener
            }

            if (!passwordPatterns.matcher(password).matches()) {
                binding.passwordTextInput.error = getString(R.string.password_not_valid)
                binding.passwordTextInput.requestFocus()
                return@setOnClickListener
            }

            if (password != reEnterPassword) {
                binding.reEnterPasswordTextInput.error = getString(R.string.password_not_match)
                binding.reEnterPasswordTextInput.requestFocus()
                return@setOnClickListener
            }

            registerAuth(email, image, name, noTelp, password)
        }
        val loginTextClicked: TextView = binding.loginTextView
        loginTextClicked.setOnClickListener {
            directToLogin(it)
        }
    }
}

fun directToLogin(view: View) {
    val intent = Intent(view.context.applicationContext, LoginActivity::class.java)
    view.context.startActivity(intent)
}

fun RegisterActivity.registerAuth(email: String, image: String, name: String, noTelp: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { register ->
            if (register.isSuccessful) {
                val user = auth.currentUser
                val id = user?.uid ?: return@addOnCompleteListener
                addToFirestore(email, id, image, name, noTelp, password)
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
            }
        }
}

fun addToFirestore(email: String, id: String, image: String, name: String, noTelp: String, password: String) {
    val hashedPassword = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).joinToString("") {
        "%02x".format(it)
    }
    val user = hashMapOf(
        "user_email" to email,
        "user_id" to id,
        "user_image" to image,
        "user_name" to name,
        "user_no_telp" to noTelp,
        "user_password" to hashedPassword
    )
    FirebaseFirestore.getInstance().collection("users").document(id).set(user)
}

