package com.example.mobilibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobillibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class BookDetailsFragment extends AppCompatActivity {
    TextView title;
    TextView author;
    TextView owner;
    TextView ISBN;
    TextView authorTitle;
    TextView ownerTitle;
    TextView isbnTitle;
    FloatingActionButton backButton;
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;
    String req_users [] = {"Natalia", "Chloe", "Kimberly", "Jill", "Nguyen", "Sooraj"}; // sort of a placeholder this one, need to replace with actual requesting users once we implement it
    Button detailsBtn;
    Button requestsBtn;
    TextView[] requestAssets;
    ImageView photo;
    ListView reqList;
    ArrayAdapter<String> reqAdapter;
    ArrayList<String> reqDataList;

    @Override
    protected void onCreate (@Nullable Bundle SavedInstances) {
        super.onCreate(SavedInstances);
        setContentView(R.layout.layout_book_details_fragment);

        // photo option is separate user story, will come back to it
        title =  findViewById(R.id.view_title);
        author = findViewById(R.id.view_author);
        owner = findViewById(R.id.view_owner);
        ISBN = findViewById(R.id.view_isbn);
        backButton = findViewById(R.id.back_to_books_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);
        photo = findViewById(R.id.imageView);
        detailsBtn = findViewById(R.id.detailsBtn);
        requestsBtn = findViewById(R.id.reqBtn);
        reqList = findViewById(R.id.reqListView);
        authorTitle = findViewById(R.id.view_author_title);
        ownerTitle = findViewById(R.id.view_owner_title);
        isbnTitle = findViewById(R.id.view_isbn_title);
        requestAssets = new TextView[]{title, author, authorTitle, owner, ownerTitle,ISBN, isbnTitle, };
        reqDataList =new ArrayList<>();
        for (String user: req_users){
            reqDataList.add(user + "has requested your book");
        }
        reqAdapter =  new ArrayAdapter<String>(this,R.layout.req_custom_list, R.id.textView, reqDataList);
        reqList.setAdapter(reqAdapter);
        reqList.setVisibility(View.GONE);
        // get book from intent
        if (getIntent() == null) {
            finish();
        }

        final Book viewBook = (Book) getIntent().getSerializableExtra("view book");

        System.out.println("Book name: " +  viewBook.getTitle());

        title.setText(viewBook.getTitle());
        author.setText(viewBook.getAuthor());



        // owner.setText(viewBook.getOwner().getUsername());
        ISBN.setText(viewBook.getISBN());
        Bitmap bitmap;
        if (viewBook.getImage() != null) {
            bitmap = BitmapFactory.decodeByteArray(viewBook.getImage(), 0,
                    viewBook.getImage().length);
        } else {
            bitmap = null;
        }
        photo.setImageBitmap(bitmap);
        photo.setImageBitmap(bitmap);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // only return things from this intention if something was edited
                if ((title.getText().toString() != viewBook.getTitle()) ||
                        (author.getText().toString() != viewBook.getAuthor()) ||
                        (ISBN.equals(viewBook.getISBN()))){
                    viewBook.setTitle(title.getText().toString());
                    viewBook.setAuthor(author.getText().toString());

                    String stringISBN = ISBN.getText().toString().replaceAll(" ", "");
                    viewBook.setISBN(stringISBN);

                    if (!nullPhoto()) {
                        Bitmap bitmap = ((BitmapDrawable)photo.getDrawable()).getBitmap();
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        byte[] bookImage = outStream.toByteArray();
                        viewBook.setImage(bookImage);
                    } else {
                        viewBook.setImage(null);
                    }

                    Intent editedIntent = new Intent();
                    editedIntent.putExtra("edited book", viewBook);
                    setResult(2, editedIntent);
                }
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book deleteBook = viewBook;
                Intent deleteIntent = new Intent();
                deleteIntent.putExtra("delete book", deleteBook);
                setResult(1, deleteIntent);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book editBook = viewBook;
                Intent editIntent = new Intent(BookDetailsFragment.this, EditBookFragment.class);
                editIntent.putExtra("edit", editBook);
                startActivityForResult(editIntent, 2);
            }
        });

        requestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                for (TextView asset : requestAssets) {
                    asset.setVisibility(View.GONE);
                }
                reqList.setVisibility(View.VISIBLE);


            }
        });
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TextView asset : requestAssets) {
                    asset.setVisibility(View.VISIBLE);
                }
                reqList.setVisibility(View.GONE);
            }
        });
    }

    private boolean nullPhoto () {
        Drawable drawable = photo.getDrawable();
        BitmapDrawable bitmapDrawable;
        if (!(drawable instanceof BitmapDrawable)) {
            bitmapDrawable = null;
        } else {
            bitmapDrawable = (BitmapDrawable) photo.getDrawable();
        }
        return drawable == null || bitmapDrawable.getBitmap() == null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                // pass edited book back to parent activity
                Book editedBook = (Book) data.getSerializableExtra("edited");
                title.setText(editedBook.getTitle());
                author.setText(editedBook.getAuthor());
                // owner.setText(editedBook.getOwner().getUsername());
                ISBN.setText(String.valueOf(editedBook.getISBN()));
                Bitmap bitmap; // used for null case
                if (editedBook.getImage() != null) {
                    bitmap = BitmapFactory.decodeByteArray(editedBook.getImage(), 0,
                            editedBook.getImage().length);
                } else {
                    bitmap = null;
                }
                photo.setImageBitmap(bitmap);
            }
        }
    }
}