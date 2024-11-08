package com.example.alfaresto_customersapp.ui.components.restoPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.di.FirebaseModule
import com.example.alfaresto_customersapp.ui.components.mainActivity.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Singleton

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
@UninstallModules(FirebaseModule::class) // Uninstall the real Firebase module for testing
class RestoFragmentTest {

  // Test-specific Hilt module to mock Firestore
  @Module
  @InstallIn(SingletonComponent::class)
  object TestFirebaseModule {
    @Provides
    @Singleton
    fun provideMockFirestore(): FirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
  }

  @Before
  fun setup() {
    // Launch the MainActivity to test navigation
    val activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)
    activityScenario.onActivity { activity ->
      // Find the NavHostFragment in the activity and set up the navigation controller
      val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.fcv_switch_screen) as NavHostFragment
      val navController = navHostFragment.navController

      // Set up the BottomNavigationView with the navController
      navController.let { navController ->
        activity.findViewById<BottomNavigationView>(R.id.bnv_customer_navigation).setupWithNavController(navController)
      }
    }
  }

  // Test if "Best Seller" text is displayed in the toolbar
  @Test
  fun testToolbarVisibility() {
    onView(withId(R.id.tv_best_seller))
      .check(matches(isDisplayed()))
  }
//
//  // Test if the RecyclerView is visible
//  @Test
//  fun testRecyclerViewIsVisible() {
//    onView(withId(R.id.rv_menu))
//      .check(matches(isDisplayed()))
//  }
//
//  // Test if the "All Menu" button is clickable
//  @Test
//  fun testAllMenuButtonIsClickable() {
//    onView(withId(R.id.btn_all_menu))
//      .perform(click())
//  }
//
//  // Test if the cart icon is visible and clickable
//  @Test
//  fun testCartButton() {
//    onView(withId(R.id.btn_cart))
//      .check(matches(isDisplayed()))
//      .perform(click())
//  }
//
//  // Test if the cart item count is visible and matches expected value
//  @Test
//  fun testCartItemCount() {
//    onView(withId(R.id.tv_cart_count))
//      .check(matches(withText("0"))) // Adjust based on initial value
//  }
//
//  // Test visibility of the "All Menu" button based on conditions
//  @Test
//  fun testAllMenuButtonVisibility() {
//    onView(withId(R.id.btn_all_menu))
//      .check(matches(isDisplayed()))
//  }
//
//  // Test visibility of relative layout (Cart)
//  @Test
//  fun testCartLayoutVisibility() {
//    onView(withId(R.id.rl_cart))
//      .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
//  }
}