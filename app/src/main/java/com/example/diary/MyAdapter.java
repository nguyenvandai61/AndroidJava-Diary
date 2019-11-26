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

public class MyAdapter extends ArrayAdapter {
    Context context;
    ArrayList<Diary> list;
    static Intent intent;
    public MyAdapter(Context context, ArrayList<Diary> arrayList) {
        super(context, R.layout.item_view, arrayList);
        this.context = context;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_view, parent, false);

        TextView tvDate, tvTime, tvTitle, tvContent, tvPublisher;
        tvDate = rowView.findViewById(R.id.tv_item_date);
        tvTime = rowView.findViewById(R.id.tv_item_time);
        tvTitle = rowView.findViewById(R.id.tv_item_title);
        tvContent = rowView.findViewById(R.id.tv_item_content);
        tvPublisher = rowView.findViewById(R.id.tv_item_publisher);
        LinearLayout llItem = rowView.findViewById(R.id.ll_item);



        final Diary item = list.get(position);
        tvDate.setText(item.getmDate());
        tvTime.setText(item.getmTime());
        tvTitle.setText(item.getmTitle());
        tvContent.setText(item.getmContent());
        tvPublisher.setText(item.getmPublisher());
        llItem.getBackground().setColorFilter(Color.parseColor(item.getmColor()), PorterDuff.Mode.SRC_ATOP);



        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, EditDiary.class);
                intent.putExtra("pos", position);
                intent.putExtra("diary", item);
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public void updateList(ArrayList<Diary> newlist) {
        list.clear();
        list.addAll(newlist);
        System.out.println(list.get(0));
        this.notifyDataSetChanged();
    }


}
