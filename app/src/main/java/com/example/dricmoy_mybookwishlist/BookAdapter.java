package com.example.dricmoy_mybookwishlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final OnBookClickListener bookClickListener;
    private List<Book> books; // Use List for flexibility
    private final Context context;
    private final OnEditBookClickListener editListener;

    public interface OnEditBookClickListener {
        void onEditBookClick(int position); // Interface to handle edit button click
    }

    public interface OnBookClickListener {
        void onBookClick(int position); // Interface for book click
    }

    public BookAdapter(Context context, List<Book> books, OnEditBookClickListener editListener, OnBookClickListener bookClickListener) {
        this.context = context;
        this.books = new ArrayList<>(books); // Initialize books
        this.editListener = editListener;
        this.bookClickListener = bookClickListener; // Initialize book click listener
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        // Handle the Edit button click
        holder.editButton.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditBookClick(position);
            }
        });

        // Handle the book item click
        holder.itemView.setOnClickListener(v -> {
            if (bookClickListener != null) {
                bookClickListener.onBookClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateBooks(List<Book> updatedBooks) {
        this.books.clear();
        this.books.addAll(updatedBooks);
        notifyDataSetChanged(); // Notify adapter to refresh the view
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author;
        Button editButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }
}
