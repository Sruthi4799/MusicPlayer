package com.example.tech2k8.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {
    ArrayList<EncapsulatedData> data;
    Context context;
    public ListAdapter(ArrayList<EncapsulatedData> data, Context context) {
     this.data=data;
     this.context=context;


    }

    @Override

    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflator=LayoutInflater.from(context).inflate(R.layout.listadpater,null,false);
        TextView musicname=inflator.findViewById(R.id.name);
        musicname.setText(data.get(position).getName());
        return inflator;
    }
}
