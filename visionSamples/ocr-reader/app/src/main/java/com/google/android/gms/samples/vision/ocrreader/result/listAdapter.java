package com.google.android.gms.samples.vision.ocrreader.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.R;

import java.util.ArrayList;

/**
 * Created by tom.saju on 2/28/2018.
 */

public class listAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> dataList;
    TextView title;

    public listAdapter(Context context, ArrayList<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
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

        if(convertView==null){
            convertView = inflater.inflate(R.layout.list_item_layout,parent,false);
             title = (TextView) convertView.findViewById(R.id.title_book);
        }
        title.setText(dataList.get(position));
        return convertView;
    }
}
