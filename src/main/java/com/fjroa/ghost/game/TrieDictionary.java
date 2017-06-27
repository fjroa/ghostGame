package com.fjroa.ghost.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The Class TrieDictionary.
 */
public class TrieDictionary implements Dictionary {

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
			if (word.length() >= MIN_WORD_LENGTH)
				root.add(word);
		}
	}

	private String cleanWord(String s) {
		return s.replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").replaceAll("ó", "o")
				.replaceAll("ú", "o").replaceAll("'", "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fjroa.ghost.game.Dictionary#isWord(java.lang.String)
	 */
	@Override
	public boolean isWord(String word) {
		return root.isWord(word);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fjroa.ghost.game.Dictionary#getAnyWordStartingWith(java.lang.String)
	 */
	@Override
	public String getWordStartingWith(String prefix) {
		return root.getWordStartingWith(prefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fjroa.ghost.game.Dictionary#getNextPrefix(java.lang.String)
	 */
	@Override
	public String getNextPrefix(String prefix) {
		return root.getRandomChild(prefix);
	}
}
