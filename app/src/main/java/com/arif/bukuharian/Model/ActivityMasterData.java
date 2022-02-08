package com.arif.bukuharian.Model;

public class ActivityMasterData {
    private String Kode,Nama,Tanggal,Bagian,Jabatan,CreateByID,CreateByNama,Status,CheckDate,CheckBy;
    public ActivityMasterData(){

    }

    public ActivityMasterData(String kode, String nama, String tanggal, String bagian, String jabatan, String createByID,String createByNama, String status, String checkDate, String checkBy) {
        Kode = kode;
        Nama = nama;
        Tanggal = tanggal;
        Bagian = bagian;
        Jabatan = jabatan;
        CreateByID = createByID;
        CreateByNama = createByNama;
        Status = status;
        CheckDate = checkDate;
        CheckBy = checkBy;
    }

    public String getKode() {
        return Kode;
    }

    public void setKode(String kode) {
        Kode = kode;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getBagian() {
        return Bagian;
    }

    public void setBagian(String bagian) {
        Bagian = bagian;
    }

    public String getJabatan() {
        return Jabatan;
    }

    public void setJabatan(String jabatan) {
        Jabatan = jabatan;
    }

    public String getCreateByID() {
        return CreateByID;
    }

    public void setCreateByID(String createByID) {
        CreateByID = createByID;
    }

    public String getCreateByNama() {
        return CreateByNama;
    }

    public void setCreateByNama(String createByNama) {
        CreateByNama = createByNama;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCheckDate() {
        return CheckDate;
    }

    public void setCheckDate(String checkDate) {
        CheckDate = checkDate;
    }

    public String getCheckBy() {
        return CheckBy;
    }

    public void setCheckBy(String checkBy) {
        CheckBy = checkBy;
    }
}
