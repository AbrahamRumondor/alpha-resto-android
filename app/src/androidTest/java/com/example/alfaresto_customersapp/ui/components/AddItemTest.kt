package com.example.alfaresto_customersapp.ui.components


import android.Manifest
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import com.example.alfaresto_customersapp.BuildConfig
import com.example.alfaresto_customersapp.R
import com.example.alfaresto_customersapp.ui.components.loginPage.LoginActivity
import com.example.alfaresto_customersapp.ui.components.restoPage.adapter.RestoViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class AddItemTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

//  @get:Rule
//  var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)


  // ini plis yang di run
  @get:Rule
  val mGrantPermissionRule: GrantPermissionRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    GrantPermissionRule.grant(Manifest.permission.INTERNET)
    GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
  } else {
    GrantPermissionRule.grant(Manifest.permission.INTERNET)
  }

  @Before
  fun init() {
    hiltRule.inject() // This line initializes Hilt
  }

  @Test
  fun addItemTest() {

    ActivityScenario.launch(LoginActivity::class.java)

    val textInputEditText = onView(
      allOf(
        withId(R.id.et_email),
        childAtPosition(
          childAtPosition(
            withId(R.id.til_email),
            0
          ),
          0
        )
      )
    )
    textInputEditText.perform(scrollTo(), replaceText("abraham@gmail.com"), closeSoftKeyboard())

    val textInputEditText2 = onView(
      allOf(
        withId(R.id.et_password),
        childAtPosition(
          childAtPosition(
            withId(R.id.til_password),
            0
          ),
          0
        )
      )
    )
    textInputEditText2.perform(scrollTo(), replaceText("Abraham123!"), closeSoftKeyboard())

    val appCompatButton = onView(
      allOf(
        withId(R.id.btn_login), withText("Login"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          8
        )
      )
    )
    appCompatButton.perform(scrollTo(), click())
    Thread.sleep(4000) // 2 seconds delay

    onView(withId(R.id.rv_menu))
      .perform(
        RecyclerViewActions.actionOnItemAtPosition<RestoViewHolder>(
          0, // Replace with the desired item position
          clickChildViewWithId(R.id.btn_menu_add) // Replace with the button's ID
        )
      )
    Thread.sleep(2000) // 2 seconds delay

    onView(withId(R.id.rv_menu))
      .perform(
        RecyclerViewActions.actionOnItemAtPosition<RestoViewHolder>(
          0, // Replace with the desired item position
          clickChildViewWithId(R.id.btn_add_order) // Replace with the button's ID
        )
      )
    Thread.sleep(2000) // 2 seconds delay

    val imageButton = onView(
      allOf(
        withId(R.id.btn_cart), withContentDescription("cartLogo"),
        withParent(
          allOf(
            withId(R.id.rl_cart),
            withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
          )
        ),
        isDisplayed()
      )
    )
    imageButton.check(matches(isDisplayed()))

    val appCompatImageButton = onView(
      allOf(
        withId(R.id.btn_cart), withContentDescription("cartLogo"),
        childAtPosition(
          allOf(
            withId(R.id.rl_cart),
            childAtPosition(
              withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
              6
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatImageButton.perform(click())
    Thread.sleep(4000) // 2 seconds delay

    // from here onwards we're in orderSummary fragments (main activity)
    val textView2 = onView(
      allOf(
        withId(R.id.tv_food_qty), withText("2"),
        withParent(withParent(withId(R.id.rv_order_summary))),
        isDisplayed()
      )
    )
    textView2.check(matches(withText("2")))

    val appCompatImageView = onView(
      allOf(
        withId(R.id.iv_order_decrease),
        childAtPosition(
          childAtPosition(
            withId(R.id.rv_order_summary),
            1
          ),
          4
        ),
        isDisplayed()
      )
    )
    appCompatImageView.perform(click())

    val appCompatImageView2 = onView(
      allOf(
        withId(R.id.iv_order_decrease),
        childAtPosition(
          childAtPosition(
            withId(R.id.rv_order_summary),
            1
          ),
          4
        ),
        isDisplayed()
      )
    )
    appCompatImageView2.perform(click())

    val appCompatImageButton2 = onView(
      allOf(
        withId(R.id.btn_back), withContentDescription("Back Logo"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            4
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatImageButton2.perform(click())

    val appCompatImageButton3 = onView(
      allOf(
        withId(R.id.btn_logout), withContentDescription("Logout Logo"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            2
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatImageButton3.perform(click())

    val materialButton = onView(
      allOf(
        withId(android.R.id.button1), withText("Logout"),
        childAtPosition(
          childAtPosition(
            withId(com.google.maps.android.R.id.buttonPanel),
            0
          ),
          3
        )
      )
    )
    materialButton.perform(scrollTo(), click())
  }

  private fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
  ): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
      }

      public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return parent is ViewGroup && parentMatcher.matches(parent)
          && view == parent.getChildAt(position)
      }
    }
  }

  fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
          ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(RecyclerView::class.java)),
          ViewMatchers.isClickable()
        )
      }

      override fun getDescription(): String {
        return "Click on a child view with specified id."
      }

      override fun perform(uiController: UiController?, view: View) {
        // Find the child view with the specified ID
        val childView = view.findViewById<Button>(id)
        // Perform click on the child view if it is not null
        childView?.performClick()
      }
    }
  }
}
