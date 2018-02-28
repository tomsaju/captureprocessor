package com.google.android.gms.samples.vision.ocrreader.networkLayer;

import com.google.android.gms.samples.vision.ocrreader.model.Book;
import com.google.android.gms.samples.vision.ocrreader.model.BookList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tom.saju on 2/27/2018.
 */

public interface BooksService {
    String SERVICE_ENDPOINT = "https://www.googleapis.com/books/v1/";


    @GET("volumes")
    Call<BookList> getBooks(@Query("q=title") String name);


    @GET()
    Call<BookList> getBook();
   /* @GET("volumes?q=title:{name}")
    Call<BookList> getBooks(@Path("name") String name);*/

}
