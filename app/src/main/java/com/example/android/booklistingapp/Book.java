package com.example.android.booklistingapp;

/**
 * Created by Maino96-10022 on 11/26/2016.
 */

public class Book {

    /**
     * Title of the book
     */
    private String mBookTitle;

    /**
     * Name of the author
     */
    private String mAuthorName;


    public Book(String bookTitle, String authorName) {
        mBookTitle = bookTitle;
        mAuthorName = authorName;

    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

}
