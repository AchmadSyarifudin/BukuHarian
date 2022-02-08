package com.arif.bukuharian.Model;

public class ActivityRencanaData {
    private String Kd_Act,Uraian,Jml,Satuan;
    public ActivityRencanaData(){

    }

    public ActivityRencanaData(String kd_Act, String uraian, String jml, String satuan) {
        Kd_Act = kd_Act;
        Uraian = uraian;
        Jml = jml;
        Satuan = satuan;
    }

    public String getKd_Act() {
        return Kd_Act;
    }

    public void setKd_Act(String kd_Act) {
        Kd_Act = kd_Act;
    }

    public String getUraian() {
        return Uraian;
    }

    public void setUraian(String uraian) {
        Uraian = uraian;
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
}
