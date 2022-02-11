package com.arif.bukuharian.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arif.bukuharian.Model.ActivityHasilData;
import com.arif.bukuharian.Model.ActivityRencanaHasilData;
import com.arif.bukuharian.R;

import java.text.DecimalFormat;
import java.util.List;


public class ActivityRencanaHasilAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<ActivityRencanaHasilData> items;

    public ActivityRencanaHasilAdapter(AppCompatActivity activity, List<ActivityRencanaHasilData> items) {
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
            convertView = inflater.inflate(R.layout.list_rencana_hasil_activity, null);

        TextView uraian = (TextView) convertView.findViewById(R.id.uraian);
        TextView task = (TextView) convertView.findViewById(R.id.task);
        TextView jam = (TextView) convertView.findViewById(R.id.jam);
        TextView hasil = (TextView) convertView.findViewById(R.id.hasil);
        TextView keterangan = (TextView) convertView.findViewById(R.id.keterangan);
        TextView disposisi = (TextView) convertView.findViewById(R.id.disposisi);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);

        ActivityRencanaHasilData data = items.get(position);

        if(data.getAktifitas()==null){
            uraian.setText("Belum Dikerjakan");
        } else {
            uraian.setText(data.getAktifitas());
        }
        if(data.getHasil()==null){
            hasil.setText("0%");
        } else {
            hasil.setText(data.getHasil()+"%");
        }
        task.setText(data.getTaskList());
        jam.setText(data.getJamAwal());
        jumlah.setText(data.getJml());
        keterangan.setText(data.getKeterangan());
        disposisi.setText(data.getDisposisi());
        return convertView;
    }

}