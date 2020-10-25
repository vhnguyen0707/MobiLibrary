package com.example.mobilibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class customBookAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> books;

    public customBookAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context,0,books);
        this.context = context;
        this.books = books;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        TextView bookTitle = view.findViewById(R.id.my_book_title);
        TextView bookAuthor = view.findViewById(R.id.my_book_author);
        TextView bookISBN= view.findViewById(R.id.my_book_title);

        Book book = books.get(position);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getISBN());
        return view;
    }
}
