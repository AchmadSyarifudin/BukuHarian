package com.arif.bukuharian.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arif.bukuharian.Model.PegawaiData;
import com.arif.bukuharian.R;

import java.util.List;


public class PegawaiAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<PegawaiData> items;

    public PegawaiAdapter(AppCompatActivity activity, List<PegawaiData> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.view_pegawai, null);

        TextView nama = (TextView) convertView.findViewById(R.id.nama);

        PegawaiData data = items.get(position);

        nama.setText(data.getNama());

        return convertView;
    }

}