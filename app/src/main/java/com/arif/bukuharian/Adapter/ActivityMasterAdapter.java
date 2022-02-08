package com.arif.bukuharian.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arif.bukuharian.Model.ActivityMasterData;
import com.arif.bukuharian.R;

import java.text.DecimalFormat;
import java.util.List;


public class ActivityMasterAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<ActivityMasterData> items;

    public ActivityMasterAdapter(AppCompatActivity activity, List<ActivityMasterData> items) {
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
            convertView = inflater.inflate(R.layout.list_master_activity, null);

        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        TextView bagian = (TextView) convertView.findViewById(R.id.bagian);
        TextView jabatan = (TextView) convertView.findViewById(R.id.jabatan);
        TextView createby = (TextView) convertView.findViewById(R.id.createby);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView checkdate = (TextView) convertView.findViewById(R.id.checkdate);
        TextView checkby = (TextView) convertView.findViewById(R.id.checkby);

        ActivityMasterData data = items.get(position);

        nama.setText(data.getNama());
        //2021-03-01
        String tgl = data.getTanggal().substring(8, 10)+"-"+data.getTanggal().substring(5, 7)+"-"+data.getTanggal().substring(0, 4);
        tanggal.setText(tgl);
        if(data.getCheckDate().equals("-")){
            checkdate.setText("-");
        } else {
            String tgl2 = data.getCheckDate().substring(8, 10) + "-" + data.getCheckDate().substring(5, 7) + "-" + data.getCheckDate().substring(0, 4);
            checkdate.setText(tgl2);
        }
        tanggal.setText(tgl);
        bagian.setText(data.getBagian());
        jabatan.setText(data.getJabatan());
        createby.setText(data.getCreateByNama());
        if(data.getStatus().equals("A")) {
            status.setText("Belum Di Cek Kabag atau Pimpinan");
            status.setTextColor(Color.BLUE);
        } else if(data.getStatus().equals("C")) {
            status.setText("Disetujui Kabag atau Pimpinan");
            status.setTextColor(Color.GREEN);
        } else {
            status.setText("Ditolak Kabag atau Pimpinan");
            status.setTextColor(Color.RED);
        }

        checkby.setText(data.getCheckBy());
        return convertView;
    }

}