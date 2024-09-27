package com.example.dricmoy_mybookwishlist;

import java.util.ArrayList;

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

}
