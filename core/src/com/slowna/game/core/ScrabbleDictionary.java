package com.slowna.game.core;


import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.pojo.ScrabbleField;
import com.slowna.game.pojo.ScrabbleWordProposition;
import com.slowna.game.scrabble.extra.ChunkProcess;

import java.util.List;
import java.util.regex.Pattern;

public interface ScrabbleDictionary {

    ChunkProcess<String, ScrabbleWordProposition> getSearchingProcess(ScrabbleField[] availableFieldsInLine, List<ScrabbleChar> playerChars, int minWordLength, int maxWordLength);

    /**
     * @param chars       lower case chars which can be used to create word
     * @param maxQuantity quantity of looking for results, negative value disable counting
     * @param maxLength
     * @param minLength
     * @return List of words sorted by points
     */
    List<String> findTheBestWords(char[] chars, int maxQuantity, int minLength, int maxLength);


    /**
     * @return lower case scrabble chars Pool enabled in single game
     */
    List<ScrabbleChar> getScrabbleCharsPoolForNewGame();

    boolean containsWord(String word);

    List<String> getAvailableWords(Pattern pattern, int length);

    void saveNewWord(String word);

    int getPointsForChar(char c);

}
