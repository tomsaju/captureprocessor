package com.google.android.gms.samples.vision.ocrreader.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tom.saju on 2/27/2018.
 */

public class volumeInfo {
    String title;
    imageLinks imageLinks;
    @SerializedName("authors")
    List<String> bookList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public com.google.android.gms.samples.vision.ocrreader.model.imageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(com.google.android.gms.samples.vision.ocrreader.model.imageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<String> getBookList() {
        return bookList;
    }

    public void setBookList(List<String> bookList) {
        this.bookList = bookList;
    }
}
