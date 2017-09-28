package com.fjroa.ghost.game;

public interface IDictionary {
    
    /** The Constant MIN_WORD_LENGTH. */
    public final static int MIN_WORD_LENGTH = 4;
    
    /**
     * Checks if is word.
     *
     * @param word the word
     * @return true, if is word
     */
    boolean isWord(String word);
    
    /**
     * Gets a word starting with.
     *
     * @param prefix the prefix
     * @return the any word starting with
     */
    String getWordStartingWith(String prefix);
    
    /**
     * Gets the next prefix.
     *
     * @param prefix the prefix
     * @return the next prefix
     */
    String getNextPrefix(String prefix);
}
