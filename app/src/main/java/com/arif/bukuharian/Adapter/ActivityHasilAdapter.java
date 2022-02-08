package com.arif.bukuharian.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arif.bukuharian.Model.ActivityHasilData;
import com.arif.bukuharian.R;

import java.text.DecimalFormat;
import java.util.List;


public class ActivityHasilAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<ActivityHasilData> items;

    public ActivityHasilAdapter(AppCompatActivity activity, List<ActivityHasilData> items) {
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
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_hasil_activity, null);

        TextView jam = (TextView) convertView.findViewById(R.id.jam);
        TextView hasil = (TextView) convertView.findViewById(R.id.hasil);
        TextView keterangan = (TextView) convertView.findViewById(R.id.keterangan);
        TextView disposisi = (TextView) convertView.findViewById(R.id.disposisi);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        TextView satuan = (TextView) convertView.findViewById(R.id.satuan);

        ActivityHasilData data = items.get(position);

        jam.setText(data.getJam());
        jumlah.setText(data.getJml());
        satuan.setText(data.getSatuan());
        hasil.setText(data.getHasilKerja());
        keterangan.setText(data.getKeterangan());
        disposisi.setText(data.getDisposisi());
        return convertView;
    }

}