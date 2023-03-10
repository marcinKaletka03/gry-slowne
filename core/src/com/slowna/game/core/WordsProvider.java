package com.slowna.game.core;

import com.slowna.game.pojo.ScrabbleChar;

import java.util.List;

public interface WordsProvider {

    /**
     * @param maxSize maxSizeOfWord
     * @return all correct lower case words available in game
     */
    List<String> getEnabledWords(int maxSize);

    /**
     * @return lower case Scrabble chars available in game including quantity
     */
    List<ScrabbleChar> getLettersPool();

    /**
     * @param c searching lower case char
     * @return basic points for char
     * @throws IllegalArgumentException if char is not available in game
     */
    int getBasicPointsForChar(char c);

    void saveNewWord(String word);

}
