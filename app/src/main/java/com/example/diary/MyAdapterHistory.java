package com.example.diary;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterHistory extends ArrayAdapter {
    Context context;
    ArrayList<History> list;
    static Intent intent;
    public MyAdapterHistory(Context context, ArrayList<History> arrayList) {
        super(context, R.layout.item_view_history, arrayList);
        this.context = context;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_view_history, parent, false);

        TextView tvDate, tvTime, tvTitle, tvContent, tvPublisher;
        tvDate = rowView.findViewById(R.id.tv_item_date);
        tvTime = rowView.findViewById(R.id.tv_item_time);
        tvContent = rowView.findViewById(R.id.tv_item_content);
        tvPublisher = rowView.findViewById(R.id.tv_item_publisher);



        final History item = list.get(position);
        tvDate.setText(item.getmDate());
        tvTime.setText(item.getmTime());
        tvContent.setText(item.getmContent());
        tvPublisher.setText(item.getmPublisher());



        return rowView;
    }



}
