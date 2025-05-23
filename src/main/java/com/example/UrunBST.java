package com.example;

import java.util.function.Consumer;

public class UrunBST {
    UrunNode kok;

    public void ekle(UrunNode yeni) {
        kok = ekleRecursif(kok, yeni);
    }

    private UrunNode ekleRecursif(UrunNode mevcut, UrunNode yeni) {
        if (mevcut == null) return yeni;

        if (yeni.fiyat < mevcut.fiyat) {
            mevcut.left = ekleRecursif(mevcut.left, yeni);
        } else if (yeni.fiyat > mevcut.fiyat) {
            mevcut.right = ekleRecursif(mevcut.right, yeni);
        } else {
            return mevcut; // Aynı fiyat varsa ekleme yapılmaz
        }

        mevcut.height = 1 + Math.max(yukseklikGetir(mevcut.left), yukseklikGetir(mevcut.right));
        int denge = dengeFarkiGetir(mevcut);

        // AVL dengesizliklerine karşı önlemler
        if (denge > 1 && yeni.fiyat < mevcut.left.fiyat) return sagaDondur(mevcut);
        if (denge < -1 && yeni.fiyat > mevcut.right.fiyat) return solaDondur(mevcut);
        if (denge > 1 && yeni.fiyat > mevcut.left.fiyat) {
            mevcut.left = solaDondur(mevcut.left);
            return sagaDondur(mevcut);
        }
        if (denge < -1 && yeni.fiyat < mevcut.right.fiyat) {
            mevcut.right = sagaDondur(mevcut.right);
            return solaDondur(mevcut);
        }

        return mevcut;
    }

    public void sil(double fiyat) {
        kok = silRecursif(kok, fiyat);
    }

    private UrunNode silRecursif(UrunNode mevcut, double fiyat) {
        if (mevcut == null) return null;

        if (fiyat < mevcut.fiyat) {
            mevcut.left = silRecursif(mevcut.left, fiyat);
        } else if (fiyat > mevcut.fiyat) {
            mevcut.right = silRecursif(mevcut.right, fiyat);
        } else {
            if ((mevcut.left == null) || (mevcut.right == null)) {
                UrunNode gecici = (mevcut.left != null) ? mevcut.left : mevcut.right;
                if (gecici == null) {
                    mevcut = null;
                } else {
                    mevcut = gecici;
                }
            } else {
                UrunNode enKucuk = enKucukDeger(mevcut.right);
                mevcut.fiyat = enKucuk.fiyat;
                mevcut.urunKodu = enKucuk.urunKodu;
                mevcut.urunAdi = enKucuk.urunAdi;
                mevcut.kategori = enKucuk.kategori;
                mevcut.marka = enKucuk.marka;
                mevcut.right = silRecursif(mevcut.right, enKucuk.fiyat);
            }
        }

        if (mevcut == null) return null;

        mevcut.height = 1 + Math.max(yukseklikGetir(mevcut.left), yukseklikGetir(mevcut.right));
        int denge = dengeFarkiGetir(mevcut);

        // AVL dengesizlik kontrolleri
        if (denge > 1 && dengeFarkiGetir(mevcut.left) >= 0) return sagaDondur(mevcut);
        if (denge > 1 && dengeFarkiGetir(mevcut.left) < 0) {
            mevcut.left = solaDondur(mevcut.left);
            return sagaDondur(mevcut);
        }
        if (denge < -1 && dengeFarkiGetir(mevcut.right) <= 0) return solaDondur(mevcut);
        if (denge < -1 && dengeFarkiGetir(mevcut.right) > 0) {
            mevcut.right = sagaDondur(mevcut.right);
            return solaDondur(mevcut);
        }

        return mevcut;
    }

    private UrunNode enKucukDeger(UrunNode node) {
        UrunNode aktif = node;
        while (aktif.left != null)
            aktif = aktif.left;
        return aktif;
    }


public void fiyataGoreListele(Consumer<UrunNode> callback) {
    inorderSirala(kok, callback);
}
private void inorderSirala(UrunNode node, Consumer<UrunNode> callback) {
    if (node != null) {
        inorderSirala(node.left, callback);
        callback.accept(node);
        inorderSirala(node.right, callback);
    }
}


public void fiyataGoreTersListele(Consumer<UrunNode> callback) {
    tersInorderSirala(kok, callback);
}
private void tersInorderSirala(UrunNode node, Consumer<UrunNode> callback) {
    if (node != null) {
        tersInorderSirala(node.right, callback);
        callback.accept(node);
        tersInorderSirala(node.left, callback);
    }
}


    // AVL Yardımcı Metotlar

    private int yukseklikGetir(UrunNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int dengeFarkiGetir(UrunNode node) {
        return (node == null) ? 0 : yukseklikGetir(node.left) - yukseklikGetir(node.right);
    }

    private UrunNode sagaDondur(UrunNode y) {
        UrunNode x = y.left;
        UrunNode t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(yukseklikGetir(y.left), yukseklikGetir(y.right)) + 1;
        x.height = Math.max(yukseklikGetir(x.left), yukseklikGetir(x.right)) + 1;

        return x;
    }

    private UrunNode solaDondur(UrunNode x) {
        UrunNode y = x.right;
        UrunNode t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(yukseklikGetir(x.left), yukseklikGetir(x.right)) + 1;
        y.height = Math.max(yukseklikGetir(y.left), yukseklikGetir(y.right)) + 1;

        return y;
    }
}
