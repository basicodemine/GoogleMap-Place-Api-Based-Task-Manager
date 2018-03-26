package gorev.yerservis.com.gorevgo;

import java.io.Serializable;



public class TaskItem implements Serializable {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getEnlem() {
        return enlem;
    }

    public void setEnlem(String enlem) {
        this.enlem = enlem;
    }

    public String getBoylam() {
        return boylam;
    }

    public void setBoylam(String boylam) {
        this.boylam = boylam;
    }

    public String getBitistarih() {
        return bitistarih;
    }

    public void setBitistarih(String bitistarih) {
        this.bitistarih = bitistarih;
    }

    public String getBaslangictarih() {
        return baslangictarih;
    }

    public void setBaslangictarih(String baslangictarih) {
        this.baslangictarih = baslangictarih;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public TaskItem(Long id, String baslik, String icerik, String enlem, String boylam, String bitistarih, String baslangictarih, String adres, String saat, String cap, String durum) {

        this.id = id;
        this.baslik = baslik;
        this.icerik = icerik;
        this.enlem = enlem;
        this.boylam = boylam;
        this.bitistarih = bitistarih;
        this.baslangictarih = baslangictarih;
        this.adres = adres;
        this.saat = saat;
        this.cap = cap;
        this.durum = durum;
    }

    public TaskItem(String baslik, String icerik, String enlem, String boylam, String bitistarih, String baslangictarih, String adres, String saat, String cap, String durum) {

        this.baslik = baslik;
        this.icerik = icerik;
        this.enlem = enlem;
        this.boylam = boylam;
        this.bitistarih = bitistarih;
        this.baslangictarih = baslangictarih;
        this.adres = adres;
        this.saat = saat;
        this.cap = cap;
        this.durum = durum;
    }

    Long id;
    String baslik = "";
    String icerik = "";
    String enlem = "";
    String boylam = "";
    String bitistarih = "";
    String baslangictarih = "";
    String adres = "";
    String saat = "";
    String cap = "";
    String durum = "";
}
