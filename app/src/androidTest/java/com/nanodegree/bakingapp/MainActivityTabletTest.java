package com.nanodegree.bakingapp;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.nanodegree.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


@LargeTest
@RunWith(AndroidJUnit4.class)
class MainActivityTabletTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // This test only works with tablets
    @Test
    public void recipeActivityTabletTest() {
        onView(ViewMatchers.withId(R.id.recipe_tablet)).check(matches(isDisplayed()));
    }

}
