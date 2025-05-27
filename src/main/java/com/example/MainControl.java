package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class MainControl {


@FXML private TextField aramaKodField;
@FXML private TextField aramaAdField;


@FXML private ChoiceBox<String> filtreKategoriBox;
@FXML private ChoiceBox<String> filtreMarkaBox;
@FXML private TextField minFiyatField;
@FXML private TextField maxFiyatField;


@FXML private TextField ekleKodField;
@FXML private TextField ekleAdField;
@FXML private TextField ekleKategoriField;
@FXML private TextField ekleMarkaField;
@FXML private TextField ekleFiyatField;


@FXML private StackPane ekleMenu;
@FXML private Button eklemenubuton;
@FXML private Label ekleMenuKapatmaLabel;


private boolean fiyatArtanMi = true;


@FXML private TableView<UrunNode> urunTablosu;
@FXML private TableColumn<UrunNode, String> kodKolon, adKolon, kategoriKolon, markaKolon;
@FXML private TableColumn<UrunNode, Double> fiyatKolon;
@FXML private TableColumn<UrunNode, Void> silKolon;



    private HashTable hashTable = new HashTable(10);

    @FXML
    public void initialize() {

        filtreKategoriBox.getItems().addAll(
        "Elektronik", "Ev Aletleri", "Giyim", "Kitap", "Spor", "Müzik", "Oyuncak", "Kozmetik"
        );

        filtreMarkaBox.getItems().addAll(
        "Logitech", "Bosch", "Nike", "Penguin", "Adidas", "Yamaha", "Lego", "Loreal"
         )   ;

        
        kodKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().urunKodu));
        adKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().urunAdi));
        kategoriKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().kategori));
        markaKolon.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().marka));
        fiyatKolon.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().fiyat).asObject());

       
        urunTablosu.setItems(hashTable.getObservableList());



    Button fiyatButton = new Button("Fiyat");
    fiyatButton.setStyle("-fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white;");

    fiyatButton.setOnAction(e -> {
        // Sıralama işlemini burada yap
        ObservableList<UrunNode> siraliListe = FXCollections.observableArrayList();
        if (fiyatArtanMi) {
            hashTable.fiyataGoreListele(siraliListe::add);
        } else {
            hashTable.fiyataGoreTersListele(siraliListe::add);
        }
        urunTablosu.setItems(siraliListe);
        fiyatArtanMi = !fiyatArtanMi;
    });

    // TableColumn başlığı olarak buton koy
    fiyatKolon.setText(""); // Yazıyı temizle
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


        ortalaKolon(kodKolon);
        ortalaKolon(adKolon);
        ortalaKolon(kategoriKolon);
        ortalaKolon(markaKolon);
        ortalaKolon(fiyatKolon);


    }



    public <T> void ortalaKolon(TableColumn<UrunNode, T> kolon) {
    kolon.setCellFactory(tc -> new TableCell<>() {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                setAlignment(Pos.CENTER);
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
        ekleMenuKapatma();

        if (!filtreKategoriBox.getItems().contains(kategori)) {
            filtreKategoriBox.getItems().add(kategori);
            }
        if (!filtreMarkaBox.getItems().contains(marka)) {
            filtreMarkaBox.getItems().add(marka);
            }

    }



    @FXML
    public void filtreleUrunler() {
    String seciliKategori = filtreKategoriBox.getValue();
    String seciliMarka = filtreMarkaBox.getValue();
    String minStr = minFiyatField.getText().trim();
    String maxStr = maxFiyatField.getText().trim();

    double minFiyat = Double.MIN_VALUE;
    double maxFiyat = Double.MAX_VALUE;

    try {
        if (!minStr.isEmpty()) minFiyat = Double.parseDouble(minStr);
        if (!maxStr.isEmpty()) maxFiyat = Double.parseDouble(maxStr);
    } catch (NumberFormatException e) {
        gosterUyari("Lütfen geçerli fiyat değerleri giriniz!");
        return;
    }

    ObservableList<UrunNode> filtrelenmis = FXCollections.observableArrayList();
    for (UrunNode urun : hashTable.getObservableList()) {
        boolean eslesiyor = true;
        if (seciliKategori != null && !urun.kategori.equals(seciliKategori)) eslesiyor = false;
        if (seciliMarka != null && !urun.marka.equals(seciliMarka)) eslesiyor = false;
        if (urun.fiyat < minFiyat || urun.fiyat > maxFiyat) eslesiyor = false;

        if (eslesiyor) filtrelenmis.add(urun);
    }

    if (filtrelenmis.isEmpty()) {
        gosterUyari("Filtreye uyan ürün bulunamadı.");
    }
    urunTablosu.setItems(filtrelenmis);
}


    @FXML
    public void urunAra() {
    String kod = aramaKodField.getText().trim();
    String ad = aramaAdField.getText().trim();
    

    ObservableList<UrunNode> filtrelenmis = FXCollections.observableArrayList();

    
    if (!kod.isEmpty() && ad.isEmpty() ) {
        UrunNode bulunan = hashTable.urunAra(kod);
        if (bulunan != null) {
            filtrelenmis.add(bulunan);
        } else {
            gosterUyari("Kod ile eşleşen ürün bulunamadı.");
        }
        urunTablosu.setItems(filtrelenmis);
        return;
    }

    
    ObservableList<UrunNode> tumUrunler = hashTable.getObservableList();

    for (UrunNode urun : tumUrunler) {
        boolean eslesiyor = true;

        if (!kod.isEmpty() && !urun.urunKodu.equalsIgnoreCase(kod)) eslesiyor = false;
        if (!ad.isEmpty() && !urun.urunAdi.toLowerCase().contains(ad.toLowerCase())) eslesiyor = false;


        if (eslesiyor) filtrelenmis.add(urun);
    }

    if (filtrelenmis.isEmpty()) {
        gosterUyari("Aranan kriterlere uygun ürün bulunamadı.");
    }

    urunTablosu.setItems(filtrelenmis);
}

    @FXML
    public void ekleMenuAc() {
        ekleMenu.setVisible(true);
    }

    @FXML
    public void ekleMenuKapatma() {
        ekleMenu.setVisible(false);
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
