package com.example.mobilibrary;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {
    private static int nextID = 0;

    private String title;
    private long ISBN;
    private String author;
    private String status;
    // private User owner;
    // location variable?
    private transient Bitmap image;
    private int id;


    public Book(String title, long ISBN, String author, String status, Bitmap image) { // User user){
        this.id = nextID;
        this.title = title;
        this.ISBN = ISBN;
        this.author = author;
        this.status = status;

        this.image = image;

        //this.owner = user;
        nextID++;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
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
    
    /*public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }*/

    public Bitmap getImage() {
        return image;
    }


    /*public void setOwner(User owner) {
        this.owner = owner;
    }*/

    public void setImage(Bitmap image) {
        this.image = image;
    }


    /**
     * Compares a book the book passed in the parameter by comparing their IDs,
     * if they are the same return 0, otherwise return 1
     * @param book
     * @return int value, 0 if the books are the same, 1 otherwise
     */
    @Override
    public int compareTo(Book book){
        if (this.id == book.getId()){
            return 0;
        }
        else {
            return 1;
        }
    }
}