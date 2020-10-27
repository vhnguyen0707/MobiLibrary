package com.example.mobilibrary;

import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {
    private String title;
    private int ISBN;
    private String author;
    private String status;
    private User owner;
    // location variable?
    // picture variable?

    public Book(String title, int ISBN, String author, String status, User owner){
        this.title = title;
        this.ISBN = ISBN;
        this.author = author;
        this.status = status;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Compares a book the book passed in the parameter by comparing their ISBNs,
     * if they are the same return 0, otherwise return 1
     * @param book
     * @return int value, 0 if the books are the same, 1 otherwise
     */
    @Override
    public int compareTo(Book book) {
        if (ISBN == book.getISBN()){
            return 0;
        }
        else {
            return 1;
        }
    }
}