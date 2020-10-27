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

public class MyBooks extends AppCompatActivity {
    ListView bookView;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookList;
    FloatingActionButton addButton;

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
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bookBundle = data.getExtras();
                assert bookBundle != null;
                Book new_book = (Book) bookBundle.getSerializable("new book");
                bookAdapter.add(new_book);
                bookAdapter.notifyDataSetChanged();
            }
        }
    }
    // userBookList
}


