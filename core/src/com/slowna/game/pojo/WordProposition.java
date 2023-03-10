package com.slowna.game.pojo;

import com.slowna.game.scrabble.exeption.WordException;

import java.util.Collections;
import java.util.List;

import static com.slowna.game.config.ScrableConfig.DELAY_BETWEEN_SIGN;

public class WordProposition implements Comparable<WordProposition> {


    private final List<ScrabbleField> fields;
    private final String word;
    private final int points;

    public WordProposition(List<ScrabbleField> fieldList) {
        if (fieldList.isEmpty()) {
            throw new WordException("Empty proposition");
        }
        Collections.sort(fieldList);
        this.fields = fieldList;
        int points = 0;
        StringBuilder word = new StringBuilder();
        for (ScrabbleField field : fieldList) {
            ScrabbleChar scrabbleChar = field.getScrabbleCharOn();
            if (scrabbleChar == null) {
                scrabbleChar = field.getPropOn();
            }
            if (scrabbleChar == null) {
                throw new WordException(field + "has no char on");
            }
            int pointsForChar = scrabbleChar.getPoints();
            if (!field.isBonusCaught()) {
                pointsForChar = field.getScrabbleFieldBonus().multiplyLetter(pointsForChar);
            }
            points += pointsForChar;
            word.append(scrabbleChar.getLetter());
        }
        for (ScrabbleField field : fieldList) {
            if (!field.isBonusCaught()) {
                points = field.getScrabbleFieldBonus().multiplyWord(points);
            }
        }

        this.word = word.toString();
        this.points = points;
    }

    public void applyProps(Runnable onFinish, float delay) {
        for (ScrabbleField field : fields) {
            field.applyProposition(delay += DELAY_BETWEEN_SIGN, true);
        }
        onFinish.run();
    }

    public float countDelay() {
        return word.length() * DELAY_BETWEEN_SIGN;
    }

    public String getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(WordProposition o) {
        return this.points - o.points;
    }
}
