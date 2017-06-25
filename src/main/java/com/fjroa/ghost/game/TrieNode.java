package com.fjroa.ghost.game;

import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    private boolean isWord = false;

    public TrieNode() {}

    public void add(String word) {
        HashMap<Character,TrieNode> child = children;
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);

            TrieNode t;
            if(child.containsKey(c)){
                t = child.get(c);
            }else{
                t = new TrieNode();
                child.put(c, t);
            }

            child = t.children;

            //set leaf node
            if(i==word.length()-1)
                t.isWord = true;
        }
    }

    // Returns if the word is in the trie.
    public boolean isWord(String word) {
        TrieNode t = searchNode(word);
        if(t != null && t.isWord)
            return true;
        else
            return false;
    }


    protected TrieNode searchNode(String s){
        HashMap<Character, TrieNode> child = children;
        TrieNode t = null;
        for(int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if(child.containsKey(c)){
                t = child.get(c);
                child = t.children;
            }else{
                return null;
            }
        }

        return t;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode t = searchNode(s);
        String output = s +"";
        HashMap<Character,TrieNode> child;
        if(t==null){
            return null;
        }
        else{
            while(!t.isWord){
                child = t.children;
                Character nextKey = (Character)child.keySet().toArray()[0];
                output = output + nextKey;
                t = child.get(nextKey);
            }
        }
        return output;
    }

    public String getRandomChild(String prefix) {
    	TrieNode t = searchNode(prefix);
        if (t != null && t.children != null && t.children.size() > 0) {
        	int i = 0;
        	int random = new Random().nextInt(t.children.size());
        	for (Character c : t.children.keySet()) {
        		if (i == random) {
        			return prefix + c;
        		}
        		i++;
        	}
        }
        return null;
    }
}
