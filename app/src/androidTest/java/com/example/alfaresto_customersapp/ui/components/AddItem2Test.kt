//package com.example.alfaresto_customersapp.ui.components
//
//
//import android.view.View
//import android.view.ViewGroup
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
//import androidx.test.espresso.action.ViewActions.replaceText
//import androidx.test.espresso.action.ViewActions.scrollTo
//import androidx.test.espresso.assertion.ViewAssertions.matches
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
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//import org.hamcrest.Matchers.allOf
//import org.hamcrest.Matchers.`is`
//import org.hamcrest.TypeSafeMatcher
//import org.hamcrest.core.IsInstanceOf
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class AddItem2Test {
//
//  // TODO ADD HILT CONFIGURATION
//
//  @Rule
//  @JvmField
//  var mActivityScenarioRule = ActivityScenarioRule(SplashScreenActivity::class.java)
//
//  // TODO need changes #1
//  @Rule
//  @JvmField
//  var mGrantPermissionRule =
//    GrantPermissionRule.grant(
//      "android.permission.POST_NOTIFICATIONS"
//    )
//
//  // TODO INJECT HILT on @Before
//
//  @Test
//  fun addItem2Test() {
//    val textInputEditText = onView(
//      allOf(
//        withId(R.id.et_email),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.til_email),
//            0
//          ),
//          0
//        )
//      )
//    )
//    textInputEditText.perform(scrollTo(), replaceText("abraham@gmail.com"), closeSoftKeyboard())
//
//    val textInputEditText2 = onView(
//      allOf(
//        withId(R.id.et_password),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.til_password),
//            0
//          ),
//          0
//        )
//      )
//    )
//    textInputEditText2.perform(scrollTo(), replaceText("Abraham123!"), closeSoftKeyboard())
//
//    val appCompatButton = onView(
//      allOf(
//        withId(R.id.btn_login), withText("Login"),
//        childAtPosition(
//          childAtPosition(
//            withClassName(`is`("android.widget.ScrollView")),
//            0
//          ),
//          8
//        )
//      )
//    )
//    appCompatButton.perform(scrollTo(), click())
//
//    // TODO need changes
//    val appCompatButton2 = onView(
//      allOf(
//        withId(R.id.btn_menu_add), withText("Add To Cart"),
//        childAtPosition(
//          childAtPosition(
//            withClassName(`is`("android.widget.FrameLayout")),
//            0
//          ),
//          2
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatButton2.perform(click())
//
//    // TODO need changes
//    val appCompatButton3 = onView(
//      allOf(
//        withId(R.id.btn_add_order),
//        childAtPosition(
//          allOf(
//            withId(R.id.clActionButtons),
//            childAtPosition(
//              withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
//              3
//            )
//          ),
//          2
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatButton3.perform(click())
//
//    val imageButton = onView(
//      allOf(
//        withId(R.id.btn_cart), withContentDescription("cartLogo"),
//        withParent(
//          allOf(
//            withId(R.id.rl_cart),
//            withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
//          )
//        ),
//        isDisplayed()
//      )
//    )
//    imageButton.check(matches(isDisplayed()))
//
//    val appCompatImageButton = onView(
//      allOf(
//        withId(R.id.btn_cart), withContentDescription("cartLogo"),
//        childAtPosition(
//          allOf(
//            withId(R.id.rl_cart),
//            childAtPosition(
//              withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
//              6
//            )
//          ),
//          0
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatImageButton.perform(click())
//
//    val appCompatImageView = onView(
//      allOf(
//        withId(R.id.iv_order_decrease),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.rv_order_summary),
//            1
//          ),
//          4
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatImageView.perform(click())
//
//    val appCompatImageView2 = onView(
//      allOf(
//        withId(R.id.iv_order_decrease),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.rv_order_summary),
//            1
//          ),
//          4
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatImageView2.perform(click())
//
//    val appCompatImageButton2 = onView(
//      allOf(
//        withId(R.id.btn_back), withContentDescription("Back Logo"),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.toolbar),
//            4
//          ),
//          0
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatImageButton2.perform(click())
//
//    val appCompatImageButton3 = onView(
//      allOf(
//        withId(R.id.btn_logout), withContentDescription("Logout Logo"),
//        childAtPosition(
//          childAtPosition(
//            withId(R.id.toolbar),
//            2
//          ),
//          0
//        ),
//        isDisplayed()
//      )
//    )
//    appCompatImageButton3.perform(click())
//
//    val materialButton = onView(
//      allOf(
//        withId(android.R.id.button1), withText("Logout"),
//        childAtPosition(
//          childAtPosition(
//            withId(com.google.maps.android.R.id.buttonPanel),
//            0
//          ),
//          3
//        )
//      )
//    )
//    materialButton.perform(scrollTo(), click())
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
//}
