package com.example.dricmoy_mybookwishlist;
import java.util.ArrayList;

public interface WishListInterface {
    void addBook(Book book);
    void removeBook(Book book);
    ArrayList<Book> getBooks();
    String getName();
}
