package com.example.alfaresto_customersapp.ui.components.registerPage

import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.security.MessageDigest

class RegisterViewModel_UnitTest {

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(
            userUseCase = mock(UserUseCase::class.java),
            authUseCase = mock(AuthUseCase::class.java)
        )
    }

    @Test
    fun `hashPassword returns correct hash for given password`() {
        val password = "password123"
        val expectedHash = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val actualHash = viewModel.hashPassword(password)

        assertEquals(expectedHash, actualHash)
    }

    @Test
    fun `hashPassword returns different hash for different passwords`() {
        val password1 = "password123"
        val password2 = "differentPassword"

        val hash1 = viewModel.hashPassword(password1)
        val hash2 = viewModel.hashPassword(password2)

        assert(hash1 != hash2)
    }

    @Test
    fun `hashPassword returns same hash for same passwords`() {
        val password = "password123"

        val hash1 = viewModel.hashPassword(password)
        val hash2 = viewModel.hashPassword(password)

        assertEquals(hash1, hash2)
    }
}