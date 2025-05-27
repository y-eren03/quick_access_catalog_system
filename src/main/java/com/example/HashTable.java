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



        urunEkle("X01", "Kablosuz Mouse", "Elektronik", "Logitech", 319.90);
        urunEkle("X02", "Çamaşır Makinesi", "Ev Aletleri", "Bosch", 7299.00);
        urunEkle("X03", "Spor Ayakkabı", "Giyim", "Nike", 1199.50);
         urunEkle("X04", "Roman Kitabı", "Kitap", "Penguin", 89.99);
         urunEkle("X05", "Futbol Topu", "Spor", "Adidas", 249.00);
        urunEkle("X06", "Bluetooth Hoparlör", "Elektronik", "Yamaha", 799.90);
        urunEkle("X07", "Yapboz Seti", "Oyuncak", "Lego", 349.99);
        urunEkle("X08", "Ruj", "Kozmetik", "Loreal", 149.90);
        urunEkle("X09", "Elektrikli Süpürge", "Ev Aletleri", "Bosch", 3899.00);
        urunEkle("X10", "Kulak Üstü Kulaklık", "Elektronik", "Sony", 1199.00);
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
