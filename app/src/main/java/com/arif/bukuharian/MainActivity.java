package com.arif.bukuharian;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arif.bukuharian.Adapter.ActivityMasterAdapter;
import com.arif.bukuharian.Adapter.PegawaiAdapter;
import com.arif.bukuharian.Model.ActivityMasterData;
import com.arif.bukuharian.Model.PegawaiData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationListener {

    SharedPreferences sharedpreferences;
    Boolean session = false;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    String id,nama,supervisorid,namadivisi,kodejabatan,tglaw,tglak,lat="0.0",lon="0.0";
    String idpegawai,namapegawai,divisipegawai,namadivisipegawai,jabatanpegawai;
    String kodemaster;
    TextView tvnama,tvtglawal,tvtglakhir;
    ImageView ivacc;
    CircleImageView btnrefresh,btntambah;
    Spinner spinner;
    Button btntgl;
    ListView listView;
    List<ActivityMasterData> itemList = new ArrayList<ActivityMasterData>();
    List<PegawaiData> daftarpegawai = new ArrayList<PegawaiData>();
    ActivityMasterAdapter adapter;
    PegawaiAdapter pegawaiAdapter;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter,dateFormatter2;
    JSONAPI jsonapi;
    LocationManager locationManager;
    String kdbln,kdhari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent objIntent = getIntent();
//        id = objIntent.getStringExtra("ID");
//        nama = objIntent.getStringExtra("Nama");
//        kodejabatan = objIntent.getStringExtra("KodeJabatan");
//        supervisorid = objIntent.getStringExtra("SupervisorID");

        findViewById();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi = retrofit.create(JSONAPI.class);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString("ID",null);
        nama = sharedpreferences.getString("Nama",null);
        namadivisi = sharedpreferences.getString("NamaDivisi",null);
        kodejabatan = sharedpreferences.getString("Jabatan",null);
        supervisorid = sharedpreferences.getString("SupervisorID",null);

        if (session) {
            Toast.makeText(MainActivity.this,"Selamat Datang : "+nama,Toast.LENGTH_SHORT).show();
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//            }
//            getLocation();
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putBoolean(LoginActivity.session_status, false);
//            editor.putString(id, null);
//            editor.putString(nama, null);
//            editor.putString(supervisorid, null);
//            editor.putString(kodejabatan, null);
//            editor.commit();
//
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            finish();
//            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

        pegawaiAdapter = new PegawaiAdapter(MainActivity.this, daftarpegawai);
        spinner.setAdapter(pegawaiAdapter);
        adapter = new ActivityMasterAdapter(MainActivity.this, itemList);
        listView.setAdapter(adapter);

        tvnama.setText(nama);
        setCurrentDate();
        if(kodejabatan.equals("SP01")){
            getPegawai();
        } else {
            daftarpegawai.clear();
            pegawaiAdapter.notifyDataSetChanged();
            PegawaiData  item2 = new PegawaiData();
            item2.setID(id);
            item2.setNama(nama);
            item2.setNamaDivisi(namadivisipegawai);
            item2.setKodeJabatan(kodejabatan);
            daftarpegawai.add(item2);
            pegawaiAdapter.notifyDataSetChanged();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                //tvsalesid.setText("Pendidikan Terakhir : " + listsalesman.get(position).getSalesid());
                idpegawai = daftarpegawai.get(position).getID();
                namapegawai = daftarpegawai.get(position).getNama();
                namadivisipegawai = daftarpegawai.get(position).getNamaDivisi();
                jabatanpegawai = daftarpegawai.get(position).getKodeJabatan();
                getActivityMaster(daftarpegawai.get(position).getID());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        tvtglawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("awal");
            }
        });

        tvtglakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("akhir");
            }
        });

        btntgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivityMaster(idpegawai);
                //Toast.makeText(MainActivity.this,tglaw+"____"+tglak,Toast.LENGTH_SHORT).show();
            }
        });

        tvnama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm3();
            }
        });

        ivacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm3();
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivityMaster(idpegawai);
                //Toast.makeText(MainActivity.this,tglaw+"____"+tglak,Toast.LENGTH_SHORT).show();
            }
        });

        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateKode();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view,
                                    int position, long kdnota) {
                // TODO Auto-generated method stub
                final String kode = itemList.get(position).getKode();
                final String divisi = itemList.get(position).getBagian();
                final String tanggal = itemList.get(position).getTanggal();
                final String nama = itemList.get(position).getNama();
                final String status = itemList.get(position).getStatus();

                if(kodejabatan.equals("SP01")){
                    DialogMain(id,kodejabatan,nama,kode,tanggal,idpegawai,nama,status,divisi);
                } else {
                    Intent tbIntent = new Intent(MainActivity.this,MainActivity2.class);
                    Bundle b = new Bundle();
                    b.putString("id", id);
                    b.putString("kodejabatan", kodejabatan);
                    b.putString("nama", nama);
                    b.putString("kode", kode);
                    b.putString("tanggal", tanggal);
                    b.putString("idpegawai", idpegawai);
                    b.putString("namapegawai", namapegawai);
                    b.putString("jabatanpegawai", jabatanpegawai);
                    b.putString("status", status);
                    b.putString("divisi", divisi);
                    b.putString("lat", lat);
                    b.putString("lon", lon);

                    tbIntent.putExtras(b);
                    /*startActivity(tbIntent);*/
                    startActivityForResult(tbIntent, 1);
                }

            }
        });
    }

    private void findViewById() {
        ivacc = findViewById(R.id.ivacc);
        tvnama = findViewById(R.id.tvnama);
        tvtglawal = findViewById(R.id.tglawal);
        tvtglakhir = findViewById(R.id.tglakhir);
        listView = findViewById(R.id.lv);
        btntgl = findViewById(R.id.btntgl);
        btnrefresh = findViewById(R.id.btnrefresh);
        btntambah = findViewById(R.id.btntambah);
        spinner = findViewById(R.id.spinner);
    }

    private void setCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String tglskr = df.format(c.getTime());
        /*tglAwal.setText(tglskr);*/
        tvtglakhir.setText(tglskr);
        String tglak2=tvtglakhir.getText().toString();
        String tglaw2="01-"+tglak2.substring(3, 5)+"-"+tglak2.substring(6, 10);
        tvtglawal.setText(tglaw2);
        tglak = tglak2.substring(6, 10)+"-"+tglak2.substring(3, 5)+"-"+tglak2.substring(0, 2);
        tglaw = tglak2.substring(6, 10)+"-"+tglak2.substring(3, 5)+"-01";
        //getActivityMaster(idpegawai);
    }

    private void showDateDialog(final String stat){
        dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */

                if (stat.equals("awal")) {
                    tvtglawal.setText(dateFormatter2.format(newDate.getTime()));
                    tglaw = dateFormatter.format(newDate.getTime());
                } else {
                    tvtglakhir.setText(dateFormatter2.format(newDate.getTime()));
                    tglak = dateFormatter.format(newDate.getTime());
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void generateKode(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        final String currentMonth = sdf.format(new Date());

        if (currentMonth.equals("01")){
            kdbln = "A";
        } else if (currentMonth.equals("02")){
            kdbln = "B";
        } else if (currentMonth.equals("03")){
            kdbln = "C";
        } else if (currentMonth.equals("04")){
            kdbln = "D";
        } else if (currentMonth.equals("05")){
            kdbln = "E";
        } else if (currentMonth.equals("06")){
            kdbln = "F";
        } else if (currentMonth.equals("07")){
            kdbln = "G";
        } else if (currentMonth.equals("08")){
            kdbln = "H";
        } else if (currentMonth.equals("09")){
            kdbln = "I";
        } else if (currentMonth.equals("10")){
            kdbln = "J";
        } else if (currentMonth.equals("11")){
            kdbln = "K";
        } else if (currentMonth.equals("12")){
            kdbln = "L";
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        final String currentDay = sdf2.format(new Date());

        if (currentDay.equals("01")){
            kdhari = "ZA";
        } else if (currentDay.equals("02")){
            kdhari = "ZB";
        } else if (currentDay.equals("03")){
            kdhari = "ZC";
        } else if (currentDay.equals("04")){
            kdhari = "ZD";
        } else if (currentDay.equals("05")){
            kdhari = "ZE";
        } else if (currentDay.equals("06")){
            kdhari = "ZF";
        } else if (currentDay.equals("07")){
            kdhari = "ZG";
        } else if (currentDay.equals("08")){
            kdhari = "ZH";
        } else if (currentDay.equals("09")){
            kdhari = "ZI";
        } else if (currentDay.equals("10")){
            kdhari = "AZ";
        } else if (currentDay.equals("11")){
            kdhari = "AA";
        } else if (currentDay.equals("12")){
            kdhari = "AB";
        } else if (currentDay.equals("13")){
            kdhari = "AC";
        } else if (currentDay.equals("14")){
            kdhari = "AD";
        } else if (currentDay.equals("15")){
            kdhari = "AE";
        } else if (currentDay.equals("16")){
            kdhari = "AF";
        } else if (currentDay.equals("17")){
            kdhari = "AG";
        } else if (currentDay.equals("18")){
            kdhari = "AH";
        } else if (currentDay.equals("19")){
            kdhari = "AI";
        } else if (currentDay.equals("20")){
            kdhari = "BZ";
        } else if (currentDay.equals("21")){
            kdhari = "BA";
        } else if (currentDay.equals("22")){
            kdhari = "BB";
        } else if (currentDay.equals("23")){
            kdhari = "BC";
        } else if (currentDay.equals("24")){
            kdhari = "BD";
        } else if (currentDay.equals("25")){
            kdhari = "BE";
        } else if (currentDay.equals("26")){
            kdhari = "BF";
        } else if (currentDay.equals("27")){
            kdhari = "BG";
        } else if (currentDay.equals("28")){
            kdhari = "BH";
        } else if (currentDay.equals("29")){
            kdhari = "BI";
        } else if (currentDay.equals("30")){
            kdhari = "CZ";
        } else if (currentDay.equals("31")){
            kdhari = "CA";
        }

        SimpleDateFormat sdf3 = new SimpleDateFormat("yy");
        final String currentYear = sdf3.format(new Date());

        //kode=area.substring(0,2)+"/"+devid+"/SP/"+currentYear+"/"+kdbln+kdhari;
        String kode = idpegawai + "/TL/" + currentYear + "/" + kdbln+kdhari;
        DialogCreateMaster(kode);
    }

    public void getKode(final String kode){

        Call<ResponseBody> call = jsonapi.getKodeRencana(kode);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());

                        String last = jobj.getString("last");
                        String kdnota = kode + last;
//                        DialogCreateMaster(kdnota);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"Gagal Buat Daftar Rencana Baru, Cek koneksi Atatu Hubungi Admin",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Gagal Buat Daftar Rencana Baru, Cek koneksi Atatu Hubungi Admin",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPegawai(){

        daftarpegawai.clear();
        pegawaiAdapter.notifyDataSetChanged();

        Call<List<PegawaiData>> call = jsonapi.getPegawai(id);

        call.enqueue(new Callback<List<PegawaiData>>() {
            @Override
            public void onResponse(Call<List<PegawaiData>> call, retrofit2.Response<List<PegawaiData>> response) {

                if(response.isSuccessful()){
                    PegawaiData  item2 = new PegawaiData();
                    item2.setID(id);
                    item2.setNama(nama);
                    item2.setNamaDivisi(namadivisipegawai);
                    item2.setKodeJabatan(kodejabatan);
                    if (response.body().size()>0) {
                        List<PegawaiData> posts = response.body();

                        for (PegawaiData post : posts) {
                            PegawaiData item = new PegawaiData();
                            item.setID(post.getID());
                            item.setNama(post.getNama());
                            item.setNamaDivisi(post.getNamaDivisi());
                            item.setKodeJabatan(post.getKodeJabatan());

                            // menambah item ke array
                            daftarpegawai.add(item);
                        }
                        pegawaiAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this,"Data Pegawai Tidak Ditemukan Hubungi admin",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"Data Pegawai Tidak Ditemukan Hubungi admin",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PegawaiData>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Data Pegawai Tidak Ditemukan Hubungi admin",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getActivityMaster(final String idpegawai){
        itemList.clear();
        adapter.notifyDataSetChanged();
        Call<List<ActivityMasterData>> call = jsonapi.getActivityMaster(idpegawai,tglaw,tglak);

        call.enqueue(new Callback<List<ActivityMasterData>>() {
            @Override
            public void onResponse(Call<List<ActivityMasterData>> call, retrofit2.Response<List<ActivityMasterData>> response) {
                DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
                if(response.isSuccessful()){
                    List<ActivityMasterData> posts = response.body();

                    for (ActivityMasterData post: posts){
                        ActivityMasterData item = new ActivityMasterData();

                        item.setKode(post.getKode());
                        item.setNama(post.getNama());
                        item.setTanggal(post.getTanggal());
                        item.setBagian(post.getBagian());
                        item.setJabatan(post.getJabatan());
                        item.setCreateByID(post.getCreateByID());
                        item.setCreateByNama(post.getCreateByNama());
                        item.setStatus(post.getStatus());
                        item.setCheckBy(post.getCheckBy());
                        item.setCheckDate(post.getCheckDate());

                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this,"Data Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ActivityMasterData>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Gagal ambil data, Silahkan cek koneksi dan refresh data",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void postMaster(final String kode,final String tanggal){

        Call<ResponseBody> call = jsonapi.postMaster(kode,tanggal,idpegawai,divisipegawai,id,nama);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            Toast.makeText(MainActivity.this, "Berhasil Membuat Daftar Rencana", Toast.LENGTH_SHORT).show();
                            getActivityMaster(idpegawai);

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity.this,"Gagal Membuat Daftar Rencana", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MainActivity.this,"Gagal Membuat Daftar Rencana", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateMaster(final String kode,final String status,final String alasan){

        Call<ResponseBody> call = jsonapi.updateMaster(kode,status,id,nama,alasan);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            Toast.makeText(MainActivity.this, "Berhasil Update Rencana", Toast.LENGTH_SHORT).show();
                            getActivityMaster(idpegawai);

                        } else {
                            // Jika login gagal
                            Toast.makeText(MainActivity.this,"Gagal Update Rencana", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MainActivity.this,"Gagal Update Rencana", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getakun(){

        Call<ResponseBody> call = jsonapi.getAkun(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            String ID = jobj.getString("ID");
                            String Nama = jobj.getString("Nama");
                            String Divisi = jobj.getString("Divisi");
                            String Password = jobj.getString("Password");
                            String Supervisor = jobj.getString("Supervisor");
                            String Alamat = jobj.getString("Alamat");
                            String Lat = jobj.getString("Lat");
                            String Lon = jobj.getString("Lon");

                            DialogForm4(ID,Nama,Password,Divisi,Supervisor,Alamat,Lat,Lon);
                        } else {
                            // Jika login gagal

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateakun(final String id,final String password){

        Call<ResponseBody> call = jsonapi.updateAkun(id,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //date.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        if(String.valueOf(location.getLatitude()).equals("0.0")){
            DialogForm1();
        } else {
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
            Toast.makeText(MainActivity.this,lat+","+lon,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        DialogForm1();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void DialogForm1() {
        AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater2 = getLayoutInflater();
        dialog2.setCancelable(false);
        dialog2.setIcon(R.mipmap.fbr);
        dialog2.setTitle("MOHON AKTIFKAN GPS");

        dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getLocation();
                dialog.dismiss();
            }
        });


        dialog2.show();
    }

    private void DialogForm2() {
        AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater2 = getLayoutInflater();
        dialog2.setCancelable(false);
        dialog2.setIcon(R.mipmap.fbr);
        dialog2.setTitle("Gagal Update Rencana");
        dialog2.setMessage("Rencana Kerja Yang Statusnya Telah Di Tolak Atau Diterima Tidak Dapat Diubah Lagi." +
                " Silahkan Hubungi Admin Untuk Ubah Status Rencana Kerja");

        dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog2.show();
    }

    private void DialogForm3() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.opsiakun, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Menu : ");

        Button btnlogout = (Button) dialogView.findViewById(R.id.btnlogout);
        Button btnkonf = (Button) dialogView.findViewById(R.id.btnconfig);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        btnkonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getakun();
                alert.dismiss();
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(id, null);
                editor.putString(nama, null);
                editor.putString(supervisorid, null);
                editor.putString(kodejabatan, null);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void DialogForm4(final String id,final String nama,final String password,
                             final String divisi,final String spv,final String alamat,
                             final String lat,final String lon) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_konfigurasi, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Menu : ");

        EditText edid = (EditText) dialogView.findViewById(R.id.edid);
        EditText ednama = (EditText) dialogView.findViewById(R.id.ednama);
        EditText edpassword = (EditText) dialogView.findViewById(R.id.edpassword);
        EditText eddivisi = (EditText) dialogView.findViewById(R.id.eddivisi);
        EditText edspv = (EditText) dialogView.findViewById(R.id.edspv);
        EditText edalamat = (EditText) dialogView.findViewById(R.id.edalamat);
        EditText edlokasi = (EditText) dialogView.findViewById(R.id.edlokasi);
        Button btnlokasi = (Button) dialogView.findViewById(R.id.btnlokasi);
        Button btnlokasi2 = (Button) dialogView.findViewById(R.id.btnlokasi2);
        Button btnupdate = (Button) dialogView.findViewById(R.id.btnupdate);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        edid.setText(id);
        ednama.setText(nama);
        edpassword.setText(password);
        eddivisi.setText(divisi);
        edspv.setText(spv);
        edalamat.setText(alamat);
        edlokasi.setText(lat+","+lon);

        btnlokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Menu Belum tersedia",Toast.LENGTH_SHORT).show();
            }
        });

        btnlokasi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Menu Belum tersedia",Toast.LENGTH_SHORT).show();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateakun(id,edpassword.getText().toString());
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void DialogMain(final String id,final String kodejabatan,final String nama,final String kode,final String tanggal,final String idpegawai,final String namapegawai,final String status
            ,final String divisi) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.opsimain, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Menu : ");

        Button btnubah = (Button) dialogView.findViewById(R.id.btnubah);
        Button btnterima = (Button) dialogView.findViewById(R.id.btnterima);
        Button btntolak = (Button) dialogView.findViewById(R.id.btntolak);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        btnubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tbIntent = new Intent(MainActivity.this,MainActivity2.class);
                Bundle b = new Bundle();
                b.putString("id", id);
                b.putString("kodejabatan", kodejabatan);
                b.putString("nama", nama);
                b.putString("kode", kode);
                b.putString("tanggal", tanggal);
                b.putString("idpegawai", idpegawai);
                b.putString("namapegawai", namapegawai);
                b.putString("jabatanpegawai", jabatanpegawai);
                b.putString("status", status);
                b.putString("divisi", divisi);
                b.putString("lat", lat);
                b.putString("lon", lon);

                tbIntent.putExtras(b);
                /*startActivity(tbIntent);*/
                startActivityForResult(tbIntent, 1);
                alert.dismiss();
            }
        });

        btnterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("A")){
                    updateMaster(kode,"C","");
                } else {
                    DialogForm2();
                }
                alert.dismiss();
            }
        });

        btntolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("A")){
                    DialogAlasan(kode,"V");
                } else {
                    DialogForm2();
                }
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void DialogAlasan(final String kode,final String status) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_alasan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle(" Pilih Menu : ");

        Button btnsimpan = (Button) dialogView.findViewById(R.id.btnsimpan);
        EditText edketerangan = (EditText) dialogView.findViewById(R.id.edketerangan);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edketerangan.getText().toString().trim().equals("")){
                    edketerangan.setError("Isi Alasan Penolakan");
                } else {
                    updateMaster(kode,status,edketerangan.getText().toString());
                }
                alert.dismiss();
            }
        });

        alert.show();
    }

    private void DialogCreateMaster(final String kode) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.form_master_rencana, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.fbr);
        dialog.setTitle("Pilih Tanggal Rencana Baru ");

        TextView tvnama = (TextView) dialogView.findViewById(R.id.tvnama);
        TextView tvbagian = (TextView) dialogView.findViewById(R.id.tvbagian);
        TextView tvtanggal = (TextView) dialogView.findViewById(R.id.tvtanggal);
        Button btnsimpan = (Button) dialogView.findViewById(R.id.btnsimpan);
        Button btntanggal = (Button) dialogView.findViewById(R.id.btntanggal);

        final AlertDialog alert = dialog.create();
        alert.setView(dialogView);

        tvnama.setText(namapegawai);
        tvbagian.setText(namadivisi);

        btntanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                SimpleDateFormat dateFormatter4 = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

                Calendar newCalendar = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        tvtanggal.setText(dateFormatter3.format(newDate.getTime()));
                        tvtglakhir.setText(dateFormatter4.format(newDate.getTime()));
                        tglak = dateFormatter3.format(newDate.getTime());
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        tvtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                SimpleDateFormat dateFormatter4 = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

                Calendar newCalendar = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvtanggal.setText(dateFormatter3.format(newDate.getTime()));
                        tvtglakhir.setText(dateFormatter4.format(newDate.getTime()));
                        tglak = dateFormatter3.format(newDate.getTime());

                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             postMaster(kode,tvtanggal.getText().toString());
                alert.dismiss();
            }
        });

        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            getActivityMaster(idpegawai);
        }

    }

}