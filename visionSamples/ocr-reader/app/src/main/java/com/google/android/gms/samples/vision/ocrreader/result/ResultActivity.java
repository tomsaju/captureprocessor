package com.google.android.gms.samples.vision.ocrreader.result;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.ocrreader.R;
import com.google.android.gms.samples.vision.ocrreader.model.Book;
import com.google.android.gms.samples.vision.ocrreader.model.BookList;
import com.google.android.gms.vision.text.TextBlock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    ArrayList<String> detectedList;
    private static final String TAG = "ResultActivity";
    private static final String ENDPOINT = "https://www.googleapis.com/books/v1/volumes?q=title:";
    private static final String ENDPOINTISBN = "https://www.googleapis.com/books/v1/volumes?q=ISBN:";
    TextBlock currentItem ;
    private Gson gson;
    ArrayList<String> IdentifiedBookList;
    private RequestQueue requestQueue;
    listAdapter adapter;
    ListView resultlist;
    String barcode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        IdentifiedBookList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        resultlist = (ListView) findViewById(R.id.resultlist);
        Intent  i = getIntent();
        if(i.getExtras()!=null){
            detectedList = i.getStringArrayListExtra("list");
            barcode = i.getStringExtra("barcode");
        }
        if(detectedList==null||detectedList.isEmpty()){
            if(barcode!=null&&!barcode.isEmpty()){
                requestBookWIthCode(barcode);
            }
        }else {
            requestBookApi(detectedList);
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

        if(!detectedStringList.isEmpty()){
            requestBookApi(detectedStringList.get(0));
        }

    }

    private void requestBookWIthCode(String code){

        String url =ENDPOINTISBN+ code.replaceAll("\\n","%20");

        String query=url.replaceAll(" ", "%20");

        JsonObjectRequest request = new JsonObjectRequest(query, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                BookList posts = gson.fromJson(response.toString(), BookList.class);
                Log.d(TAG, "onResponse: ");
                List<Book> books = posts.getBookList();
                if(books!=null) {
                    for (int i = 0; i < books.size(); i++) {
                        //if (books.get(i).getVolumeInfo().getTitle().contains(value.replaceAll("\\n","%20"))) {
                        IdentifiedBookList.add(books.get(i).getVolumeInfo().getTitle());
                        Log.d(TAG, "identified book" + books.get(i).getVolumeInfo().getTitle());
                        // }
                    }
                    adapter = new listAdapter(getApplicationContext(),IdentifiedBookList);
                    resultlist.setAdapter(adapter);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
            }
        });

        requestQueue.add(request);
    }

    private void requestBookApi(final String value) {
        String url =ENDPOINT+ value.replaceAll("\\n","%20")+"&maxResults=2";

        String query=url.replaceAll(" ", "%20");

        JsonObjectRequest request = new JsonObjectRequest(query, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                BookList posts = gson.fromJson(response.toString(), BookList.class);
                Log.d(TAG, "onResponse: ");
                List<Book> books = posts.getBookList();
                if(books!=null) {
                    for (int i = 0; i < books.size(); i++) {
                        //if (books.get(i).getVolumeInfo().getTitle().contains(value.replaceAll("\\n","%20"))) {
                            IdentifiedBookList.add(books.get(i).getVolumeInfo().getTitle());
                            Log.d(TAG, "identified book" + books.get(i).getVolumeInfo().getTitle());
                       // }
                    }
                    adapter = new listAdapter(getApplicationContext(),IdentifiedBookList);
                    resultlist.setAdapter(adapter);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
