package com.example.dricmoy_mybookwishlist;

public class Book {
    private String title;
    private String author;
    private String genre; // Genre of the book
    private int publicationYear; // Year of publication
    private boolean status; // true = read, false = not read

    // Constructor for basic book details
    public Book(String title, String author, String genre, int publicationYear, boolean status) {
        this.setTitle(title);
        this.setAuthor(author);
        this.genre = genre;
        this.setPublicationYear(publicationYear);
        this.status = status;
    }

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() <= BookFieldLimits.TITLE_MAX_LENGTH.getLimit()) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title must be " + BookFieldLimits.TITLE_MAX_LENGTH.getLimit() + " characters or fewer.");
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author.length() <= BookFieldLimits.AUTHOR_MAX_LENGTH.getLimit()) {
            this.author = author;
        } else {
            throw new IllegalArgumentException("Author name must be " + BookFieldLimits.AUTHOR_MAX_LENGTH.getLimit() + " characters or fewer.");
        }
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear >= BookFieldLimits.PUBLICATION_YEAR_MIN.getLimit() && publicationYear <= BookFieldLimits.PUBLICATION_YEAR_MAX.getLimit()) {
            this.publicationYear = publicationYear;
        } else {
            throw new IllegalArgumentException("Publication year must be between " + BookFieldLimits.PUBLICATION_YEAR_MIN.getLimit() + " and " + BookFieldLimits.PUBLICATION_YEAR_MAX.getLimit() + ".");
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean getStatus() {
        return status; // Use this to check if the book is read
    }

    public void setStatus(boolean read) {
        this.status = read; // Set the book's read status
    }
}
