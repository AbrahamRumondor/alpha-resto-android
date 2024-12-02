package com.example.alfaresto_customersapp.ui.components.restoPage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.data.di.FirebaseModule
import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import com.example.alfaresto_customersapp.launchFragmentInHiltContainer
import com.example.alfaresto_customersapp.ui.components.restoPage.adapter.RestoAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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

  private val menuUseCase: MenuUseCase = mockk()
  private val cartUseCase: CartUseCase = mockk()
  private val userUseCase: UserUseCase = mockk()

  private val mockMenuFlow = MutableStateFlow<List<Menu>>(emptyList())
  private val mockCartFlow = MutableStateFlow<List<CartEntity>>(emptyList())

  @Before
  fun setUp() {
    // Inject Hilt dependencies
    hiltRule.inject()

    // Initialize MockK annotations for mocking
    MockKAnnotations.init(this)
  }

  private fun setUpMockData() {
    // This can be used to setup any additional mock data
    //    // Mock the ViewModel methods
//    coEvery { mockViewModel.fetchMenus() } answers {
//      mockMenuFlow.value = listOf(Menu("1", "Pizza", "Delicious pizza"))
//    }
//    coEvery { mockViewModel.fetchCart() } answers {
//      mockCartFlow.value = listOf(CartEntity(menuId = "1", menuQty = 1))
//    }
//    coEvery { mockViewModel.getToken() } answers {
//      "mock_token"
//    }
  }
//
  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun testElementsDisplayedCorrectly() {
    // Launch the fragment with Hilt container
    launchFragmentInHiltContainer<RestoFragment>()
    onView(withId(R.id.btn_all_menu))
      .check(matches(isDisplayed()))
      .check(matches(isClickable()))
//    onView(withId(R.id.btn_all_menu)).perform(click())
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun testAddButtonClickInRecyclerView() {
    // Launch the fragment
    launchFragmentInHiltContainer<RestoFragment>()

    Thread.sleep(2000)

    // Assert RecyclerView is displayed
    onView(withId(R.id.rv_menu))
      .check(matches(isDisplayed()))

    // Assert RecyclerView is not empty
    onView(withId(R.id.rv_menu))
      .check { view, _ ->
        val recyclerView = view as RecyclerView
        assertTrue("RecyclerView is empty", (recyclerView.adapter?.itemCount ?: 0) > 0)
      }

    // Click the "add" button inside the first item of the RecyclerView
    onView(withId(R.id.rv_menu))
      .perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
          0,
          object : ViewAction {
            override fun getConstraints(): Matcher<View> {
              return isDisplayed()
            }

            override fun getDescription(): String {
              return "Click on the add button inside the first RecyclerView item"
            }

            override fun perform(uiController: UiController, view: View) {
              // Replace `R.id.add_button` with the actual ID of the "add" button
              val addButton = view.findViewById<View>(R.id.btn_menu_add)
              addButton.performClick()
            }
          }
        )
      )

    // Optionally, add verifications if you have access to the listener or ViewModel
    // For example:
    // verify(viewModel).addOrderQuantity(anyContext(), eq("menuId"), any())
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


//package com.example.alfaresto_customersapp.ui.components
//
//
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import androidx.test.core.app.ActivityScenario
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
//import androidx.test.espresso.action.ViewActions.replaceText
//import androidx.test.espresso.action.ViewActions.scrollTo
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.contrib.RecyclerViewActions
//import androidx.test.espresso.matcher.BoundedMatcher
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withClassName
//import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.matcher.ViewMatchers.withParent
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import androidx.test.rule.GrantPermissionRule
//import com.example.alfaresto_customersapp.R
//import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
//import com.example.alfaresto_customersapp.ui.components.restoPage.adapter.RestoViewHolder
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import io.mockk.MockKAnnotations
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//import org.hamcrest.Matchers.allOf
//import org.hamcrest.Matchers.`is`
//import org.hamcrest.TypeSafeMatcher
//import org.hamcrest.core.IsInstanceOf
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class AddItemTest {
//
//  @get:Rule
//  var hiltRule = HiltAndroidRule(this)
//
////  @get:Rule
////  var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)
//
//  @get:Rule
//  var mGrantPermissionRule = GrantPermissionRule.grant(
//    "android.permission.POST_NOTIFICATIONS"
//  )
//
//  @Before
//  fun init() {
//    hiltRule.inject() // This line initializes Hilt
//  }
//
//  @Test
//  fun addItemTest() {
//    // Launch the LoginActivity
//    ActivityScenario.launch(LoginActivity::class.java)
//
//    // Input email
//    onView(withId(R.id.et_email))
//      .perform(scrollTo(), replaceText("abraham@gmail.com"), closeSoftKeyboard())
//
//    // Input password
//    onView(withId(R.id.et_password))
//      .perform(scrollTo(), replaceText("Abraham123!"), closeSoftKeyboard())
//
//    // Click login button
//    onView(withId(R.id.btn_login))
//      .perform(scrollTo(), click())
//
//    // Delay to allow the next screen to load
//    Thread.sleep(2000) // 2 seconds delay
//
//    // Click "Add To Cart" button in the first item of the RecyclerView
//    onView(withId(R.id.rv_menu)) // Replace with your RecyclerView ID
//      .perform(
//        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//        withRecyclerViewItemAtPosition(0, withId(R.id.btn_menu_add)), // Replace with your button ID
//        click()
//      ))
//
//    // Delay to allow the UI to update
//    Thread.sleep(1000) // 1 second delay
//
//    // Check order quantity
//    onView(withId(R.id.tv_order_qty))
//      .check(matches(withText("1")))
//
//    // Click "Add Order"
//    onView(withId(R.id.btn_add_order))
//      .perform(click())
//
//    // Delay to allow the UI to update
//    Thread.sleep(1000) // 1 second delay
//
//    // Check if the cart button is displayed
//    onView(withId(R.id.btn_cart))
//      .check(matches(isDisplayed()))
//
//    // Click on the cart button
//    onView(withId(R.id.btn_cart))
//      .perform(click())
//
//    // Delay to allow the UI to update
//    Thread.sleep(1000) // 1 second delay
//
//    // Check food quantity in order summary
//    onView(withId(R.id.tv_food_qty))
//      .check(matches(withText("2")))
//
//    // Decrease order quantity
//    onView(withId(R.id.iv_order_decrease))
//      .perform(click())
//    onView(withId(R.id.iv_order_decrease))
//      .perform(click())
//
//    // Delay to allow the UI to update
//    Thread.sleep(1000) // 1 second delay
//
//    // Navigate back
//    onView(withId(R.id.btn_back))
//      .perform(click())
//
//    // Delay to allow the UI to update
//    Thread.sleep(1000) // 1 second delay
//
//    // Logout
//    onView(withId(R.id.btn_logout))
//      .perform(click())
//
//    // Confirm logout
//    onView(withText("Logout"))
//      .perform(click())
//  }
//
//  private fun childAtPosition(
//    parentMatcher: Matcher<View>, position: Int
//  ): Matcher<View> {
//
//    return object : TypeSafeMatcher<View>() {
//      override fun describeTo(description: Description) {
//        description.appendText("Child at position $position in parent ")
//        parentMatcher.describeTo(description)
//      }
//
//      public override fun matchesSafely(view: View): Boolean {
//        val parent = view.parent
//        return parent is ViewGroup && parentMatcher.matches(parent)
//          && view == parent.getChildAt(position)
//      }
//    }
//  }
//
//  fun withRecyclerViewItemAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
//    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
//      override fun describeTo(description: Description) {
//        description.appendText("RecyclerView item at position $position: ")
//        itemMatcher.describeTo(description)
//      }
//
//      override fun matchesSafely(recyclerView: RecyclerView): Boolean {
//        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
//        return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
//      }
//    }
//  }
//}