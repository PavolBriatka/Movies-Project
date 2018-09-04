package com.briatka.pavol.skymoviesproject.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.RadioGroup;

import com.briatka.pavol.skymoviesproject.R;
import com.briatka.pavol.skymoviesproject.adapters.MovieAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        searchView = mActivity.findViewById(R.id.search_view);
        recyclerView = mActivity.findViewById(R.id.main_recycler_view);
        radioGroup = mActivity.findViewById(R.id.options);


    }

    @Test
    public void viewsCreated() {

        Assert.assertNotNull(searchView);
        Assert.assertNotNull(recyclerView);
        Assert.assertNotNull(radioGroup);
    }

    @Test
    public void viewsVisible() {
        Espresso.onView(withId(R.id.search_view)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.main_recycler_view)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.options)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void radioButtonsAreClickable() {
        Espresso.onView(withId(R.id.title_option)).check(ViewAssertions.matches(isChecked()));
        Espresso.onView(withId(R.id.genre_option)).perform(click()).check(ViewAssertions.matches(isChecked()));
        Espresso.onView(withId(R.id.title_option)).perform(click()).check(ViewAssertions.matches(isChecked()));

    }

    @Test
    public void scrollsToLastPosition() {
        int position = recyclerView.getAdapter().getItemCount() - 1;
        Espresso.onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(position)).check(ViewAssertions.matches(isDisplayed()));

    }

    @Test
    public void showsLastViewHolder() {
        Espresso.onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.scrollToHolder(isLast())).check(ViewAssertions.matches(isDisplayed()));

    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    private static Matcher<MovieAdapter.ViewHolder> isLast() {
        return new TypeSafeMatcher<MovieAdapter.ViewHolder>() {
            @Override
            protected boolean matchesSafely(MovieAdapter.ViewHolder item) {
                return item.getLastItem();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("the last item");
            }
        };
    }


}