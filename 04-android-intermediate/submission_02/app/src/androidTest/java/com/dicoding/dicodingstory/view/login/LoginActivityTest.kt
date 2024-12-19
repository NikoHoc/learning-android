package com.dicoding.dicodingstory.view.login

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.dicoding.dicodingstory.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    private val registeredEmail = "nikohenrik22@gmail.com"
    private val registeredPassword = "jalanego2"
    private val unregisteredEmail = "akuhoc2@gmail.com"
    private val unregisteredPassword = "akuhoc2@gmail.com"

    @Before
    fun setup () {
        ActivityScenario.launch(LoginActivity::class.java)
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
        disableSystemAnimations()
    }


    @Test
    fun test_01_testLoginWithUnregisteredEmail() {
        onView(withId(R.id.ed_login_email)).perform(typeText(unregisteredEmail))
        closeSoftKeyboard()

        onView(withId(R.id.ed_login_password)).perform(typeText(registeredPassword))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.error_alert_reply)).inRoot(isDialog()).check(matches(isDisplayed())).perform(
            click()
        )

        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
    }

    @Test
    fun test_02_testLoginWithUnregisteredPassword() {
        onView(withId(R.id.ed_login_email)).perform(typeText(registeredEmail))
        closeSoftKeyboard()

        onView(withId(R.id.ed_login_password)).perform(typeText(unregisteredPassword))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.error_alert_reply)).inRoot(isDialog()).check(matches(isDisplayed())).perform(
            click()
        )

        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
    }

    @Test
    fun test_03_testLoginLogoutWithRegisteredAccount() {
        onView(withId(R.id.ed_login_email)).perform(typeText(registeredEmail))
        closeSoftKeyboard()

        onView(withId(R.id.ed_login_password)).perform(typeText(registeredPassword))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.success_alert_reply)).inRoot(isDialog()).check(matches(isDisplayed())).perform(
            click()
        )

        Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.logout)).check(matches(isDisplayed())).perform(click())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
        enableSystemAnimations()
    }

    private fun enableSystemAnimations() {
        try {
            val instrumentation = getInstrumentation()
            val uiAutomation = instrumentation.uiAutomation
            uiAutomation.executeShellCommand("settings put global window_animation_scale 1.0")
            uiAutomation.executeShellCommand("settings put global transition_animation_scale 1.0")
            uiAutomation.executeShellCommand("settings put global animator_duration_scale 1.0")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun disableSystemAnimations() {
        try {
            val instrumentation = getInstrumentation()
            val uiAutomation = instrumentation.uiAutomation
            uiAutomation.executeShellCommand("settings put global window_animation_scale 0.0")
            uiAutomation.executeShellCommand("settings put global transition_animation_scale 0.0")
            uiAutomation.executeShellCommand("settings put global animator_duration_scale 0.0")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

object IdlingResource {
    private const val RESOURCE = "GLOBAL"
    val idlingResource = CountingIdlingResource(RESOURCE)
}