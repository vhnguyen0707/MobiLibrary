package com.example.mobilibrary;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */

@RunWith(AndroidJUnit4.class)
public class MyBooksTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyBooks> rule =
            new ActivityTestRule<>(MyBooks.class, true, true);

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
     *Opens MyBookS activity and then tries to open addBookFragment
     */
    @Test
    public void checkActivities() {
// Asserts that the current activity is MyBooks and if it will switch to addBookFragment. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong Activity", AddBookFragment.class);
    }

    /**
     *Opens addBookFragment and tries to add a new book (title, author, ISBN)
     */
    @Test
    public void addActivity() {
// Asserts that the current activity is MyBooks and if it will switch to addBookFragment. Otherwise, show “Wrong Activity”
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong Activity", AddBookFragment.class);
        solo.enterText((EditText) solo.getView(R.id.book_title), "Harry Potter");
        solo.enterText((EditText) solo.getView(R.id.book_author), "J.K Rowling");
        solo.enterText((EditText) solo.getView(R.id.book_isbn), "1234 5678");
        solo.clickOnButton("confirm");
        MyBooks books = (MyBooks) solo.getCurrentActivity();
        final ListView bookView = books.bookView; // Get the listview
        Book book = (Book) bookView.getItemAtPosition(0); // Get item from first position
        assertEquals("Harry Potter", book.getTitle());
        assertEquals("J.K Rowling", book.getAuthor());
        assertEquals(12345678, book.getISBN());
    }

    /**
     *Opens addBookFragment and tries to add a new book, but with empty fields and checks for set errors.
     */
    @Test
    public void addActivityEmpty() {
// Asserts that the current activity is MyBooks and if it will switch to addBookFragment. Otherwise, show “Wrong Activity”
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong Activity", AddBookFragment.class);
        solo.clickOnButton("confirm");
        solo.waitForText("Please insert book title!", 1, 2000); // wait for error text
        solo.waitForText("Please insert book author!", 1, 2000); // you will wait untill the screen is entirely displayed.
        solo.waitForText("Please insert book ISBN!", 1, 2000); // you will wait untill the screen is entirely displayed.
        assertTrue(solo.searchText("Please insert book title!"));
        assertTrue(solo.searchText("Please insert book author!"));
        assertTrue(solo.searchText("Please insert book ISBN!"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
