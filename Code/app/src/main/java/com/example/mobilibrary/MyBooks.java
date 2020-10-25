package com.example.mobilibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MyBooks extends AppCompatActivity {
    ListView bookView;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookList;
    Button addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mybooks);

        addButton = findViewById(R.id.add_button);
        bookView = findViewById(R.id.book_list);
        bookList = new ArrayList<Book>();

        bookAdapter = new customBookAdapter(this, bookList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MyBooks.this, AddBookFragment.class);
                startActivityForResult(addIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bookBundle = data.getExtras();
                Book new_book = (Book) bookBundle.get("new book");
                bookAdapter.add(new_book);
                bookAdapter.notifyDataSetChanged();
            }
        }
    }
    // userBookList
}