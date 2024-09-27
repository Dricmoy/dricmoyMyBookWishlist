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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MainActivity class is responsible for displaying the wishlist UI, allowing users to add, edit, delete, and view book details.
 * It integrates RecyclerView for displaying the book list and dialog boxes for book operations (add/edit/delete).
 */
public class MainActivity extends AppCompatActivity implements BookAdapter.OnEditBookClickListener, BookAdapter.OnBookClickListener, BookOperations, DialogOperations {
    private BookAdapter bookAdapter;  // Adapter to manage the display of books in the RecyclerView
    private WishList myWishList;  // Object representing the user's wish list of books
    private TextView totalCount, readCount;  // TextViews to display total number of books and the count of books that have been read
    private RecyclerView booklist;  // RecyclerView that displays the list of books
    private Book BookEdit;  // Variable to hold the reference to the book currently being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews and RecyclerView
        totalCount = findViewById(R.id.total_count);
        readCount = findViewById(R.id.read_count);
        booklist = findViewById(R.id.recycler_view);
        booklist.setLayoutManager(new LinearLayoutManager(this));

        // Initialize wishlist and adapter
        myWishList = new WishList("My Favorite Books");

        // Initialize adapter with empty list
        bookAdapter = new BookAdapter(this, myWishList.getBooks(), this, this::onBookClick);
        booklist.setAdapter(bookAdapter);
        // Update counts of book on the screen
        updateCounts();

        // Set up buttons for adding and deleting books
        Button addBookButton = findViewById(R.id.add_book_button);
        Button deleteBookButton = findViewById(R.id.delete_book_button);

        // Set up click listeners for buttons
        addBookButton.setOnClickListener(v -> showAddBookDialog());  // Opens dialog to add a new book
        deleteBookButton.setOnClickListener(v -> {
            if (!myWishList.getBooks().isEmpty()) {
                showDeleteBookDialog();  // Opens dialog to delete a book
            } else {
                Toast.makeText(MainActivity.this, "No books to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Triggered when the edit button for a book is clicked. It fetches the book at the given position for editing.
     *
     * @param position The position of the book in the RecyclerView list.
     */
    @Override
    public void onEditBookClick(int position) {
        BookEdit = myWishList.getBooks().get(position); // Assign book to BookEdit
        showEditBookDialog(); // Now use the BookEdit for editing
    }

    /**
     * Updates the total count of books and the number of books marked as read.
     * Uses a stream to count books that have been marked as "Read."
     */
    @SuppressLint("SetTextI18n")
    private void updateCounts() {
        totalCount.setText("Total: " + myWishList.getBooks().size());
        long readBooks = 0;
        // If the API level is 24+, use Java streams to filter the read books
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            readBooks = myWishList.getBooks().stream().filter(Book::getStatus).count();
        }
        readCount.setText("Read: " + readBooks);
    }

    /**
     * Displays a dialog allowing the user to input details for adding a new book.
     */
    public void showAddBookDialog() {
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
                try {
                    int year = Integer.parseInt(yearString);
                    boolean status = statusString.equalsIgnoreCase("Read");
                    addBook(new Book(title, author, genre, year, status));
                    dialog.dismiss();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); // Show the error message
                }
            } else {
                Toast.makeText(MainActivity.this, "Invalid input. Please check your fields.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Shows the dialog to edit a book's details. Populates the input fields with the current book data.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void showEditBookDialog() {
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
                try {
                    BookEdit.setTitle(title);
                    BookEdit.setAuthor(author);
                    BookEdit.setGenre(genre);
                    BookEdit.setPublicationYear(year); // Set year as integer
                    BookEdit.setStatus(statusString.equalsIgnoreCase("Read")); // Update status

                    bookAdapter.updateBooks(myWishList.getBooks()); // Update adapter with the updated book list
                    updateCounts(); // Update any counts if necessary
                    dialog.dismiss();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); // Show the error message
                }
            } else {
                Toast.makeText(MainActivity.this, "Invalid input. Please check your fields.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Triggered when a book item is clicked. Displays the details of the clicked book in a Toast message.
     *
     * @param position The position of the clicked book in the list.
     */
    public void onBookClick(int position) {
        Book book = myWishList.getBooks().get(position);
        showBookDetailsDialog(book);
    }

    /**
     * Displays a dialog with detailed information about the selected book.
     *
     * @param book The book whose details are to be displayed.
     */
    public void showBookDetailsDialog(@NonNull Book book) {
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

    /**
     * Validates book input fields for proper data.
     * Ensures that fields are non-empty, within limit and that the year is a valid integer.
     *
     * @param title The title of the book.
     * @param author The author of the book.
     * @param genre The genre of the book.
     * @param yearString The publication year of the book (as string).
     * @param status The read status of the book.
     * @return True if all fields are valid, false otherwise.
     */
    private boolean validateBookInputs(@NonNull String title, String author, String genre, String yearString, String status) {
        return !title.isEmpty() && title.length() <= BookFieldLimits.TITLE_MAX_LENGTH.getLimit() &&
                !author.isEmpty() && author.length() <= BookFieldLimits.AUTHOR_MAX_LENGTH.getLimit() &&
                !genre.isEmpty() &&
                !yearString.isEmpty() && yearString.matches("\\d{4}") &&
                (status.equalsIgnoreCase("read") || status.equalsIgnoreCase("unread")); // Validate status
    }

    /**
     * Displays a confirmation dialog to the user before deleting a book.
     *
     * @param position The position of the book in the wishlist that the user intends to delete.
     */
    public void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion"); // Set the dialog title
        builder.setMessage("Are you sure you want to delete this book?"); // Message to confirm deletion

        // Set positive button action to delete the book
        builder.setPositiveButton("Yes", (dialog, which) -> deleteBook(position));
        // Set negative button action to dismiss the dialog if the user cancels
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        // Show the confirmation dialog
        builder.show();
    }

    /**
     * Displays a dialog for the user to select a book from the wishlist to delete.
     */
    public void showDeleteBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Book to Delete");

        // Create an array of book titles for the dialog
        String[] bookTitles = new String[myWishList.getBooks().size()];
        for (int i = 0; i < myWishList.getBooks().size(); i++) {
            bookTitles[i] = myWishList.getBooks().get(i).getTitle();
        }

        // Set the items in the dialog, and handle the selection
        builder.setItems(bookTitles, (dialog, which) -> {
            // Delete the selected book by showing a confirmation dialog
            showDeleteConfirmationDialog(which);
        });

        // Set the negative button to cancel the action
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog for selecting a book to delete
        builder.show();
    }

    /**
     * Adds a new book to the wishlist and refreshes the UI.
     *
     * @param book The book to be added.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void addBook(Book book) {
        myWishList.addBook(book); // Updating the wishlist
        bookAdapter.updateBooks(myWishList.getBooks()); // Refreshing the adapter
        updateCounts();
    }

    /**
     * Deletes a book from the wishlist and refreshes the UI.
     *
     * @param position The book to be deleted.
     */
    public void deleteBook(int position) {
        myWishList.removeBook(myWishList.getBooks().get(position));
        bookAdapter.updateBooks(myWishList.getBooks()); // Refreshing the adapter
        updateCounts();
    }
}
