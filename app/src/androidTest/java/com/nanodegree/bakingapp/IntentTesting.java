package com.nanodegree.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.activities.MainActivity;
import com.nanodegree.bakingapp.activities.PreparationActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntentTesting {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void intentTest() {

        // Recyclerview scroll to position
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(4));

        // Perform Recyclerview click on item at position
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if intent (RecipeActivity to RecipeDetailsActivity) has RECIPE_INTENT_EXTRA
        intended(hasExtraWithKey(AppUtils.RECIPE_INTENT_EXTRA));

        // Perform click action on start cooking button
        onView(withId(R.id.btn_preparation)).perform(click());

        // Check if intent (RecipeDetailsActivity to CookingActivity) has RECIPE_INTENT_EXTRA
        intended(hasComponent(PreparationActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
