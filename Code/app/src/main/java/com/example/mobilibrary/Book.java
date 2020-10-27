package com.example.mobilibrary;

import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private int ISBN;
    private String author;
    private String status;
    // location variable?
    // picture variable?

    public Book(String title, int ISBN, String author, String status){
        this.title = title;
        this.ISBN = ISBN;
        this.author = author;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
