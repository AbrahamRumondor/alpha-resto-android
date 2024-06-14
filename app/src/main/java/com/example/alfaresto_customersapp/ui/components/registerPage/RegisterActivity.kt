package com.example.alfaresto_customersapp.ui.components.registerPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alfaresto_customersapp.databinding.RegisterPageBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}