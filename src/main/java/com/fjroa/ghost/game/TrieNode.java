package com.fjroa.ghost.game;

import java.util.HashMap;
import java.util.Random;

/**
 * The Class TrieNode.
 */
public class TrieNode {

	/** The children. */
	private HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();

	/** The is word. */
	private boolean isWord = false;

	/**
	 * Instantiates a new trie node.
	 */
	public TrieNode() {
	}

	/**
	 * Adds the word into the TrieNode
	 *
	 * @param word
	 *            the word
	 */
	public void add(String word) {
		HashMap<Character, TrieNode> child = children;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);

			TrieNode t;
			if (child.containsKey(c)) {
				t = child.get(c);
			} else {
				t = new TrieNode();
				child.put(c, t);
			}

			child = t.children;

			// set leaf node
			if (i == word.length() - 1)
				t.isWord = true;
		}
	}

	/**
	 * Checks if is word.
	 *
	 * @param prefix
	 *            the word
	 * @return true, if is the word is in the trie
	 */
	public boolean isWord(String prefix) {
		TrieNode t = searchNode(prefix);
		if (t != null && t.isWord)
			return true;
		else
			return false;
	}

	/**
	 * Search node.
	 *
	 * @param prefix
	 *            the prefix
	 * @return the trie node acording to the string
	 */
	protected TrieNode searchNode(String prefix) {
		HashMap<Character, TrieNode> child = children;
		TrieNode t = null;
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			if (child.containsKey(c)) {
				t = child.get(c);
				child = t.children;
			} else {
				return null;
			}
		}

		return t;
	}

	/**
	 * Gets a word starting with the prefix.
	 *
	 * @param prefix
	 *            the prefix
	 * @return the any word starting with
	 */
	public String getWordStartingWith(String prefix) {
		TrieNode t = searchNode(prefix);
		String output = new String(prefix);
		HashMap<Character, TrieNode> child;
		if (t == null) {
			return null;
		} else {
			while (!t.isWord) {
				child = t.children;
				Character nextKey = (Character) child.keySet().toArray()[0];
				output = output + nextKey;
				t = child.get(nextKey);
			}
		}
		return output;
	}

	/**
	 * Gets the random child.
	 *
	 * @param prefix
	 *            the prefix
	 * @return the random child
	 */
	public String getRandomChild(String prefix) {
		TrieNode t = searchNode(prefix);
		String newPrefix = null;
		if (t != null && t.children != null && t.children.size() > 0) {
			int i = 0;
			int random = new Random().nextInt(t.children.size());
			int maxRandomIterations = t.children.size() * 5;
			int counter = 0;
			while (counter++ < maxRandomIterations) {
				for (Character c : t.children.keySet()) {
					if (random == i++) {
						newPrefix = prefix + c;
						if (isWord(newPrefix)) {
							random = new Random().nextInt(t.children.size());
							i = 0;
							break;
						} else {
							return newPrefix;
						}
					}
				}
			}
		}
		return newPrefix;
	}
}
