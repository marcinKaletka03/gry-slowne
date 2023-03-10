package com.slowna.game.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public class ScrabbleWord {

    private final String word;
    private final char[] sortedChars;

    public ScrabbleWord(String word) {
        this.word = word.toLowerCase();
        sortedChars = word.toCharArray();
        Arrays.sort(sortedChars);
    }

    public boolean containsWord(String word) {
        return this.word.equals(word);
    }

    public int getLength() {
        return sortedChars.length;
    }

    @Override
    public String toString() {
        return word;
    }
}
