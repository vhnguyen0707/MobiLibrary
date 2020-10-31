package com.example.mobilibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobillibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MyBooks extends AppCompatActivity {
    ListView bookView;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookList;
    FloatingActionButton addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mybooks);

        addButton = findViewById(R.id.add_button);
        bookView = (ListView) findViewById(R.id.book_list);
        bookList = new ArrayList<Book>();

        bookAdapter = new customBookAdapter(this, bookList);
        bookView.setAdapter(bookAdapter);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Book new_book = (Book) Objects.requireNonNull(data.getExtras()).getSerializable("new book");
                bookAdapter.add(new_book);
                bookAdapter.notifyDataSetChanged();
            }
        }
    }

        if (requestCode == 1) {
            if (resultCode == 1) {
                // book needs to be deleted, intent has book to delete
                Book delete_book = (Book) data.getSerializableExtra("delete book");

                // find the book to delete and delete it
                for (int i = 0; 0 < bookAdapter.getCount(); i++) {
                    Book currentBook = bookAdapter.getItem(i) ;
                    if (delete_book.compareTo(currentBook) == 0){
                        bookAdapter.remove(currentBook);
                    }
                }

                bookAdapter.notifyDataSetChanged();
            }
            else if (resultCode == 2) {
                // book was edited update data set
                Book edited_book = (Book) data.getSerializableExtra("edited book");

                // find the book to edit and edit it
                for (int i = 0; 0 < bookList.size(); i++) {
                    Book currentBook = bookList.get(i) ;
                    if (edited_book.compareTo(currentBook) == 0){
                        currentBook.setTitle(edited_book.getTitle());
                        currentBook.setAuthor(edited_book.getAuthor());
                        currentBook.setISBN(edited_book.getISBN());
                        currentBook.setImage(edited_book.getImage());
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        }
    }
    // userBookList
}


