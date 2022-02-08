package com.arif.bukuharian.Model;
public class PegawaiData {
    private String ID, Nama,NamaDivisi,KodeJabatan;

    public PegawaiData(String ID, String nama, String namaDivisi,String kodeJabatan) {
        this.ID = ID;
        Nama = nama;
        NamaDivisi = namaDivisi;
        KodeJabatan = kodeJabatan;
    }

    public PegawaiData(){

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getNamaDivisi() {
        return NamaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        NamaDivisi = namaDivisi;
    }

    public String getKodeJabatan() {
        return KodeJabatan;
    }

    public void setKodeJabatan(String kodeJabatan) {
        KodeJabatan = kodeJabatan;
    }
}