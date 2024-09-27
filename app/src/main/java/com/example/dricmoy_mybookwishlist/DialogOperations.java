package com.example.dricmoy_mybookwishlist;

public interface DialogOperations {
    void showAddBookDialog();
    void showEditBookDialog();
    void showDeleteBookDialog();
    void showDeleteConfirmationDialog(int position);
    void showBookDetailsDialog(Book book);
}