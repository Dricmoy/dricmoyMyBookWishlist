package com.example.dricmoy_mybookwishlist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnEditBookClickListener {
    private BookAdapter bookAdapter;
    private WishList myWishList;
    private TextView totalCount, readCount;
    private RecyclerView booklist;
    private Book BookEdit; // Declare this to hold the book being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalCount = findViewById(R.id.total_count);
        readCount = findViewById(R.id.read_count);
        booklist = findViewById(R.id.recycler_view);
        booklist.setLayoutManager(new LinearLayoutManager(this));

        myWishList = new WishList("My Favorite Books");
        //setupPresetWishlist();

        bookAdapter = new BookAdapter(this, myWishList.getBooks(), this, this::onBookClick);
        booklist.setAdapter(bookAdapter);
        updateCounts();

        Button addBookButton = findViewById(R.id.add_book_button);
        Button deleteBookButton = findViewById(R.id.delete_book_button);

        addBookButton.setOnClickListener(v -> showAddBookDialog());
        deleteBookButton.setOnClickListener(v -> {
            if (!myWishList.getBooks().isEmpty()) {
                showDeleteBookDialog(); // Show the delete dialog with book list
            } else {
                Toast.makeText(MainActivity.this, "No books to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // private void setupPresetWishlist() { // You can keep this method if you need it later
    //     myWishList.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, false));
    //     myWishList.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, false));
    // }

    @Override
    public void onEditBookClick(int position) {
        BookEdit = myWishList.getBooks().get(position); // Assign book to BookEdit
        showEditBookDialog(); // Now use the BookEdit for editing
    }

    @SuppressLint("SetTextI18n")
    private void updateCounts() {
        totalCount.setText("Total: " + myWishList.getBooks().size());
        long readBooks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            readBooks = myWishList.getBooks().stream().filter(Book::getStatus).count();
        }
        readCount.setText("Read: " + readBooks);
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Book");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_book, null);
        EditText titleInput = dialogView.findViewById(R.id.edit_title);
        EditText authorInput = dialogView.findViewById(R.id.edit_author);
        EditText genreInput = dialogView.findViewById(R.id.edit_genre);
        EditText yearInput = dialogView.findViewById(R.id.edit_year);
        Spinner statusSpinner = dialogView.findViewById(R.id.edit_status); // Use Spinner instead

        // Set up the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.read_unread_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        builder.setView(dialogView);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String genre = genreInput.getText().toString().trim();
            String yearString = yearInput.getText().toString().trim();
            String statusString = statusSpinner.getSelectedItem().toString(); // Get selected item

            if (validateBookInputs(title, author, genre, yearString, statusString)) {
                int year = Integer.parseInt(yearString);
                boolean status = statusString.equalsIgnoreCase("Read");
                addBook(new Book(title, author, genre, year, status));
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Invalid input. Please check your fields.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showEditBookDialog() {
        if (BookEdit == null) return; // Check if BookEdit is set

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Book");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_book, null);
        EditText titleInput = dialogView.findViewById(R.id.edit_title);
        EditText authorInput = dialogView.findViewById(R.id.edit_author);
        EditText genreInput = dialogView.findViewById(R.id.edit_genre);
        EditText yearInput = dialogView.findViewById(R.id.edit_year);
        Spinner statusSpinner = dialogView.findViewById(R.id.edit_status);

        // Set up Spinner for Read/Unread status
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.read_unread_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Populate dialog with current book details
        titleInput.setText(BookEdit.getTitle());
        authorInput.setText(BookEdit.getAuthor());
        genreInput.setText(BookEdit.getGenre());
        yearInput.setText(String.valueOf(BookEdit.getPublicationYear())); // Populate with int value
        statusSpinner.setSelection(BookEdit.getStatus() ? 0 : 1); // Set selection based on status

        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String genre = genreInput.getText().toString().trim();
            String yearString = yearInput.getText().toString().trim(); // Get year as string

            // Validate year input
            if (yearString.isEmpty()) {
                Toast.makeText(MainActivity.this, "Year cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearString); // Safely parse the year input
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Invalid year format", Toast.LENGTH_SHORT).show();
                return;
            }

            String statusString = statusSpinner.getSelectedItem().toString(); // Get selected status

            // Validate inputs
            if (validateBookInputs(title, author, genre, yearString, statusString)) {
                BookEdit.setTitle(title);
                BookEdit.setAuthor(author);
                BookEdit.setGenre(genre);
                BookEdit.setPublicationYear(year); // Set year as integer
                BookEdit.setStatus(statusString.equalsIgnoreCase("Read")); // Update status

                bookAdapter.updateBooks(myWishList.getBooks()); // Update adapter with the updated book list
                updateCounts(); // Update any counts if necessary
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Invalid input. Please check your fields.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void onBookClick(int position) {
        Book book = myWishList.getBooks().get(position);
        showBookDetailsDialog(book);
    }

    private void showBookDetailsDialog(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(book.getTitle());

        String details = "Author: " + book.getAuthor() + "\n" +
                "Genre: " + book.getGenre() + "\n" +
                "Year: " + book.getPublicationYear() + "\n" +
                "Status: " + (book.getStatus() ? "Read" : "Unread");

        builder.setMessage(details);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private boolean validateBookInputs(String title, String author, String genre, String yearString, String status) {
        return !title.isEmpty() && title.length() <= 50 &&
                !author.isEmpty() && author.length() <= 30 &&
                !genre.isEmpty() &&
                !yearString.isEmpty() && yearString.matches("\\d{4}") &&
                (status.equalsIgnoreCase("read") || status.equalsIgnoreCase("unread")); // Validate status
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this book?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteBook(position));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showDeleteBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Book to Delete");

        String[] bookTitles = new String[myWishList.getBooks().size()];
        for (int i = 0; i < myWishList.getBooks().size(); i++) {
            bookTitles[i] = myWishList.getBooks().get(i).getTitle();
        }

        builder.setItems(bookTitles, (dialog, which) -> {
            // Delete the selected book
            showDeleteConfirmationDialog(which);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addBook(Book book) {
        myWishList.addBook(book); // Updating the wishlist
        bookAdapter.updateBooks(myWishList.getBooks()); // Refreshing the adapter
        updateCounts();
    }

    public void deleteBook(int position) {
        myWishList.removeBook(myWishList.getBooks().get(position));
        bookAdapter.updateBooks(myWishList.getBooks()); // Refreshing the adapter
        updateCounts();
    }
}
