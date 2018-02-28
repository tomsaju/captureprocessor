package com.google.android.gms.samples.vision.ocrreader.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tom.saju on 2/27/2018.
 */

public class BookList {
    @SerializedName("items")
    List<Book> bookList;

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
