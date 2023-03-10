package com.slowna.game.pojo;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
public class WordPropositionWrapper implements Comparable<WordPropositionWrapper> {

    public static final WordPropositionWrapper EMPTY = new WordPropositionWrapper(ScrabbleWordProposition.EMPTY_PROPOSITION);

    private final ScrabbleWordProposition main;
    private final List<ScrabbleWordProposition> side = new LinkedList<>();

    public WordPropositionWrapper(ScrabbleWordProposition main) {
        this.main = main;
        this.points = main.getPoints();
    }

    private int points;

    public void addSideProposition(ScrabbleWordProposition proposition) {
        this.side.add(proposition);
        this.points += proposition.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordPropositionWrapper that = (WordPropositionWrapper) o;
        return Objects.equals(main, that.main);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main);
    }

    @Override
    public int compareTo(WordPropositionWrapper o) {
        return this.points - o.points;
    }
}
