package com.example.saicharan.whatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saicharan on 15-08-2017.
 */

public class SettingsAdapter extends BaseAdapter {


    Context context;
    ArrayList<String> headings;

    public SettingsAdapter(Context context, ArrayList<String> headings){
        this.context = context;
        this.headings = headings;
    }

    @Override
    public int getCount() {
        return headings.size();
    }

    @Override
    public Object getItem(int position) {
        return headings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_settings, parent, false);

        TextView textView= (TextView) view.findViewById(R.id.text_settings);
        String s = headings.get(position);

        textView.setText(s);

        return view;
    }

}
