package com.example.pw_78

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_validURL() {
        onView(withId(R.id.url_of_image)).perform(typeText("https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_3x4.jpg"))
        onView(withId(R.id.load_image_button)).perform(click())

        Thread.sleep(5000)

        onView(withId(R.id.image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_invalidURL() {
        onView(withId(R.id.url_of_image)).perform(typeText("https://shorturl.at/wgBfdsafdsfdsuC"))
        onView(withId(R.id.load_image_button)).perform(click())

        Thread.sleep(5000)

        onView(withId(R.id.image_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_emptyURL() {
        onView(withId(R.id.url_of_image)).perform(typeText(""))
        onView(withId(R.id.load_image_button)).perform(click())

        Thread.sleep(5000)

        onView(withId(R.id.image_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_showPhoto() {
        onView(withId(R.id.url_of_image)).perform(typeText("https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_3x4.jpg"))
        onView(withId(R.id.only_load_image_button)).perform(click())

        Thread.sleep(5000)

        onView(withId(R.id.image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clearAll() {
        onView(withId(R.id.clear_all_button)).perform(click())
        onView(withId(R.id.image_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.url_of_image)).check(matches(withText("")))
    }
}