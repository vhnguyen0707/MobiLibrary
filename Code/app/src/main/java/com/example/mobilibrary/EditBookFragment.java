package com.example.mobilibrary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.mobillibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    ImageView photo;
    FloatingActionButton editImageButton;
    FloatingActionButton deleteImageButton;

    FloatingActionButton backButton;
    FloatingActionButton scanButton;
    Button confirmButton;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate (@Nullable Bundle SavedInstances){
        super.onCreate(SavedInstances);
        setContentView(R.layout.layout_edit_book_fragment);

        // photo option is separate user story, will come back to it
        title = findViewById(R.id.edit_title);
        author = findViewById(R.id.edit_author);
        ISBN = findViewById(R.id.edit_isbn);
        confirmButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_to_view_button);
        scanButton = findViewById(R.id.edit_scan_button);
        confirmButton = findViewById(R.id.confirm_button);
        photo = findViewById(R.id.image);
        editImageButton = findViewById(R.id.edit_image_button);
        deleteImageButton = findViewById(R.id.delete_image_button);


        mRequestQueue = Volley.newRequestQueue(this);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                PackageManager.PERMISSION_GRANTED); //Request permission to use Camera

        if (getIntent() == null) {
            finish();
        }

        // fill fields
        final Book book = (Book) getIntent().getSerializableExtra("edit");
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        ISBN.setText(String.valueOf(book.getISBN()));
        photo.setImageBitmap(book.getImage());


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
                    Long isbn = Long.parseLong(stringISBN);
                    if (!nullPhoto()) {
                        BitmapDrawable drawable = (BitmapDrawable) photo.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        book.setImage(bitmap);
                    } else {
                        book.setImage(null);
                    }
                    book.setTitle(title.getText().toString());
                    book.setAuthor(author.getText().toString());
                    book.setISBN(isbn);
                    Intent editIntent = new Intent();
                    editIntent.putExtra("edited", book);
                    setResult(RESULT_OK, editIntent);
                    finish();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ScanButton(view);
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo.setImageBitmap(null);
            }
        });

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int pic_id = 2;
                startActivityForResult(camera_intent, pic_id);
            }
        });
    }

    private void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
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
        if (validateISBN.isEmpty() == true || validateISBN.length() != 13) {
            ISBN.setError("Required: Book ISBN!");
            validation = false;
        }
        return validation;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Bitmap book_photo = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(book_photo);
        } else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (intentResult != null) { //scanner got a result
                if (intentResult.getContents() == null) { //scanner worked, but was not able to get data
                    System.out.println("scanner worked, but not able to get data");
                    Toast toast = Toast.makeText(this, "Unable to obtain data from barcode",
                            Toast.LENGTH_SHORT); // used to display error message
                    toast.show();
                } else {
                    //got ISBN
                    //Use the ISBN to search through Google Books API to find the author, and title.
                    String isbn = intentResult.getContents();
                    ISBN.setText(isbn);

                    //Check if connected to internet
                    boolean isConnected = isNetworkAvailable();
                    if (!isConnected) {
                        System.out.println("Check Internet Connection");
                        Toast.makeText(getApplicationContext(), "Please check Internet conncetion", Toast.LENGTH_LONG).show(); //Popup message for user
                        return;
                    }

                    final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"; //base url
                    Uri uri = Uri.parse(url + isbn);
                    Uri.Builder builder = uri.buildUpon();

                    parseJson(builder.toString()); //get results from webpage
                }
            }
        }
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
                                    editAuthor = authors.getString(0) + "," + authors.getString(1);
                                }
                                System.out.println("author: " + author);
                                author.setText(editAuthor);

                            } catch (Exception e) { //the book info in database does not contain a title or author
                                if (editTitle == "") {
                                    title.setText("");
                                    title.setError("Title not found");
                                } else {
                                    author.setText("");
                                    author.setError("Author not found");
                                }
                            }

                        } catch (JSONException e) { //error trying to get database info
                            e.printStackTrace();
                            title.setText("");
                            title.setError("Title not found");
                            author.setText("");
                            author.setError("Author not found");
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
