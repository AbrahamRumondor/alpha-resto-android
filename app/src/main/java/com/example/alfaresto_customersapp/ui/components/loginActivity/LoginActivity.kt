package com.example.alfaresto_customersapp.ui.components.loginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.databinding.LoginPageBinding
import com.example.alfaresto_customersapp.ui.components.registerActivity.RegisterActivity
import com.example.alfaresto_customersapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity()  {

    private lateinit var binding: LoginPageBinding
    lateinit var auth: FirebaseAuth
    private val passwordPatterns = Constants.passwordPatterns
    var firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LoginPageBinding.inflate(layoutInflater)
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
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            loginAuth(email, password)
            // addAddress(email, address)

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
            } else {
                Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        }
}
//
//fun LoginActivity.addAddress(email: String, address: String) {
//    val userAddress = hashMapOf(
//        "address" to address
//    )
//
//    FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", email)
//        .get()
//        .addOnSuccessListener { documents ->
//            if (documents.isEmpty) {
//                Log.d("Firestore", "No user found with email: $email")
//                return@addOnSuccessListener
//            }
//
//            for (document in documents) {
//                val userDocRef = document.reference
//                val addressCollectionRef = userDocRef.collection("address")
//
//                addressCollectionRef.add(userAddress)
//                    .addOnSuccessListener {
//                        Toast.makeText(baseContext, "Address added", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener { _ ->
//                        Toast.makeText(baseContext, "Error adding address", Toast.LENGTH_SHORT).show()
//                    }
//            }
//        }
//        .addOnFailureListener { _ ->
//            Toast.makeText(baseContext, "Error adding address", Toast.LENGTH_SHORT).show()
//        }
//}