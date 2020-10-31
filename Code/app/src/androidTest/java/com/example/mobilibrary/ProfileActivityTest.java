package com.example.mobilibrary;

import android.app.Activity;

import androidx.constraintlayout.solver.PriorityGoalRow;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.mobilibrary.Activity.ProfileActivity;
import com.example.mobilibrary.DatabaseController.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test class for ProfileActivity. All the UI tests are written here.
 *  Robotium test framework is used.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks that any profile that is not the user's own hides the edit button and all other related views.
     */
    @Test
    public void checkDifferentUserVisibility() {
        solo.assertCurrentActivity("Wrong activity!", ProfileActivity.class);
        User currentUser = new User("Test1", "test1@gmail.com", "Tester1", "1234567890");
        User profileUser = new User("Test2", "test2@gmail.com", "Tester2", "2345678901");

    }

    /**
     * Checks that the user's own profile shows appropriate visibility at different times.
     * e.g. the edit button on start, the edit views & cancel/confirm buttons on click,
     *      and back to how it looked before on confirm or cancel (if input validated).
     */
    @Test
    public void checkSameUserVisibility() {
        solo.assertCurrentActivity("Wrong activity!", ProfileActivity.class);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
