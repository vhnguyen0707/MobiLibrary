package com.example.mobilibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobilibrary.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class customBookAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> books;
    private Context context;

    /**
     * Used as a adapter for an array of objects
     * @param context
     * @param books
     */
    public customBookAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context,0,books);
        this.books = books;
        this.context = context;
    }

    /**
     * Create a book item in the listView with the book information (title, author and isbn)
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        Book book = books.get(position);

        TextView bookTitle = view.findViewById(R.id.my_book_title);
        TextView bookAuthor = view.findViewById(R.id.my_book_author);
        TextView bookISBN= view.findViewById(R.id.my_book_ISBN);

        String ISBN = String.valueOf(book.getISBN());

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(ISBN);
        return view;
    }
}
