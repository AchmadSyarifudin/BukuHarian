package com.arif.bukuharian.Model;

public class ActivityHasilData {
    private String Kd_Act, Jam, HasilKerja, Jml, Satuan, Keterangan, Disposisi;
    public ActivityHasilData(){

    }

    public ActivityHasilData(String kd_Act, String jam, String hasilKerja, String jml, String satuan, String keterangan, String disposisi) {
        Kd_Act = kd_Act;
        Jam = jam;
        HasilKerja = hasilKerja;
        Jml = jml;
        Satuan = satuan;
        Keterangan = keterangan;
        Disposisi = disposisi;
    }

    public String getKd_Act() {
        return Kd_Act;
    }

    public void setKd_Act(String kd_Act) {
        Kd_Act = kd_Act;
    }

    public String getJam() {
        return Jam;
    }

    public void setJam(String jam) {
        Jam = jam;
    }

    public String getHasilKerja() {
        return HasilKerja;
    }

    public void setHasilKerja(String hasilKerja) {
        HasilKerja = hasilKerja;
    }

    public String getJml() {
        return Jml;
    }

    public void setJml(String jml) {
        Jml = jml;
    }

    public String getSatuan() {
        return Satuan;
    }

    public void setSatuan(String satuan) {
        Satuan = satuan;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getDisposisi() {
        return Disposisi;
    }

    public void setDisposisi(String disposisi) {
        Disposisi = disposisi;
    }
}
