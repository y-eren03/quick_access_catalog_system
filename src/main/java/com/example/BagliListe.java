package com.example;

public class BagliListe {
    UrunNode head = null;
    UrunNode tail = null;

    public void sonaEkle(UrunNode yeni) {
        if (head == null) {
            head = tail = yeni;
        } else {
            tail.next = yeni;
            tail = yeni;
        }
    }

    public boolean sil(String urunKodu) {
        UrunNode current = head;
        UrunNode previous = null;

        while (current != null) {
            if (current.urunKodu.equals(urunKodu)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                    if (current == tail) tail = previous;
                }
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    public UrunNode ara(String urunKodu) {
        UrunNode current = head;
        while (current != null) {
            if (current.urunKodu.equals(urunKodu)) return current;
            current = current.next;
        }
        return null;
    }

    public void listele() {
        UrunNode temp = head;
        while (temp != null) {
            System.out.println("  " + temp);
            temp = temp.next;
        }
    }

    public void kategoriyeGoreListele(String kategori) {
        UrunNode temp = head;
        while (temp != null) {
            if (temp.kategori.equalsIgnoreCase(kategori)) {
                System.out.println(temp);
            }
            temp = temp.next;
        }
    }

    public void markayaGoreListele(String marka) {
        UrunNode temp = head;
        while (temp != null) {
            if (temp.marka.equalsIgnoreCase(marka)) {
                System.out.println(temp);
            }
            temp = temp.next;
        }
    }
}
