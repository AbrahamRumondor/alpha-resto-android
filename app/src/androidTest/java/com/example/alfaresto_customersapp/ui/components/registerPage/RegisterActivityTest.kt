package com.example.alfaresto_customersapp.ui.components.registerPage

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Before
  fun setUp() {
    hiltRule.inject() // Inject dependencies into the test class

    // Launch before each test
    ActivityScenario.launch(RegisterActivity::class.java)
  }

  @Test
  fun testLoginSuccess() {
    onView(withId(R.id.btn_register))
      .check(matches(isDisplayed()))
  }

}