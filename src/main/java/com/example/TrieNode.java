package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children;
    List<UrunNode> urunler;
    boolean isEndOfWord;


    public TrieNode() {
        children = new HashMap<>();
        urunler = new ArrayList<UrunNode>();
        isEndOfWord = false;

        
    }
}
