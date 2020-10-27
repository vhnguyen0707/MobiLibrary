package com.example.mobilibrary;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

@RunWith (AndroidJUnit4.class)
public class BookDetailsTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MyBooks> rule =
            new ActivityTestRule<>(MyBooks.class, true, true);

    /**
     * Sets up list with at least one book to test one
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // establish an instrument
        solo = new Solo(InstrumentationRegistry.getInstrumentation(). rule.getActivity());

        // establish a book to work on
        User owner = new User("username", "email@example.com", "First Last", "123-123-1234");
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.enterText((EditText) solo.getView(R.id.book_title), "Song of the Lioness");
        solo.enterText((EditText) solo.getView(R.id.book_author), "Tamora Pierce");
        solo.enterText((EditText) solo.getView(R.id.book_isbn), "1234 5678");
        solo.clickOnButton("confirm");
    }

    /**
     * Asserts that the current activity switches from MyBooks to BookDetailsFragment on
     * clicking a list item, if this fails it will show "Wrong Activity"
     */
    @Test
    public void checkActivityActivation() {
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        solo.clickInList(1); // THIS IS NOT CORRECT IM TRYING TO FIGURE OUT HOW TO CLICK ON THIS DAMN THING
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
    }

    /**
     * Asserts that by clicking on the back button the activity will switch back to MyBooks and
     * nothing will change in the book list
     */
    @Test
    public void backButtonTest() {
        // go from MyBooks to viewing a book
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        ArrayList<TextView> book = solo.clickInList(1);
        solo.clickOnText("Song of the Lioness");
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);

        // leave book details without changing anything
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.back_to_books_button));
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        // check that nothing was changed since conception
        MyBooks books = (MyBooks) solo.getCurrentActivity();
        final ListView bookView = books.bookView;
        assertEquals(1, bookView.getCount());
        Book book = (Book) bookView.getItemAtPosition(0);
        assertEquals("Song of the Lioness", book.getTitle());
        assertEquals("Tamora Pierce", book.getAuthor());
        assertEquals(12345678, book.getISBN());

    }

    /**
     * Asserts that the current activity switches from BookDetailsFragment to EditBookFragment on
     * clicking the edit button, if this fails it will show "Wrong Activity"
     */
    @Test
    public void editBookTest() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
    }

    /**
     * Asserts that by clicking on the delete button the activity will switch back to MyBooks
     * and the book that was deleted will no longer be in the list
     */
    @Test
    public void deleteBookTest() {
        // go from MyBooks to viewing a book
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        ArrayList<TextView> book = solo.clickInList(1);
        solo.clickOnText("Song of the Lioness");
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);

        // delete the book
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.delete_button));
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        // check that the only book formerly in list is deleted from the data list
        MyBooks books = (MyBooks) solo.getCurrentActivity();
        final ListView bookView = books.bookView;
        assertEquals(0, bookView.getCount());
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
