package com.slowna.game.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
@RequiredArgsConstructor
public class ScrabbleWordProposition implements Comparable<ScrabbleWordProposition> {

    public static final ScrabbleWordProposition FAIL_PROP = new ScrabbleWordProposition(new ScrabbleCharProposition[0], "", Direction.BOTH, 1);
    public static final ScrabbleWordProposition EMPTY_PROPOSITION = new ScrabbleWordProposition(new ScrabbleCharProposition[0], "", Direction.BOTH, 0);
    private final ScrabbleCharProposition[] scrabbleCharPropositions;
    private final String word;
    private final Direction direction;
    private final int points;

    public ScrabbleWordProposition(ScrabbleCharProposition[] scrabbleCharPropositions, int points) {
        this.scrabbleCharPropositions = scrabbleCharPropositions;
        this.points = points;
        this.direction = Direction.recognizeDirection(scrabbleCharPropositions);
        char[] chars = new char[scrabbleCharPropositions.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = scrabbleCharPropositions[i].getC();
        }
        this.word = new String(chars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrabbleWordProposition that = (ScrabbleWordProposition) o;
        return Arrays.equals(scrabbleCharPropositions, that.scrabbleCharPropositions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(scrabbleCharPropositions);
    }


    @Override
    public int compareTo(ScrabbleWordProposition o) {
        return this.points - o.points;
    }
}
