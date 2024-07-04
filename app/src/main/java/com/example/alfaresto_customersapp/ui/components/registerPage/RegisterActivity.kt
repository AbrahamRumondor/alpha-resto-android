package com.example.alfaresto_customersapp.ui.components.registerPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.ActivityRegisterBinding
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var ref: DocumentReference
    private val passwordPatterns = Constants.passwordPatterns

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        ref = firebaseFirestore.collection("users").document()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val id = ref.id
            val image = "-"
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()
            val noTelp = binding.etPhone.text.toString()
            val reEnterPassword = binding.etReeenterPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
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
                binding.etPassword.error = getString(R.string.password_not_valid)
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != reEnterPassword) {
                binding.etReeenterPassword.error = getString(R.string.password_not_match)
                binding.etReeenterPassword.requestFocus()
                return@setOnClickListener
            }

            registerAuth(email, id, image, name, noTelp, password)
        }
        val loginTextClicked: TextView = binding.tvLogin
        loginTextClicked.setOnClickListener {
            directToLogin(it)
        }
    }
}

fun directToLogin(view: View) {
    val intent = Intent(view.context.applicationContext, LoginActivity::class.java)
    view.context.startActivity(intent)
}

fun RegisterActivity.registerAuth(email: String, id: String, image: String, name: String, noTelp: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { register ->
            if (register.isSuccessful) {
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
    val user = hashMapOf(
        "user_email" to email,
        "user_id" to id,
        "user_image" to image,
        "user_name" to name,
        "user_no_telp" to noTelp,
        "user_password" to password.hashCode()
    )
    FirebaseFirestore.getInstance().collection("users").document().set(user)
}

