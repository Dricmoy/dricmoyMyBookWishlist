package com.example.dricmoy_mybookwishlist;

import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WishList {
    private final String name;
    private final ArrayList<Book> books;

    // Constructor
    public WishList(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    // Add a book to the wishlist
    public void addBook(Book book) {
        books.add(book);
    }

    // Remove a book from the wishlist
    public void removeBook(Book book) {
        books.remove(book);
    }

    // Get all books
    public ArrayList<Book> getBooks() {
        return books;
    }

    // Get wishlist name
    public String getName() {
        return name;
    }

    // Sort books by status (read first)
    public void sortBooksByStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(books, Comparator.comparing(Book::getStatus)); // Example implementation
        }
    }

    // Sort books by title
    public void sortBooksByTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(books, Comparator.comparing(Book::getTitle)); // Example implementation
        }
    }


}
