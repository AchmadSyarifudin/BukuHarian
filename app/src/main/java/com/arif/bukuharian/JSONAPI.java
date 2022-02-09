package com.arif.bukuharian;


import com.arif.bukuharian.Model.ActivityHasilData;
import com.arif.bukuharian.Model.ActivityMasterData;
import com.arif.bukuharian.Model.ActivityRencanaData;
import com.arif.bukuharian.Model.ActivityRencanaHasilData;
import com.arif.bukuharian.Model.PegawaiData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JSONAPI {

    @GET("selectpegawai.php")
    Call<List<PegawaiData>> getPegawai(@Query("id") String id);

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> getLogin(@Field("id") String id,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("selectakun.php")
    Call<ResponseBody> getAkun(@Field("id") String id);

    @FormUrlEncoded
    @POST("inserthasil.php")
    Call<ResponseBody> postHasil(@Field("Kode") String Kode,
                                   @Field("Jam") String Jam,
                                   @Field("Hasil") String Hasil,
                                   @Field("Jumlah") String Jumlah,
                                   @Field("Satuan") String Satuan,
                                   @Field("Keterangan") String Keterangan,
                                   @Field("Lat") String Lat,
                                   @Field("Lon") String Lon);

    @FormUrlEncoded
    @POST("insertrencana.php")
    Call<ResponseBody> postRencana(@Field("kode") String kode,
                                   @Field("uraian") String uraian,
                                   @Field("id") String id);

    @FormUrlEncoded
    @POST("updatehasil.php")
    Call<ResponseBody> updateHasil(  @Field("kode") String kode,
                                     @Field("aktifitas") String uraian,
                                     @Field("hasil") String hasil,
                                     @Field("jam") String jam,
                                     @Field("jumlah") String jumlah,
                                     @Field("keterangan") String keterangan);

    @FormUrlEncoded
    @POST("updateakun.php")
    Call<ResponseBody> updateAkun(  @Field("id") String id,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("hapushasil.php")
    Call<ResponseBody> hapusHasil(@Field("Kode") String Kode,
                                   @Field("Jam") String Jam,
                                   @Field("Hasil") String Hasil,
                                   @Field("Jumlah") String Jumlah,
                                   @Field("Satuan") String Satuan,
                                   @Field("Keterangan") String Keterangan);

    @GET("selectactivitymaster.php?")
    Call<List<ActivityMasterData>> getActivityMaster(@Query("id") String id,
                                                     @Query("tglaw") String tglaw,
                                                     @Query("tglak") String tglak);

    @GET("selectactivityrencana.php?")
    Call<List<ActivityRencanaData>> getActivityRencana(@Query("kode") String kode);

    @GET("selectactivityhasil.php?")
    Call<List<ActivityHasilData>> getActivityHasil(@Query("kode") String kode);

    @GET("selectactivitydetail.php?")
    Call<List<ActivityRencanaHasilData>> getActivityRencanaHasil(@Query("kode") String kode);

    @FormUrlEncoded
    @POST("insertmaster.php")
    Call<ResponseBody> postMaster(@Field("kode") String kode,
                                 @Field("tanggal") String tanggal,
                                 @Field("idpegawai") String idpegawai,
                                 @Field("divisi") String divisi,
                                 @Field("id") String id,
                                 @Field("nama") String nama);

    @FormUrlEncoded
    @POST("updatemaster.php")
    Call<ResponseBody> updateMaster(@Field("kode") String kode,
                                  @Field("status") String status,
                                  @Field("id") String id,
                                  @Field("nama") String nama,
                                  @Field("alasan") String alasan);


    @FormUrlEncoded
    @POST("generaterencana.php")
    Call<ResponseBody> getKodeRencana(@Field("kode") String kode);

}
