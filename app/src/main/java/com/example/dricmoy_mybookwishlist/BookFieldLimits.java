package com.example.dricmoy_mybookwishlist;

/**
 * Enum representing the limits for various book fields.
 */
public enum BookFieldLimits {
    // Maximum length for the book title
    TITLE_MAX_LENGTH(50),

    // Maximum length for the book author name
    AUTHOR_MAX_LENGTH(30),

    // Minimum publication year for a book
    PUBLICATION_YEAR_MIN(1900),

    // Maximum publication year for a book
    PUBLICATION_YEAR_MAX(2024);

    private final int limit;  // The limit value for the respective field

    /**
     * Constructor to initialize the limit for each enum constant.
     *
     * @param limit The limit value for the book field
     */
    BookFieldLimits(int limit) {
        this.limit = limit;
    }

    /**
     * Retrieves the limit value for the book field.
     *
     * @return The limit value
     */
    public int getLimit() {
        return limit;
    }
}
