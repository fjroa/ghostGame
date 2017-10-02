package com.fjroa.ghost.dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class TrieDictionary.
 */
public class TrieDictionary implements IDictionary {

	/** The root. */
	private TrieNode root;

	/**
	 * Instantiates a new trie dictionary.
	 *
	 * @param wordListStream
	 *            the word list stream
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public TrieDictionary(InputStream wordListStream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
		root = new TrieNode();
		String line = null;
		while ((line = in.readLine()) != null) {
			String word = cleanWord(line.trim());
			if (word.length() >= MIN_WORD_LENGTH) {
				root.add(word);
			}
		}
		// After populating the tree we can prune the tree, 
		// with this, all leaf nodes must be valid words.
		root = prune(root);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fjroa.ghost.game.Dictionary#isWord(java.lang.String)
	 */
	@Override
	public boolean isWord(String prefix) {
		TrieNode t = root.searchNode(prefix);
		if (t != null && t.isWord)
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fjroa.ghost.game.Dictionary#getFirstWordStartingWith(java.lang.String)
	 */
	@Override
	public String getFirstWordStartingWith(String prefix) {
		TrieNode t = root.searchNode(prefix);
		if (t == null) {
			return null;
		} else {
			String word = new String(prefix);
			while (!t.isWord) {
				HashMap<Character, TrieNode> child = t.children;
				Character nextKey = (Character) child.keySet().toArray()[0];
				word = word + nextKey;
				t = child.get(nextKey);
			}
			return word;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fjroa.ghost.game.Dictionary#getNextPrefix(java.lang.String)
	 */
	@Override
	public Set<Character> getNextLetters(String prefix) {
		TrieNode t = root.searchNode(prefix);
		if (t != null && t.children != null && t.children.size() > 0) {
			return t.children.keySet();
		}
		return null;
	}


	private String cleanWord(String s) {
		return s.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o")
				.replaceAll("ú", "o").replaceAll("'", "").toLowerCase();
	}
	
	private TrieNode prune(TrieNode t) {
		if (t.isWord) {
			t.children = null;
		} else {
			Iterator<Entry<Character, TrieNode>> it = t.children.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Character, TrieNode> pair = (Map.Entry<Character, TrieNode>) it.next();
				t.children.put(pair.getKey(), prune(pair.getValue()));
			}
		}
		return t;
	}
	
	/**
	 * The Private Class TrieNode.
	 */
	private class TrieNode {

		/** The children. */
		private HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();

		/** The is word. */
		private boolean isWord = false;

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
	}
}
