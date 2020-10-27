package com.example.mobilibrary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.io.Serializable;

public class AddBookFragment extends AppCompatActivity {
    EditText newTitle;
    EditText newAuthor;
    EditText newIsbn;
    ImageView newImage;
    Button confirmButton;
    Button backButton;
    Button cameraButton;
    Intent addIntent;
    boolean inputsGood;
    String bookStatus;

    private RequestQueue mRequestQueue;

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

        mRequestQueue = Volley.newRequestQueue(this);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED); //Request permission to use Camera

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
                String ISBN = newIsbn.getText().toString();
                ISBN = ISBN.replaceAll(" ", "");
                checkInputs(bookTitle, bookAuthor,ISBN);
                if (inputsGood) {
                    int bookIsbn = Integer.parseInt(ISBN);
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
                ScanButton(v);
            }
        });
    }

    public void ScanButton(View view) { //When camera button is clicked
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
        //System.out.println("scan clicked");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) { //scanner got a result
            if (intentResult.getContents() == null) { //scanner worked, but was not able to get data
                System.out.println("scanner worked, but not able to get data");
                Toast toast = Toast.makeText(this, "Unable to obtain data from barcode", Toast.LENGTH_SHORT); //used ot display error message
                toast.show();
            }
            else {
                //got ISBN
                //Use the ISBN to search through Google Books API to find the author, and title.
                String isbn = intentResult.getContents();
                newIsbn.setText(isbn);

                //Maybe add in a check to see if they are connected to internet? Or do we assume they are already connected

                final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"; //base url
                Uri uri = Uri.parse(url + isbn);
                Uri.Builder builder = uri.buildUpon();

                parseJson(builder.toString()); //get results from webpage

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void parseJson(String key) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() { //volley stuff
                    @Override
                    public void onResponse(JSONObject response) {
                        String title = "";
                        String author = "";

                        try {
                            System.out.println("RESPPONSSEEE: " + response);

                            JSONArray items = response.getJSONArray("items");
                            JSONObject item = items.getJSONObject(0);
                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                            try {
                                title = volumeInfo.getString("title");
                                System.out.println("title: " + title);
                                newTitle.setText(title);

                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                if (authors.length() == 1) {
                                    author = authors.getString(0);
                                } else { //if there are multiple authors
                                    author = authors.getString(0) + "|" + authors.getString(1); //haven't tested with multiple authors yet
                                }
                                System.out.println("author: " + author);
                                newAuthor.setText(author);

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

    public void checkInputs(String title, String Author, String ISBN){
        if(title.isEmpty()){
            newTitle.setError("Please insert book title!");
            inputsGood = false;
        } else if(Author.isEmpty()){
            newAuthor.setError("Please insert book author!");
            inputsGood = false;
        } else if(ISBN.isEmpty()){
            newIsbn.setError("Please insert book ISBN!");
            inputsGood = false;
        }
    }
}


