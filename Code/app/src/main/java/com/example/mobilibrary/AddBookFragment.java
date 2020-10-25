package com.example.mobilibrary;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class AddBookFragment extends AppCompatActivity {
        EditText newTitle;
        EditText newAuthor;
        EditText newIsbn;
        ImageView newImage;
        Button confirmButton;
        Button backButton;
        Button cameraButton;
        Intent cameraIntent;
        Intent addIntent;
        Intent inputIntent;
        boolean inputsGood;
        String bookStatus;

        @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_add_book_fragment);

            inputsGood = true;
            newTitle = findViewById(R.id.book_title);
            newAuthor = findViewById(R.id.book_author);
            newIsbn = findViewById(R.id.book_isbn);
            newImage = findViewById(R.id.book_image);
            confirmButton = findViewById(R.id.confirm_button);
            backButton = findViewById(R.id.back_button);
            cameraButton = findViewById(R.id.camera_button);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bookTitle = newTitle.getText().toString();
                    String bookAuthor = newAuthor.getText().toString();
                        checkInputs(bookTitle, bookAuthor);
                    if (inputsGood) {
                        int bookIsbn = Integer.parseInt(String.valueOf(newIsbn));
                        bookStatus = "available";
                        Book newBook = new Book(bookTitle, bookIsbn, bookAuthor, bookStatus);
                        addIntent = new Intent();
                        addIntent.putExtra("new book", (Serializable) newBook);
                        setResult(RESULT_OK, addIntent);
                        finish();
                    }
                }
            });

            cameraButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    cameraIntent = new Intent(AddBookFragment.this,scan.class);
                    startActivity(cameraIntent);
                    inputIntent = getIntent();
                    newImage.setImageIcon(Icon.createWithContentUri("Image"));
                    newTitle.setText(inputIntent.getStringExtra("Title"));
                    newAuthor.setText(inputIntent.getStringExtra("Author"));
                    newIsbn.setText(inputIntent.getIntExtra("Isbn",0));
                }
            });
    }

    public void checkInputs(String title, String Author){
            if(title.isEmpty()){
                newTitle.setError("Please insert book title!");
                inputsGood = false;
            } else if(Author.isEmpty()){
                newAuthor.setError("Please insert book author!");
                inputsGood = false;
            } else if(newIsbn.getText().toString().isEmpty()){
                newIsbn.setError("Please insert book ISBN!");
                inputsGood = false;
            }
    }
}