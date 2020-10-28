package com.example.mobilibrary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditBookFragment extends AppCompatActivity {
    EditText title;
    EditText author;
    EditText ISBN;
    // ImageView photo; photo option is seperate user story
    Button backButton;
    Button scanButton;
    Button confirmButton;
    Book book;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate (@Nullable Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.layout_edit_book_fragment);

        // photo option is separate user story, will come back to it
        title = findViewById(R.id.edit_title);
        author = findViewById(R.id.edit_author);
        ISBN = findViewById(R.id.edit_isbn);
        backButton = findViewById(R.id.back_to_view_button);
        scanButton = findViewById(R.id.edit_scan_button);

        mRequestQueue = Volley.newRequestQueue(this);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                PackageManager.PERMISSION_GRANTED); //Request permission to use Camera

        if (getIntent() == null) {
            finish();
        }

        // fill fields
        book = (Book) getIntent().getSerializableExtra("edit");
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        ISBN.setText(book.getISBN());

        // ignoring edit photo option for now, it is its own user story

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringISBN = ISBN.getText().toString().replaceAll(" ", "");

                // if input is valid, edit book and return it to parent activity
                if (validateInputs(title.getText().toString(), author.getText().toString(), stringISBN)) {
                    int isbn = Integer.parseInt(stringISBN);
                    book.setTitle(title.getText().toString());
                    book.setAuthor(author.getText().toString());
                    book.setISBN(isbn);
                    Intent editIntent = new Intent();
                    editIntent.putExtra("edited", book);
                    setResult(2, editIntent);
                    finish();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanButton(view);
            }
        });
    }

    private void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
        // System.out.println("scan clicked");  // for testing/debugging
    }

    private boolean validateInputs(String validateTitle, String validateAuthor, String validateISBN){
        boolean validation = true;
        if (validateTitle.isEmpty() == true) {
            title.setError("Required: Book Title!");
            validation = false;
        }
        if (validateAuthor.isEmpty() == true) {
            author.setError("Required: Book Author!");
            validation = false;
        }
        if (validateISBN.isEmpty() == true) {
            ISBN.setError("Required: Book ISBN!");
            validation = false;
        }
        return validation;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) { //scanner got a result
            if (intentResult.getContents() == null) { //scanner worked, but was not able to get data
                System.out.println("scanner worked, but not able to get data");
                //add what to do if this happens (tell user the barcode is not valid?)
            }
            else {
                //got ISBN
                //Use the ISBN to search through Google Books API to find the author, and title.
                String isbn = intentResult.getContents();
                ISBN.setText(isbn);

                //Maybe add in a check to see if they are connected to internet? Or do we assume they are already connected

                final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"; //base url
                Uri uri = Uri.parse(url + isbn);
                Uri.Builder builder = uri.buildUpon();

                parseJson(builder.toString()); //get results from webpage

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

        // send data back to parent activity
        String stringISBN = ISBN.getText().toString().replaceAll(" ", "");
        int isbn = Integer.parseInt(stringISBN);
        book.setTitle(title.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setISBN(isbn);
        Intent editIntent = new Intent();
        editIntent.putExtra("edited", book);
        setResult(2, editIntent);
        finish();
    }

    private void parseJson(String key) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() { //volley stuff
                    @Override
                    public void onResponse(JSONObject response) {
                        String editTitle = "";
                        String editAuthor = "";

                        try {
                            System.out.println("RESPPONSSEEE: " + response);

                            JSONArray items = response.getJSONArray("items");
                            JSONObject item = items.getJSONObject(0);
                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                            try {
                                editTitle = volumeInfo.getString("title");
                                System.out.println("title: " + editTitle);
                                title.setText(editTitle);

                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                if (authors.length() == 1) {
                                    editAuthor = authors.getString(0);
                                } else { //if there are multiple authors
                                    editAuthor = authors.getString(0) + "|" + authors.getString(1); //haven't tested with multiple authors yet
                                }
                                System.out.println("author: " + author);
                                author.setText(editAuthor);

                            } catch (Exception e) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }
}