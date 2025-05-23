package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainControl {

    // Arama TextField'ları
    @FXML private TextField aramaKodField, aramaAdField, aramaKategoriField, aramaMarkaField, aramaFiyatField;

    // Ekleme TextField'ları
    @FXML private TextField ekleKodField, ekleAdField, ekleKategoriField, ekleMarkaField, ekleFiyatField;

    // Tablo ve kolonlar
    @FXML private TableView<UrunNode> urunTablosu;
    @FXML private TableColumn<UrunNode, String> kodKolon, adKolon, kategoriKolon, markaKolon;
    @FXML private TableColumn<UrunNode, Double> fiyatKolon;
    @FXML private TableColumn<UrunNode, Void> silKolon;

    

    private HashTable hashTable = new HashTable(10);
    private boolean fiyatArtanMi = true;

    @FXML
    public void initialize() {
        // Sütun verilerini bağla
        kodKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().urunKodu));
        adKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().urunAdi));
        kategoriKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().kategori));
        markaKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().marka));
        fiyatKolon.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().fiyat).asObject());
        
        // Tabloya verileri yükle
        urunTablosu.setItems(hashTable.getObservableList());
        
        ortalaHucre(kodKolon);
        ortalaHucre(adKolon);
        ortalaHucre(kategoriKolon);
        ortalaHucre(markaKolon);
        ortalaHucre(fiyatKolon);

        Button fiyatButton = new Button("FİYAT");
        fiyatButton.setStyle("-fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: transparent;");

    fiyatButton.setOnAction(e -> {
        
        ObservableList<UrunNode> siraliListe = FXCollections.observableArrayList();
        if (fiyatArtanMi) {
            hashTable.fiyataGoreListele(siraliListe::add);
        } else {
            hashTable.fiyataGoreTersListele(siraliListe::add);
        }
        urunTablosu.setItems(siraliListe);
        fiyatArtanMi = !fiyatArtanMi;
    });

    
        fiyatKolon.setText(""); 
        fiyatKolon.setGraphic(fiyatButton);

        // Sil butonunu oluştur
        silKolon.setCellFactory(param -> new TableCell<>() {
            private final Button silButton = new Button("SİL");

            {
                silButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                silButton.setOnAction(event -> {
                    UrunNode urun = getTableView().getItems().get(getIndex());
                    hashTable.urunSil(urun.urunKodu);
                    urunTablosu.setItems(hashTable.getObservableList());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(silButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private <T> void ortalaHucre(TableColumn<UrunNode, T> kolon) {
    kolon.setCellFactory(tc -> new TableCell<>() {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                setAlignment(Pos.CENTER); // HÜCREYİ ORTALAR
            }
        }
    });
}

   
    @FXML
    public void urunEkle() {
        String kod = ekleKodField.getText().trim();
        String ad = ekleAdField.getText().trim();
        String kategori = ekleKategoriField.getText().trim();
        String marka = ekleMarkaField.getText().trim();
        String fiyatText = ekleFiyatField.getText().trim();

        if (kod.isEmpty() || ad.isEmpty() || kategori.isEmpty() || marka.isEmpty() || fiyatText.isEmpty()) {
            gosterUyari("Lütfen tüm alanları doldurun!");
            return;
        }

        double fiyat;
        try {
            fiyat = Double.parseDouble(fiyatText);
        } catch (NumberFormatException e) {
            gosterUyari("Fiyat geçerli bir sayı olmalı!");
            return;
        }

        hashTable.urunEkle(kod, ad, kategori, marka, fiyat);
        urunTablosu.setItems(hashTable.getObservableList());
        temizleEkleAlanlari();
    }

    @FXML
    public void urunAra() {
        String kod = aramaKodField.getText().trim();
        String ad = aramaAdField.getText().trim();
        String kategori = aramaKategoriField.getText().trim();
        String marka = aramaMarkaField.getText().trim();
        String fiyatStr = aramaFiyatField.getText().trim();

        ObservableList<UrunNode> tumUrunler = hashTable.getObservableList();
        ObservableList<UrunNode> filtrelenmis = FXCollections.observableArrayList();

         if (!kod.isEmpty() && ad.isEmpty() && kategori.isEmpty() && marka.isEmpty() && fiyatStr.isEmpty()) {
        UrunNode bulunan = hashTable.urunAra(kod);
        if (bulunan != null) {
            filtrelenmis.add(bulunan);
        } else {
            gosterUyari("Kod ile eşleşen ürün bulunamadı.");
        }
        urunTablosu.setItems(filtrelenmis);
        return;
    }



        

        for (UrunNode urun : tumUrunler) {
            boolean eslesiyor = true;

            if (!kod.isEmpty() && !urun.urunKodu.equalsIgnoreCase(kod)) eslesiyor = false;
            if (!ad.isEmpty() && !urun.urunAdi.toLowerCase().contains(ad.toLowerCase())) eslesiyor = false;
            if (!kategori.isEmpty() && !urun.kategori.toLowerCase().contains(kategori.toLowerCase())) eslesiyor = false;
            if (!marka.isEmpty() && !urun.marka.toLowerCase().contains(marka.toLowerCase())) eslesiyor = false;

            if (!fiyatStr.isEmpty()) {
                try {
                    double arananFiyat = Double.parseDouble(fiyatStr);
                    if (urun.fiyat != arananFiyat) eslesiyor = false;
                } catch (NumberFormatException e) {
                    gosterUyari("Fiyat için geçerli bir sayı giriniz!");
                    return;
                }
            }

            if (eslesiyor) filtrelenmis.add(urun);
        }

        if (filtrelenmis.isEmpty()) {
            gosterUyari("Aranan kriterlere uygun ürün bulunamadı.");
        }

        urunTablosu.setItems(filtrelenmis);
    }

    private void temizleEkleAlanlari() {
        ekleKodField.clear();
        ekleAdField.clear();
        ekleKategoriField.clear();
        ekleMarkaField.clear();
        ekleFiyatField.clear();
    }

    private void gosterUyari(String mesaj) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}
