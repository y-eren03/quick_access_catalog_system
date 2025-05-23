package com.example;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HashTable {
    BagliListe[] table;
    int size;
    int urunSayisi = 0;
    UrunBST bst = new UrunBST();
    ObservableList<UrunNode> urunListesi = FXCollections.observableArrayList();

    public HashTable(int size) {
        this.size = size;
        table = new BagliListe[size];
        for (int i = 0; i < size; i++) {
            table[i] = new BagliListe();
        }

        // Başlangıçta 10 örnek ürün ekle
        urunEkle("A01", "Mouse", "Elektronik", "Logitech", 249.99);
        urunEkle("B02", "Klavye", "Elektronik", "Corsair", 499.99);
        urunEkle("C03", "Monitör", "Elektronik", "Samsung", 1999.99);
        urunEkle("D04", "Kulaklık", "Elektronik", "Sony", 899.50);
        urunEkle("E05", "SSD", "Depolama", "Kingston", 1299.00);
        urunEkle("F06", "Harddisk", "Depolama", "WD", 749.49);
        urunEkle("G07", "Laptop", "Bilgisayar", "HP", 13999.90);
        urunEkle("H08", "Tablet", "Mobil", "Apple", 10999.00);
        urunEkle("I09", "Yazıcı", "Ofis", "Canon", 1599.99);
        urunEkle("J10", "Router", "Ağ", "TP-Link", 549.00);
    }

    private int hash(String urunKodu) {
        int toplam = 0;
        for (char ch : urunKodu.toCharArray()) {
            toplam += ch;
        }
        return toplam % size;
    }

    public ObservableList<UrunNode> getObservableList() {
        return urunListesi;
    }

    public UrunBST getBST() {
        return bst;
    }

    public void urunEkle(String kod, String ad, String kategori, String marka, double fiyat) {
        if (urunSayisi >= 100) {
            System.out.println("Hata: Maksimum ürün limiti aşıldı.");
            return;
        }

        if (kod.length() < 3) {
            System.out.println("Hata: Ürün kodu geçersiz.");
            return;
        }

        int index = hash(kod);
        if (table[index].ara(kod) != null) {
            System.out.println("Bu ürün kodu zaten mevcut.");
            return;
        }

        UrunNode yeni = new UrunNode(kod, ad, kategori, marka, fiyat);
        table[index].sonaEkle(yeni);
        bst.ekle(yeni);
        urunListesi.add(yeni);
        urunSayisi++;

        System.out.println("Ürün başarıyla eklendi.");
    }

    public void urunSil(String kod) {
        int index = hash(kod);
        UrunNode silinecek = table[index].ara(kod);

        if (silinecek != null && table[index].sil(kod)) {
            bst.sil(silinecek.fiyat);
            urunListesi.remove(silinecek);
            urunSayisi--;
            System.out.println("Ürün silindi.");
        } else {
            System.out.println("Ürün bulunamadı.");
        }
    }

    public UrunNode urunAra(String urunKodu) {
        int index = hash(urunKodu);
        UrunNode urun = table[index].ara(urunKodu);
        return urun;
    }



    public void fiyataGoreListele(Consumer<UrunNode> a) {
        bst.fiyataGoreListele(a);
    }

    public void fiyataGoreTersListele(Consumer<UrunNode> a) {
        bst.fiyataGoreTersListele(a);
    }

    public void tumUrunleriListele() {
        for (int i = 0; i < size; i++) {
            table[i].listele();
        }
    }
}
