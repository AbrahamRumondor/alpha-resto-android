package com.example.alfaresto_customersapp.ui.components.loginPage

import android.widget.EditText
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.alfaresto_customersapp.R

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

  @Before
  fun setUp() {
    // Launch the LoginActivity before each test
    ActivityScenario.launch(LoginActivity::class.java)
  }

  @Test
  fun testLoginSuccess() {
    onView(withId(R.id.et_email))
      .check(matches(isDisplayed()))

    onView(withId(R.id.tv_title))
      .check(matches(withText("Welcome Back")))

    //    // Find and interact with the email field
//    onView(withId(R.id.et_email))
//      .perform(typeText("user@example.com"), closeSoftKeyboard())
//
//    // Find and interact with the password field
//    onView(withId(R.id.et_password))
//      .perform(typeText("password123"), closeSoftKeyboard())
//
//    // Click the login button
//    onView(withId(R.id.btn_login)).perform(click())
//
//    // Check that the next screen or element appears after login
//    // This can be based on what happens next, e.g. if the next screen has a certain TextView
//    onView(withText("Welcome")).check(matches(isDisplayed()))

  }

//  @Test
//  fun testLoginEmptyFields() {
//    // Click the login button without filling the fields
//    onView(withId(R.id.btn_login)).perform(click())
//
//    // Check if an error message is shown for the email and password
//    onView(withId(R.id.til_email)).check(matches(withText("Email is required")))
//    onView(withId(R.id.til_password)).check(matches(withText("Password is required")))
//  }

//  @Test
//  fun testRegisterRedirect() {
//    // Click on the register link
//    onView(withId(R.id.tv_register)).perform(click())
//
//    // Check that the registration screen opens (assuming a TextView with "Register" text appears)
//    onView(withText("Register Here")).check(matches(isDisplayed()))
//  }
}