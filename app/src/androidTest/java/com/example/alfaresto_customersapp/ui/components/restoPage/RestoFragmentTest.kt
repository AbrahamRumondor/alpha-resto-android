package com.example.alfaresto_customersapp.ui.components.restoPage

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.di.FirebaseModule
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.launchFragmentInHiltContainer
import com.example.alfaresto_customersapp.ui.components.restoPage.adapter.RestoAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(FirebaseModule::class)
class RestoFragmentTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @MockK
  private lateinit var mockViewModel: RestoViewModel

  @MockK
  private lateinit var mockAdapter: RestoAdapter

  private val mockMenuFlow = MutableStateFlow<List<Menu>>(emptyList())
  private val mockCartFlow = MutableStateFlow<List<CartEntity>>(emptyList())

  @Before
  fun setUp() {
    // Inject Hilt dependencies
    hiltRule.inject()

    // Initialize MockK annotations for mocking
    MockKAnnotations.init(this)

    // Set up the mocked ViewModel and its flows
    setUpMockData()
  }

  private fun setUpMockData() {
    // Mock ViewModel flows for menus and cart
    every { mockViewModel.menus } returns mockMenuFlow
    every { mockViewModel.cart } returns mockCartFlow
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun testElementsDisplayedCorrectly() {
//    // Mock data for menus and cart
//    val mockMenus = listOf(Menu("1", "Pizza", "Delicious pizza"))
//
//    // Update the flows with mock data
//    mockMenuFlow.value = mockMenus
//    mockCartFlow.value = emptyList()
//
//    // Launch the fragment in a Hilt container
//    launchFragmentInHiltContainer<RestoFragment> {
//        val viewModel: RestoViewModel = mockViewModel
//    }
//    // Perform a click action to check UI behavior
//    onView(withId(R.id.btn_all_menu)).perform(click())
  }

//  @After
//  fun tearDown() {
//    // Clear invocations to prevent potential memory leaks
//    clearAllMocks()
//  }
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