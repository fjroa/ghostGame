package com.fjroa.ghost.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TrieDictionary implements Dictionary {

    private TrieNode root;

    public TrieDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        root = new TrieNode();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                root.add(line.trim());
        }
    }
    
    @Override
    public boolean isWord(String word) {
        return root.isWord(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        return root.getAnyWordStartingWith(prefix);
    }

    @Override
    public String getNextPrefix(String prefix) {
        return root.getRandomChild(prefix);
    }
}
