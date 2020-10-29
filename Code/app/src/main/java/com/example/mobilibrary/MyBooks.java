package com.example.mobilibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobillibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MyBooks extends AppCompatActivity {
    ListView bookView;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookList;
    ArrayList<Book> tempBookList;
    FloatingActionButton addButton;
    Spinner statesSpin;
    private static final String[] states = new String[]{"Owned", "Requested", "Accepted", "Borrowed"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mybooks);

        addButton = findViewById(R.id.add_button);
        bookView = (ListView) findViewById(R.id.book_list);
        bookList = new ArrayList<Book>();
        tempBookList = new ArrayList<Book>();

        bookAdapter = new customBookAdapter(this, bookList);
        bookView.setAdapter(bookAdapter);

        statesSpin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> SpinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        SpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpin.setAdapter(SpinAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MyBooks.this, AddBookFragment.class);
                startActivityForResult(addIntent, 0);
            }
        });

        bookView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = bookList.get(i);
                Intent viewBook = new Intent(MyBooks.this, BookDetailsFragment.class);
                viewBook.putExtra("view book", book);
                // viewBook.putExtra("book owner", user.getusername());   // need to get user somehow, add User variable to this class
                startActivityForResult(viewBook, 1);
            }


        });

        statesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state = (String) adapterView.getItemAtPosition(i);
                DisplayBooks(state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Book new_book = (Book) Objects.requireNonNull(data.getExtras()).getSerializable("new book");
                bookAdapter.add(new_book);
                tempBookList.add(new_book);
                bookAdapter.notifyDataSetChanged();
            }
        }


        if (requestCode == 1) {
            if (resultCode == 1) {
                // book needs to be deleted, intent has book to delete
                Book delete_book = (Book) data.getSerializableExtra("delete book");

                // find the book to delete and delete it
                for (int i = 0; 0 < bookAdapter.getCount(); i++) {
                    Book currentBook = bookAdapter.getItem(i);
                    if (delete_book.compareTo(currentBook) == 0) {
                        tempBookList.remove(currentBook);
                        bookAdapter.remove(currentBook);
                    }
                }

                bookAdapter.notifyDataSetChanged();
            } else if (resultCode == 2) {
                // book was edited update data set
                Book edited_book = (Book) data.getSerializableExtra("edited book");

                // find the book to edit and edit it
                for (int i = 0; 0 < bookAdapter.getCount(); i++) {
                    Book currentBook = bookAdapter.getItem(i);
                    if (edited_book.compareTo(currentBook) == 0) {
                        currentBook.setTitle(edited_book.getTitle());
                        currentBook.setAuthor(edited_book.getAuthor());
                        currentBook.setISBN(edited_book.getISBN());
                        // photo can be edited, but that is its own User Story
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        }
    }

    // userBookList
    void DisplayBooks(String state) {
        state = state.toLowerCase();
        switch (state) {
            case "requested":
                for (Book book : tempBookList) {
                    if (book.getStatus() != state) {
                        if (bookList.contains(book) == true) {
                            bookAdapter.remove(book);
                        }
                    }
                }
                bookAdapter.notifyDataSetChanged();
                break;
            case "owned":
                Log.d("sooraj", "owned is pressed");
                for (Book book : tempBookList) {
                    Log.d("sooraj", "booklist doesnt contain a book, add it");
                    bookAdapter.add(book);
                }
                bookAdapter.notifyDataSetChanged();
                break;
            case "accepted":
                for (Book book : tempBookList) {
                    if (book.getStatus() != state) {
                        if (bookList.contains(book) == true) {
                            bookAdapter.remove(book);
                        }
                    }
                }
                bookAdapter.notifyDataSetChanged();
                break;

            case "borrowed":
                for (Book book : tempBookList) {
                    if (book.getStatus() != state) {
                        if (bookList.contains(book) == true) {
                            bookAdapter.remove(book);
                        }
                    }
                }
                bookAdapter.notifyDataSetChanged();
                break;
        }
    }
}




