package com.example;

public class UrunNode {
    String urunKodu;
    String urunAdi;
    String kategori;
    String marka;
    double fiyat;
    
    UrunNode next;

    UrunNode left; // BST için
    UrunNode right;
    int height = 1;


    public UrunNode(String urunKodu, String urunAdi, String kategori, String marka, double fiyat) {
        this.urunKodu = urunKodu;
        this.urunAdi = urunAdi;
        this.kategori = kategori;
        this.marka = marka;
        this.fiyat = fiyat;
    }

    @Override
    public String toString() {
        return "Ürün Kodu: " + urunKodu +
               ", Adı: " + urunAdi +
               ", Kategori: " + kategori +
               ", Marka: " + marka +
               ", Fiyat: " + fiyat + " TL";
    }
}

