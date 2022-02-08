package com.arif.bukuharian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    EditText edusername,edpassword;
    Button btnlogin;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    JSONAPI jsonapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edusername = findViewById(R.id.edusername);
        edpassword = findViewById(R.id.edpassword);
        btnlogin = findViewById(R.id.btnlogin);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi = retrofit.create(JSONAPI.class);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("loading....");
        pDialog.setTitle("Cek Data User");

        sharedpreferences = getSharedPreferences(MainActivity.my_shared_preferences, Context.MODE_PRIVATE);


        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(edusername.getText().toString().trim().equals("")||edpassword.getText().toString().trim().equals("")){
                    edusername.setError("Kolom Wajib Diisi");
                    edpassword.setError("Kolom Wajib Diisi");
                } else {
                    checkLogin2();
                }
            }
        });


    }

    public void checkLogin2(){

        Call<ResponseBody> call = jsonapi.getLogin(edusername.getText().toString(),edpassword.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jobj = new JSONObject(response.body().string());
                        if (jobj.getString("success").equals("1")){

                            String idx = jobj.getString("ID");
                            String namax = jobj.getString("Nama");
                            String namadivisix = jobj.getString("NamaDivisi");
                            String jabatanx = jobj.getString("KodeJabatan");
                            String spvx = jobj.getString("SupervisorID");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(MainActivity.session_status, true);
                            editor.putString("ID", idx);
                            editor.putString("Nama", namax);
                            editor.putString("NamaDivisi", namadivisix);
                            editor.putString("Jabatan", jabatanx);
                            editor.putString("SupervisorID", spvx);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);

//                            Intent tbIntent = new Intent(LoginActivity.this,MainActivity.class);
//                            Bundle b = new Bundle();
//                            b.putString("ID", jobj.getString("ID"));
//                            b.putString("Nama",jobj.getString("Nama"));
//                            b.putString("KodeJabatan", jobj.getString("KodeJabatan"));
//                            b.putString("SupervisorID",jobj.getString("SupervisorID"));
//                            tbIntent.putExtras(b);
//                            finish();
//                            startActivity(tbIntent);

                        } else {
                            // Jika login gagal
                            edusername.setError("ID Pegawai Atau Password Salah");
                            edpassword.setError("ID Pegawai Atau Password Salah");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Login error, Coba cek ulang Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}