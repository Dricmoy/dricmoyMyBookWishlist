package com.example.dricmoy_mybookwishlist;

import androidx.annotation.NonNull;

/**
 * Represents a book with details such as title, author, genre, publication year, and read status.
 */
public class Book {
    private String title; // Title of the book
    private String author; // Author of the book
    private String genre; // Genre of the book
    private int publicationYear; // Year of publication
    private boolean status; // true = read, false = not read

    /**
     * Constructor for initializing a book with basic details.
     *
     * @param title          The title of the book
     * @param author         The author of the book
     * @param genre          The genre of the book
     * @param publicationYear The year the book was published
     * @param status         The read status of the book (true if read, false if not read)
     */
    public Book(String title, String author, String genre, int publicationYear, boolean status) {
        this.setTitle(title);
        this.setAuthor(author);
        this.genre = genre;
        this.setPublicationYear(publicationYear);
        this.status = status;
    }

    // Getter and Setter methods

    /**
     * Retrieves the title of the book.
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set
     * @throws IllegalArgumentException If the title exceeds the maximum allowed length
     */
    public void setTitle(@NonNull String title) {
        if (title.length() <= BookFieldLimits.TITLE_MAX_LENGTH.getLimit()) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title must be " + BookFieldLimits.TITLE_MAX_LENGTH.getLimit() + " characters or fewer.");
        }
    }

    /**
     * Retrieves the author of the book.
     *
     * @return The author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author The author to set
     * @throws IllegalArgumentException If the author name exceeds the maximum allowed length
     */
    public void setAuthor(@NonNull String author) {
        if (author.length() <= BookFieldLimits.AUTHOR_MAX_LENGTH.getLimit()) {
            this.author = author;
        } else {
            throw new IllegalArgumentException("Author name must be " + BookFieldLimits.AUTHOR_MAX_LENGTH.getLimit() + " characters or fewer.");
        }
    }

    /**
     * Retrieves the publication year of the book.
     *
     * @return The publication year
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Sets the publication year of the book.
     *
     * @param publicationYear The year to set
     * @throws IllegalArgumentException If the year is outside the allowed range
     */
    public void setPublicationYear(int publicationYear) {
        if (publicationYear >= BookFieldLimits.PUBLICATION_YEAR_MIN.getLimit() && publicationYear <= BookFieldLimits.PUBLICATION_YEAR_MAX.getLimit()) {
            this.publicationYear = publicationYear;
        } else {
            throw new IllegalArgumentException("Publication year must be between " + BookFieldLimits.PUBLICATION_YEAR_MIN.getLimit() + " and " + BookFieldLimits.PUBLICATION_YEAR_MAX.getLimit() + ".");
        }
    }


    /**
     * Retrieves the genre of the book.
     *
     * @return The genre of the book
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the book.
     *
     * @param genre The genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Retrieves the read status of the book.
     *
     * @return true if the book is read, false otherwise
     */
    public boolean getStatus() {
        return status; // Use this to check if the book is read
    }

    /**
     * Sets the read status of the book.
     *
     * @param read The status to set (true if read, false if not)
     */
    public void setStatus(boolean read) {
        this.status = read; // Set the book's read status
    }
}
