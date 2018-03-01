package com.google.android.gms.samples.vision.ocrreader.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.R;
import com.google.android.gms.samples.vision.ocrreader.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tom.saju on 2/28/2018.
 */

public class listAdapter extends BaseAdapter {
    Context context;
    //ArrayList<String> dataList;
    TextView title;
    ImageView image;
    TextView author;
    ArrayList<Book> bookList;


    public listAdapter(Context context, ArrayList<Book> dataList) {
        this.context = context;
        this.bookList = dataList;
    }
    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_layout,parent,false);
        title = (TextView) convertView.findViewById(R.id.title_book);
        image = (ImageView) convertView.findViewById(R.id.thumbImage);
        author = (TextView) convertView.findViewById(R.id.author_book);

        title.setText(bookList.get(position).getVolumeInfo().getTitle());
        Picasso.with(context).load(bookList.get(position).getVolumeInfo().getImageLinks().getThumbnail()).into(image);
        if(bookList.get(position).getVolumeInfo().getBookList()!=null&&!bookList.get(position).getVolumeInfo().getBookList().isEmpty()) {
            author.setText(bookList.get(position).getVolumeInfo().getBookList().get(0));
        }else{
            author.setText("");
        }
        return convertView;
    }
}
