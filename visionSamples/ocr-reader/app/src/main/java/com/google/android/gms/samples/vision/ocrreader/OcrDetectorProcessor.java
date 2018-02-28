/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.ocrreader.model.Book;
import com.google.android.gms.samples.vision.ocrreader.model.BookList;
import com.google.android.gms.samples.vision.ocrreader.networkLayer.BooksService;
import com.google.android.gms.samples.vision.ocrreader.result.ResultActivity;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {
    private static final String TAG = "OcrDetectorProcessor";
    private Gson gson;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    ArrayList<String> detectedStringList;
    private static final String ENDPOINT = "https://www.googleapis.com/books/v1/volumes?q=title:";
    TextBlock currentItem ;
    ArrayList<String> IdentifiedBookList;
    boolean stop = false;
    Context context;

    private RequestQueue requestQueue;
    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, Context context) {
        mGraphicOverlay = ocrGraphicOverlay;
        detectedStringList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        IdentifiedBookList = new ArrayList<>();
        this.context = context;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        if(stop){
            stop = false;
            ArrayList<String> transferList = detectedStringList;
            detectedStringList.clear();
            Intent i = new Intent(context, ResultActivity.class);
            i.putStringArrayListExtra("list",transferList);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
           // requestBookApi(detectedStringList);
            return;
        }
        String total ;
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            Log.d(TAG, "receiveDetections: "+item.getValue());
            if(!validateItems(item.getValue())){
                return;
            }
            currentItem = item;
            boolean flag = false;
            for (String string : detectedStringList) {
                if(string.equalsIgnoreCase(currentItem.getValue())){
                 flag = true;
                }
            }
            if(flag){
                return;
            }else {
                detectedStringList.add(item.getValue());
             //   Toast.makeText(context, item.getValue(), Toast.LENGTH_SHORT).show();
            }

            if(detectedStringList.size()>=2){
                stop = true;
            }


          /*  Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BooksService.SERVICE_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            BooksService service = retrofit.create(BooksService.class);

            Call<BookList> call = service.getBooks("azkaban");
            //Call<BookList> call = service.getBook();
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
          call.enqueue(new Callback<BookList>() {
              @Override
              public void onResponse(Call<BookList> call, Response<BookList> response) {
                  BookList myList = response.body();
                  if(myList!=null) {
                      myList.getBookList();
                  }
                  Log.d(TAG, "onResponse: list obtained");
              }

              @Override
              public void onFailure(Call<BookList> call, Throwable t) {

              }
          });*/
        }
    }

    private void requestBookApi(ArrayList<String> detectedStringList) {

        Collections.sort(detectedStringList, new java.util.Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // TODO: Argument validation (nullity, length)
                return s2.length() - s1.length();// comparision
            }
        });
        Log.d(TAG, "requestBookApi: sorted");
    }

    private void requestBookApi(String value) {
        String query=ENDPOINT+ value;

        JsonObjectRequest request = new JsonObjectRequest(query, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                BookList posts = gson.fromJson(response.toString(), BookList.class);
                Log.d(TAG, "onResponse: ");
                List<Book> books = posts.getBookList();
                if(books!=null) {
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getVolumeInfo().getTitle().contains(currentItem.getValue())) {
                            IdentifiedBookList.add(books.get(i).getVolumeInfo().getTitle());
                            Log.d(TAG, "identified book" + books.get(i).getVolumeInfo().getTitle());
                        }
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);
    }

    private boolean validateItems(String value) {
        boolean result = true;
        if(value.length()<2){
            result = false;
        }
        if(value.equalsIgnoreCase("the")){
            result = false;
        }

        return result;
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    public class MyComparator implements java.util.Comparator<String> {

        private int referenceLength;

        public MyComparator(String reference) {
            super();
            this.referenceLength = reference.length();
        }

        public int compare(String s1, String s2) {
            int dist1 = Math.abs(s1.length() - referenceLength);
            int dist2 = Math.abs(s2.length() - referenceLength);

            return dist1 - dist2;
        }
    }
}
