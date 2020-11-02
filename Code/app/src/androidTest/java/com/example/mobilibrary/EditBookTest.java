package com.example.mobilibrary;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobillibrary.R;
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
public class EditBookTest {
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

        // view book's details
        ArrayList<TextView> book = solo.clickInList(1);
        solo.clickOnText("Song of the Lioness");
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);

    }

    /**
     * Asserts that the current activity switches from BookDetailsFragment to EditBookFragment on
     * clicking the edit button, if this fails it will show "Wrong Activity"
     */
    @Test
    public void checkActivityActivation() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
    }

    /**
     * Asserts that by clicking on the back button the activity will switch back to BookDetailsFragment and
     * nothing will change in the book list
     */
    @Test
    public void backButtonTest() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);

        // leave without changing anything
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
        solo.clickOnView(solo.getView(R.id.back_to_view_button));
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);

        // confirm that nothing has changed
        String title = solo.getView(R.id.view_title).toString();
        String author = solo.getView(R.id.view_author).toString();
        String isbn = solo.getView(R.id.view_isbn).toString().replaceAll(" ", "");
        int ISBN = Integer.parseInt(isbn);
        assertEquals("Song of the Lioness", title);
        assertEquals("Tamora Pierce", author);
        assertEquals(12345678, ISBN);
    }

    /**
     * Asserts that by clicking on confirm having changed fields the activity will switch back to MyBooks and that
     * field in that book will change
     */
    @Test
    public void editPositiveTest() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);

        // changing a field and leave
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
        solo.clearEditText((EditText) solo.getView(R.id.edit_title));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Circle of Magic");
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        // confirm that the field has changed
        MyBooks books = (MyBooks) solo.getCurrentActivity();
        ListView bookList = books.bookView;
        Book book = (Book) bookList.getItemAtPosition(0);
        assertNotEquals("Song of the Lioness", book.getTitle());
        assertEquals("Circle of Magic", book.getTitle());
        assertEquals("Tamora Pierce", book.getAuthor());
        assertEquals(12345678, book.getISBN());
    }

    /**
     * Asserts that by clicking on confirm not changing fields the activity will switch back to MyBooks and that
     * no fields in that book will change
     */
    @Test
    public void editNeutralTest() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);

        // leave without changing anything
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
        solo.clickOnButton("CONFIRM");
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);

        // confirm that nothing has changed
        MyBooks books = (MyBooks) solo.getCurrentActivity();
        ListView bookList = books.bookView;
        Book book = (Book) bookList.getItemAtPosition(0);
        assertEquals("Song of the Lioness", book.getTitle());
        assertEquals("Tamora Pierce", book.getAuthor());
        assertEquals(12345678, book.getISBN());
    }

    /**
     * Asserts that by clicking on confirm leaving (a) blank field(s) the activity will wait until the field(s)
     * are filled to switch back to MyBooks and that only changed fields in that book will change
     */
    @Test
    public void editNegativeTest() {
        solo.assertCurrentActivity("Wrong Activity", BookDetailsFragment.class);
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);

        // leave without changing anything
        solo.assertCurrentActivity("Wrong Activity", EditBookFragment.class);
        solo.clearEditText((EditText) solo.getView(R.id.edit_title));
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Please insert book title!", 1, 2000); // wait for error message
        assertTrue(solo.searchText("Please insert book title!"));
    }

    // scanner test?

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}

