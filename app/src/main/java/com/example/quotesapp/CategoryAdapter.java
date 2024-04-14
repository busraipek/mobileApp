package com.example.quotesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context ct;
    private ArrayList<Category> arr;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater i = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.category_item, null);
        }
        if (arr.size() > 0) {
            Category d = arr.get(position);
            ImageView imgCategory = convertView.findViewById(R.id.imgCategory);
            TextView txvNameCategory = convertView.findViewById(R.id.txvNameCategory);
            TextView txvContent = convertView.findViewById(R.id.txvContent);

            imgCategory.setImageResource(d.image);
            txvNameCategory.setText(d.name);
            txvContent.setText(d.content);


        }
        return convertView;
    }
}

