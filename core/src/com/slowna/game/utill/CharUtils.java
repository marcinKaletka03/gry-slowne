package com.slowna.game.utill;


import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.pojo.ScrabbleField;
import com.slowna.game.pojo.ScrabbleWord;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.slowna.game.config.ScrableConfig.BLANK_LETTER;


@UtilityClass
public class CharUtils {

    /**
     * @param word   word use to fill fields
     * @param fields fields use to fill word
     * @return first found index which should be used to fill word in fields table or -1 if cant find any
     */
    public int findStartIndex(String word, ScrabbleField[] fields) {
        char[] wordChars = word.toCharArray();
        for (int i = 0; i < wordChars.length; i++) {
            if (word.length() + i > fields.length) {
                return -1;
            }
            List<Integer> possibleIndexes = getIndexesOfChar(wordChars[i], i, fields);
            for (Integer index : possibleIndexes) {
                int startIndex = index - i;
                if (canBeBuild(word, fields, startIndex)) {
                    return startIndex;
                }
            }
        }
        return -1;
    }

    public List<Integer> getIndexesOfChar(char c, int minIndex, ScrabbleField[] scrabbleFields) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = minIndex; i < scrabbleFields.length; i++) {
            ScrabbleChar charOn = scrabbleFields[i].getScrabbleCharOn();
            if (charOn == null) {
                continue;
            }
            if (charOn.getLetter() == c) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private boolean canBeBuild(String word, ScrabbleField[] fields, int startIndex) {
        int finishIndex = startIndex + word.length() - 1;
        if (finishIndex >= fields.length || startIndex < 0) {
            return false;
        }
        char[] wordChars = word.toCharArray();
        int wordIndex = 0;
        for (int i = startIndex; i <= finishIndex; i++) {
            ScrabbleChar current = fields[i].getScrabbleCharOn();
            if (current != null && current.getLetter() != wordChars[wordIndex]) {
                return false;
            }
            wordIndex++;
        }
        return true;
    }

    public static char[] convertToCharArray(List<ScrabbleChar> collection) {
        char[] result = new char[collection.size()];
        for (int i = 0; i < collection.size(); i++) {
            result[i] = collection.get(i).getLetter();
        }
        return result;
    }

    public static int countLettersNumberInArray(char[] letters, char wantedLetter) {
        int result = 0;
        for (char c : letters) {
            if (c == wantedLetter) {
                result++;
            }
        }
        return result;
    }

    public static boolean canBeBuildScrableWordFrom(ScrabbleWord scrabbleWord, char[] sortedLowerCaseChars, int maxCharsLack) {
        int currentLack = 0;
        char[] scrableWordChars = scrabbleWord.getSortedChars();

        int givenCharsIndex = 0;

        main:
        for (int i = 0; i < scrabbleWord.getLength(); i++) {
            char wantedChar = scrableWordChars[i];
            char checkedChar = sortedLowerCaseChars[givenCharsIndex];

            while (checkedChar < wantedChar) {
                givenCharsIndex++;
                if (givenCharsIndex == sortedLowerCaseChars.length) {
                    currentLack += scrabbleWord.getLength() - i;
                    break main;
                }
                checkedChar = sortedLowerCaseChars[givenCharsIndex];
            }
            if (checkedChar == wantedChar) {
                givenCharsIndex++;
                if (givenCharsIndex == sortedLowerCaseChars.length) {
                    currentLack += scrabbleWord.getLength() - (i + 1);
                    break;
                }
                continue;
            }

            if (++currentLack > maxCharsLack) {
                return false;
            }

        }
        return currentLack <= maxCharsLack;
    }


    public static char[] sortCharsAndRemoveBlanks(char[] chars, int blanks) {
        char[] result = new char[chars.length - blanks];
        int counter = 0;
        for (int i = 0; i < result.length; i++) {
            char current = chars[i];
            if (current != BLANK_LETTER) {
                result[counter++] = current;
            }
        }
        Arrays.sort(result);
        return result;
    }


    public static boolean containBlankLetters(ScrabbleField[] fields) {
        for (ScrabbleField field : fields) {
            if (field.getPropOn() != null) {
                if (field.getPropOn().getLetter() == BLANK_LETTER) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> T findFirstBy(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public static long countBlankLetters(Collection<ScrabbleChar> collection) {
        return collection.stream()
                .filter(s -> s.getLetter() == BLANK_LETTER)
                .count();
    }

}
