package com.example.dricmoy_mybookwishlist;

public enum BookFieldLimits {
    TITLE_MAX_LENGTH(50),
    AUTHOR_MAX_LENGTH(30),
    PUBLICATION_YEAR_MIN(1900),
    PUBLICATION_YEAR_MAX(2024);

    private final int limit;

    BookFieldLimits(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
