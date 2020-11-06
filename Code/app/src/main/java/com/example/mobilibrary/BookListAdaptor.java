package com.example.mobilibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class BookListAdaptor extends RecyclerView.Adapter<BookListAdaptor.MyViewHolder> {
    private List<String> mTitles;
    private List<String> mAuthors;
    private List<String> mISBNS;
    private List<String> mStatuses;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView author;
        public TextView isbn;
        public TextView status;

        public MyViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.book_title);
            author = (TextView) v.findViewById(R.id.book_author);
            isbn = (TextView) v.findViewById(R.id.book_isbn);
            status = (TextView) v.findViewById(R.id.book_status);
        }

    }


    public BookListAdaptor(List<String> titles, List<String> authors, List<String> isbns, List<String> statuses) {
        mTitles = titles;
        mAuthors = authors;
        mISBNS = isbns;
        mStatuses = statuses;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookListAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_rows, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mTitles.get(position));
        holder.author.setText(mAuthors.get(position));
        holder.isbn.setText(mISBNS.get(position));
        holder.status.setText(mStatuses.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTitles.size();
    }
}