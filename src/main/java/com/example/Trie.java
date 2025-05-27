package com.example;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    
    TrieNode root;

    public void insert(UrunNode urunNode) {
        TrieNode node = root;
        for (char ch : urunNode.urunAdi.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.urunler.add(urunNode);
    }

    public List<UrunNode> searchByPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return new ArrayList<>();
        }
        return collectProducts(node);
    }


    private List<UrunNode> collectProducts(TrieNode node) {
        List<UrunNode> result = new ArrayList<>();
        if (node.isEndOfWord) result.addAll(node.urunler);
        for (TrieNode child : node.children.values()) {
            result.addAll(collectProducts(child));
        }
        return result;
    }

    public Trie() {
        root = new TrieNode();

        insert(new UrunNode("X01", "Kablosuz Mouse", "Elektronik", "Logitech", 319.90));
        insert(new UrunNode("X02", "Çamaşır Makinesi", "Ev Aletleri", "Bosch", 7299.00));
        insert(new UrunNode("X03", "Spor Ayakkabı", "Giyim", "Nike", 1199.50));
        insert(new UrunNode("X04", "Roman Kitabı", "Kitap", "Penguin", 89.99));
        insert(new UrunNode("X05", "Futbol Topu", "Spor", "Adidas", 249.00));
        insert(new UrunNode("X06", "Bluetooth Hoparlör", "Elektronik", "Yamaha", 799.90));
        insert(new UrunNode("X07", "Yapboz Seti", "Oyuncak", "Lego", 349.99));
        insert(new UrunNode("X08", "Ruj", "Kozmetik", "Loreal", 149.90));
        insert(new UrunNode("X09", "Elektrikli Süpürge", "Ev Aletleri", "Bosch", 3899.00));
        insert(new UrunNode("X10", "Kulak Üstü Kulaklık", "Elektronik", "Sony", 1199.00));
    }
}
