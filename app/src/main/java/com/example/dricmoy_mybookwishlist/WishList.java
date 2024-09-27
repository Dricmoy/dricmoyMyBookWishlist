package com.example.dricmoy_mybookwishlist;

import java.util.ArrayList; // Importing ArrayList for managing a dynamic list of books

/**
 * WishList represents a collection of books that a user wishes to read.
 */
public class WishList implements WishListInterface {
    private final String name; // The name of the wishlist
    private final ArrayList<Book> books; // A list to store the books in the wishlist

    // Constructor to initialize the wishlist with a name
    public WishList(String name) {
        this.name = name; // Set the wishlist name
        this.books = new ArrayList<>(); // Initialize the books list
    }

    // Method to add a book to the wishlist
    public void addBook(Book book) {
        books.add(book); // Add the book to the books list
    }

    // Method to remove a book from the wishlist
    public void removeBook(Book book) {
        books.remove(book); // Remove the book from the books list
    }

    // Method to get all books in the wishlist
    public ArrayList<Book> getBooks() {
        return books; // Return the list of books
    }

    // Method to get the name of the wishlist
    public String getName() {
        return name; // Return the name of the wishlist
    }
}
