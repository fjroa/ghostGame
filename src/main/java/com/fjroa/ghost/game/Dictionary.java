package com.fjroa.ghost.game;

/**
 * Created by apicard on 9/16/15.
 */
public interface Dictionary {
    public final static int MIN_WORD_LENGTH = 4;
    boolean isWord(String word);
    String getAnyWordStartingWith(String prefix);
    String getNextPrefix(String prefix);
}
