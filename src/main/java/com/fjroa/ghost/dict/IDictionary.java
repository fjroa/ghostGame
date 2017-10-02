package com.fjroa.ghost.dict;

import java.util.Set;

public interface IDictionary {
    
    /** The Constant MIN_WORD_LENGTH. */
    public final static int MIN_WORD_LENGTH = 4;
    
    /**
     * Checks if the prefix is a valid word.
     *
     * @param word the word
     * @return true, if is word
     */
    boolean isWord(String prefix);
    
    /**
     * Gets the next prefix.
     *
     * @param prefix the prefix
     * @return the next prefix
     */
    Set<Character> getNextLetters(String prefix);
    
    /**
     * Gets the first word starting with the prefix.
     *
     * @param prefix the prefix
     * @return the any word starting with
     */
    String getFirstWordStartingWith(String prefix);
    

}
