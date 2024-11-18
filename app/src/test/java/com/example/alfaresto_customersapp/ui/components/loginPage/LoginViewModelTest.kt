package com.example.alfaresto_customersapp.ui.components.loginPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.alfaresto_customersapp.domain.usecase.auth.AuthUseCase
import com.google.firebase.auth.AuthResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val authUseCase: AuthUseCase = mockk()
  private lateinit var loginViewModel: LoginViewModel

  private val testScheduler = TestCoroutineScheduler()
  private val testDispatcher = StandardTestDispatcher(testScheduler)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    loginViewModel = LoginViewModel(authUseCase)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `loginUser success updates loginResult to true`() = runTest(testDispatcher) {
    val mockAuthResult: AuthResult = mockk()
    coEvery { authUseCase.loginUser(any(), any()) } returns mockAuthResult

    loginViewModel.loginUser("test@example.com", "password")

    // Allow coroutines to complete
    testScheduler.advanceUntilIdle()

    assertEquals(true, loginViewModel.loginResult.getOrAwaitValue())
  }

  @Test
  fun `loginUser failure updates loginResult to false`() = runTest(testDispatcher) {
    coEvery { authUseCase.loginUser(any(), any()) } throws Exception("Login failed")

    loginViewModel.loginUser("test@example.com", "password")

    // Allow coroutines to complete
    testScheduler.advanceUntilIdle()

    assertEquals(false, loginViewModel.loginResult.getOrAwaitValue())
  }
}

// Utility function to await LiveData value
@Throws(TimeoutException::class)
fun <T> LiveData<T>.getOrAwaitValue(
  time: Long = 2, // Adjust timeout if needed
  timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
  var data: T? = null
  val latch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(value: T) {
      data = value
      latch.countDown()
      this@getOrAwaitValue.removeObserver(this)
    }
  }

  this.observeForever(observer)

  if (!latch.await(time, timeUnit)) {
    throw TimeoutException("LiveData value was never set.")
  }

  @Suppress("UNCHECKED_CAST")
  return data as T
}