package com.arif.bukuharian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.arif.bukuharian.Adapter.ActivityHasilAdapter;
import com.arif.bukuharian.Adapter.ActivityMasterAdapter;
import com.arif.bukuharian.Adapter.ActivityRencanaAdapter;
import com.arif.bukuharian.Adapter.ActivityRencanaHasilAdapter;
import com.arif.bukuharian.Model.ActivityHasilData;
import com.arif.bukuharian.Model.ActivityMasterData;
import com.arif.bukuharian.Model.ActivityRencanaData;
import com.arif.bukuharian.Model.ActivityRencanaHasilData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    String id,kodejabatan,nama,kode,tanggal,idpegawai,namapegawai,jabatanpegawai,status,divisi,lat,lon;
    TextView tvnama,tvtanggal,tvdivisi,tvstatus;
    List<ActivityRencanaHasilData> daftarrencana = new ArrayList<ActivityRencanaHasilData>();
    ActivityRencanaHasilAdapter rencanaAdapter;
    List<ActivityHasilData> daftarhasil = new ArrayList<ActivityHasilData>();
    ActivityHasilAdapter hasilAdapter;
    ListView lvrencana;
    RelativeLayout rl1,rl2;
    Button btntambah;
    CircleImageView btnrefresh;
    JSONAPI jsonapi;
    int hasil =0,rencana = 0;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent objIntent = getIntent();
        id = objIntent.getStringExtra("id");
        kodejabatan = objIntent.getStringExtra("kodejabatan");
        kode = objIntent.getStringExtra("kode");
        tanggal = objIntent.getStringExtra("tanggal");
        nama = objIntent.getStringExtra("nama");
        idpegawai = objIntent.getStringExtra("idpegawai");
        namapegawai = objIntent.getStringExtra("namapegawai");
        jabatanpegawai = objIntent.getStringExtra("jabatanpegawai");
        status = objIntent.getStringExtra("status");
        divisi = objIntent.getStringExtra("divisi");
        lat = objIntent.getStringExtra("lat");
        lon = objIntent.getStringExtra("lon");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi = retrofit.create(JSONAPI.class);

        findViewById();

        tvnama.setText(nama);
        tvtanggal.setText(tanggal.substring(8, 10)+"-"+tanggal.substring(5, 7)+"-"+tanggal.substring(0, 4));
        tvdivisi.setText(divisi);
        tvstatus.setText(status);
        if(status.equals("A")) {
            tvstatus.setText("Belum Di Cek Kabag atau Pimpinan");
            tvstatus.setTextColor(Color.RED);
        } else {
            tvstatus.setText("Sudah Di Cek Kabag atau Pimpinan");
            tvstatus.setTextColor(Color.GREEN);
        }
        rencanaAdapter = new ActivityRencanaHasilAdapter(MainActivity2.this, daftarrencana);
        lvrencana.setAdapter(rencanaAdapter);

        getRencana();

        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm5();
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRencana();
            }
        });

        lvrencana.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view,
                                    int position, long kdnota) {
                // TODO Auto-generated method stub
                final String kode = daftarrencana.get(position).getKodeTaskList();
                final String uraian = daftarrencana.get(position).getAktifitas();
                final String task = daftarrencana.get(position).getTaskList();
                final String hasil = daftarrencana.get(position).getHasil();
                final String jam = daftarrencana.get(position).getJamAwal();
                final String jml = daftarrencana.get(position).getJml();
                final String keterangan = daftarrencana.get(position).getKeterangan();
                final String disposisi = daftarrencana.get(position).getDisposisi();

                DialogForm3(kode,uraian,task,hasil,jam,jml,keterangan,disposisi);

            }
        });

    }

    private void findViewById() {
        tvnama = findViewById(R.id.tvnama);
        tvtanggal = findViewById(R.id.tvtanggal);
        tvdivisi = findViewById(R.id.tvdivisi);
        tvstatus = findViewById(R.id.tvstatus);
        lvrencana = findViewById(R.id.lvrencana);
        btntambah = findViewById(R.id.btntambah);
        btnrefresh = findViewById(R.id.btnrefresh);

    }

    public void getRencana(){
        daftarrencana.clear();
        rencanaAdapter.notifyDataSetChanged();
        Call<List<ActivityRencanaHasilData>> call = jsonapi.getActivityRencanaHasil(kode);

        call.enqueue(new Callback<List<ActivityRencanaHasilData>>() {
            @Override
            public void onResponse(Call<List<ActivityRencanaHasilData>> call, retrofit2.Response<List<ActivityRencanaHasilData>> response) {
                DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
                if(response.isSuccessful()){
                    List<ActivityRencanaHasilData> posts = response.body();

                    for (ActivityRencanaHasilData post: posts){
                        ActivityRencanaHasilData item = new ActivityRencanaHasilData();

                        item.setKode_Act(post.getKode_Act());
                        item.setKodeTaskList(post.getKodeTaskList());
                        item.setTaskList(post.getTaskList());
                        item.setAktifitas(post.getAktifitas());
                        item.setJamAwal(post.getJamAwal());
                        item.setHasil(post.getHasil());
                        item.setJml(post.getJml());
                        item.setKeterangan(post.getKeterangan());
                        item.setRClose(post.getRClose());
                        item.setIsClose(post.getIsClose());
                        item.setApprove(post.getApprove());
                        item.setDisposisi(post.getDisposisi());
                        item.setCloseDate(post.getCloseDate());
                        item.setCloseBy(post.getCloseBy());

                        daftarrencana.add(item);
                    }
                    rencanaAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity2.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ActivityRencanaHasilData>> call, Throwable t) {
//                Toast.makeText(MainActivity2.this,"Gagal ambil data, Silahkan cek koneksi dan refresh data",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getHasil(){
        daftarhasil.clear();
        hasilAdapter.notifyDataSetChanged();
        Call<List<ActivityHasilData>> call = jsonapi.getActivityHasil(kode);

        call.enqueue(new Callback<List<ActivityHasilData>>() {
            @Override
            public void onResponse(Call<List<ActivityHasilData>> call, retrofit2.Response<List<ActivityHasilData>> response) {
                DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
                if(response.isSuccessful()){
                    List<ActivityHasilData> posts = response.body();

                    for (ActivityHasilData post: posts){
                        ActivityHasilData item = new ActivityHasilData();

                        item.setKd_Act(post.getKd_Act());
                        item.setJam(post.getJam());
                        item.setHasilKerja(post.getHasilKerja());
                        item.setJml(post.getJml());
                        item.setSatuan(post.getSatuan());
                        item.setKeterangan(post.getKeterangan());
                        item.setDisposisi(post.getDisposisi());

                        daftarhasil.add(item);
                    }
                    hasilAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity2.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ActivityHasilData>> call, Throwable t) {
                Toast.makeText(MainActivity2.this,"Gagal ambil data, Silahkan cek koneksi dan refresh data",Toast.LENGTH_LONG).show();
            }
        });

    }

//    private void DialogForm() {
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.form_insert_hasil, null);
//        dialog.setView(dialogView);
//        dialog.setCancelable(true);
//        dialog.setIcon(R.mipmap.fbr);
//        dialog.setTitle(" Isi Laporan Hasil Kerja : ");
//
//        EditText kodex = (EditText) dialogView.findViewById(R.id.edkode);
//        TextView jam = (TextView) dialogView.findViewById(R.id.edjam);
//        EditText hasil = (EditText) dialogView.findViewById(R.id.edhasil);
//        EditText jumlah = (EditText) dialogView.findViewById(R.id.edjumlah);
//        EditText satuan = (EditText) dialogView.findViewById(R.id.edsatuan);
//        EditText keterangan = (EditText) dialogView.findViewById(R.id.edketerangan);
//        Button simpan = (Button) dialogView.findViewById(R.id.btnsimpan);
//
//        final AlertDialog alert = dialog.create();
//        alert.setView(dialogView);
//
////        if(kodejabatan.equals("jt02")){
////            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
////            String currentDateandTime = sdf.format(new Date());
////            jam.setText(currentDateandTime);
////            jam.setEnabled(false);
////
////        }
//        kodex.setText(kode);
//
//        jam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showTimeDialog(jam);
//            }
//        });
//
//        simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(hasil.getText().toString().trim().equals("")){
//                    hasil.setError("Kolom Tidak Boleh Kosong");
//                } else {
//                    if(jumlah.getText().toString().trim().equals("")){
//                        jumlah.setError("Kolom Tidak Boleh Kosong");
//                    } else {
//                        if(satuan.getText().toString().trim().equals("")){
//                            satuan.setError("Kolom Tidak Boleh Kosong");
//                        } else {
//                            if(keterangan.getText().toString().trim().equals("")){
//                                keterangan.setError("Kolom Tidak Boleh Kosong");
//                            } else {
//                                postHasil(kodex.getText().toString(),jam.getText().toString(),hasil.getText().toString(),
//                                        jumlah.getText().toString(),satuan.getText().toString(),keterangan.getText().toString());
//                                alert.dismiss();
//                            }
//                        }
//                    }
//                }
//
//            }
//        });
//
//        alert.show();
//    }

    private void DialogForm2(final String kodex,final String uraianx,final String taskx,final String hasilx,
                             final String jamx,final String jmlx,final String ketx,final String disposisix) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_insert_hasil, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Isi Laporan Hasil Kerja : ");

        EditText rencana = (EditText) dialogView.findViewById(R.id.edrencana);
        TextView jam = (TextView) dialogView.findViewById(R.id.edjam);
        EditText hasil = (EditText) dialogView.findViewById(R.id.edhasil);
        EditText jumlah = (EditText) dialogView.findViewById(R.id.edjumlah);
        EditText presentase = (EditText) dialogView.findViewById(R.id.edpresentase);
        EditText keterangan = (EditText) dialogView.findViewById(R.id.edketerangan);
        Button simpan = (Button) dialogView.findViewById(R.id.btnsimpan);


        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        rencana.setText(taskx);
        hasil.setText(uraianx);
        jam.setText(jamx);
        jumlah.setText(jmlx);
        presentase.setText(hasilx);
        keterangan.setText(ketx);

        jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(jam);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasil.getText().toString().trim().equals("")){
                    hasil.setError("Kolom Tidak Boleh Kosong");
                } else {
                    if(presentase.getText().toString().trim().equals("")){
                       presentase.setError("Kolom Tidak Boleh Kosong");
                        } else {
                        if(Double.parseDouble(presentase.getText().toString())>100){
                            presentase.setError("Presentase Tidak Boleh Lebih Dari 100");
                        } else {
                            if(jumlah.getText().toString().trim().equals("")){
                                jumlah.setText("-");
                            }
                            if(keterangan.getText().toString().trim().equals("")){
                                keterangan.setText("-");
                            }
                            updateHasil(kodex,hasil.getText().toString(),presentase.getText().toString(),jam.getText().toString(),
                                    jumlah.getText().toString(),keterangan.getText().toString());
                            alert.dismiss();
                        }
                    }
                }
            }
        });

        alert.show();
    }

    private void DialogForm3(final String kodex,final String uraianx,final String taskx,final String hasilx,
                             final String jamx,final String jmlx,final String ketx,final String disposisix) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_pilih_hasil, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Tindakan : ");

        Button ubah = (Button) dialogView.findViewById(R.id.btnubah);
        Button hapus = (Button) dialogView.findViewById(R.id.btnhapus);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm2(kodex,uraianx,taskx,hasilx,
                jamx,jmlx,ketx,disposisix);
                alert.dismiss();
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm4(kodex,taskx);
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void DialogForm4(final String kodex,final String taskx) {
        AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater2 = getLayoutInflater();
        dialog2.setCancelable(true);
        dialog2.setIcon(R.mipmap.fbr);
        dialog2.setTitle("Peringatan !!!");
        dialog2.setMessage("Apakah Anda Yakin Ingin Menghapus Rencana Kerja "+taskx+"?");

        dialog2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                hapusHasil(kodex);
                dialog.dismiss();
            }
        });

        dialog2.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        dialog2.show();
    }

    private void DialogForm5() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_insert_rencana, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Isi Rencana Kerja : ");

        EditText uraian = (EditText) dialogView.findViewById(R.id.eduraian);
        Button simpan = (Button) dialogView.findViewById(R.id.btnsimpan);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uraian.getText().toString().trim().equals("")) {
                    uraian.setError("Kolom Tidak Boleh Kosong");
                } else {
                    postRencana(uraian.getText().toString());
                    alert.dismiss();
                    }
            }
        });

        alert.show();
    }

    private void DialogForm6() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_pilih_hasil2, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Tindakan : ");

        Button rencana = (Button) dialogView.findViewById(R.id.btnrencana);
        Button hasil = (Button) dialogView.findViewById(R.id.btnhasil);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        rencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm5();
                alert.dismiss();
            }
        });

        hasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm();
                alert.dismiss();
            }
        });

        alert.show();
    }


    public void postHasil(final String Kode,final String Jam,final String Hasil,final String Jumlah,final String Satuan,final String Keterangan){

        Call<ResponseBody> call = jsonapi.postHasil(Kode,Jam,Hasil,Jumlah,Satuan,Keterangan,lat,lon);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            Toast.makeText(MainActivity2.this, "Laporan Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                            getHasil();

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity2.this,"Gagal Buat Laporan", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity2.this,"Gagal Buat Laporan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postRencana(final String uraian){

        Call<ResponseBody> call = jsonapi.postRencana(kode,uraian,id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            Toast.makeText(MainActivity2.this, "Rencana Kerja Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                            getRencana();

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity2.this,"Gagal Buat Rencana Kerja", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity2.this,"Gagal Buat Rencana Kerja", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateHasil(final String kodex,final String uraianx,final String hasilx,
                            final String jamx,final String jmlx,final String ketx){

        Call<ResponseBody> call = jsonapi.updateHasil(kodex,uraianx,hasilx,jamx,jmlx,ketx);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){
                            Toast.makeText(MainActivity2.this, "Laporan Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            getRencana();

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity2.this,"Gagal Ubah Laporan", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity2.this,"Gagal Ubah Laporan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hapusHasil(final String Kode,final String Jam,final String Hasil,final String Jumlah,final String Satuan,final String Keterangan){

        Call<ResponseBody> call = jsonapi.hapusHasil(Kode,Jam,Hasil,Jumlah,Satuan,Keterangan);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            Toast.makeText(MainActivity2.this, "Laporan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            getHasil();

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity2.this,"Gagal Hapus Laporan", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity2.this,"Gagal Hapus Laporan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTimeDialog(final TextView tvjam) {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                tvjam.setText(hourOfDay+":"+minute);
            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

}