package com.example;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map children;
    boolean isEndOfWord;


    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}
