package com.arif.bukuharian.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arif.bukuharian.Model.ActivityRencanaData;
import com.arif.bukuharian.R;

import java.text.DecimalFormat;
import java.util.List;


public class ActivityRencanaAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    private LayoutInflater inflater;
    private List<ActivityRencanaData> items;

    public ActivityRencanaAdapter(AppCompatActivity activity, List<ActivityRencanaData> items) {
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
            convertView = inflater.inflate(R.layout.list_rencana_activity, null);

        TextView uraian = (TextView) convertView.findViewById(R.id.uraian);
        TextView jumlah = (TextView) convertView.findViewById(R.id.jumlah);
        TextView satuan = (TextView) convertView.findViewById(R.id.satuan);

        ActivityRencanaData data = items.get(position);

        uraian.setText(data.getUraian());
        jumlah.setText(data.getJml());
        satuan.setText(data.getSatuan());
        return convertView;
    }

}