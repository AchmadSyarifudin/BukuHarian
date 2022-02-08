package com.arif.bukuharian.Model;

public class ActivityRencanaHasilData {
    private String Kode_Act, NoItem,KodeTaskList, TaskList, Aktifitas, JamAwal, JamAkhir, Hasil,
            Jml, Keterangan, RClose, IsClose, Approve, Disposisi, CloseDate, CloseBy;

    public ActivityRencanaHasilData(){
    }

    public ActivityRencanaHasilData(String kode_Act, String noItem, String kodeTaskList, String taskList, String aktifitas, String jamAwal, String jamAkhir, String hasil, String jml, String keterangan, String RClose, String isClose, String approve, String disposisi, String closeDate, String closeBy) {
        Kode_Act = kode_Act;
        NoItem = noItem;
        KodeTaskList = kodeTaskList;
        TaskList = taskList;
        Aktifitas = aktifitas;
        JamAwal = jamAwal;
        JamAkhir = jamAkhir;
        Hasil = hasil;
        Jml = jml;
        Keterangan = keterangan;
        this.RClose = RClose;
        IsClose = isClose;
        Approve = approve;
        Disposisi = disposisi;
        CloseDate = closeDate;
        CloseBy = closeBy;
    }

    public String getKode_Act() {
        return Kode_Act;
    }

    public void setKode_Act(String kode_Act) {
        Kode_Act = kode_Act;
    }

    public String getNoItem() {
        return NoItem;
    }

    public void setNoItem(String noItem) {
        NoItem = noItem;
    }

    public String getKodeTaskList() {
        return KodeTaskList;
    }

    public void setKodeTaskList(String kodeTaskList) {
        KodeTaskList = kodeTaskList;
    }

    public String getTaskList() {
        return TaskList;
    }

    public void setTaskList(String taskList) {
        TaskList = taskList;
    }

    public String getAktifitas() {
        return Aktifitas;
    }

    public void setAktifitas(String aktifitas) {
        Aktifitas = aktifitas;
    }

    public String getJamAwal() {
        return JamAwal;
    }

    public void setJamAwal(String jamAwal) {
        JamAwal = jamAwal;
    }

    public String getJamAkhir() {
        return JamAkhir;
    }

    public void setJamAkhir(String jamAkhir) {
        JamAkhir = jamAkhir;
    }

    public String getHasil() {
        return Hasil;
    }

    public void setHasil(String hasil) {
        Hasil = hasil;
    }

    public String getJml() {
        return Jml;
    }

    public void setJml(String jml) {
        Jml = jml;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getRClose() {
        return RClose;
    }

    public void setRClose(String RClose) {
        this.RClose = RClose;
    }

    public String getIsClose() {
        return IsClose;
    }

    public void setIsClose(String isClose) {
        IsClose = isClose;
    }

    public String getApprove() {
        return Approve;
    }

    public void setApprove(String approve) {
        Approve = approve;
    }

    public String getDisposisi() {
        return Disposisi;
    }

    public void setDisposisi(String disposisi) {
        Disposisi = disposisi;
    }

    public String getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(String closeDate) {
        CloseDate = closeDate;
    }

    public String getCloseBy() {
        return CloseBy;
    }

    public void setCloseBy(String closeBy) {
        CloseBy = closeBy;
    }
}
