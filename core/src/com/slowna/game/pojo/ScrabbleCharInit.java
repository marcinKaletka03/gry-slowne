package com.slowna.game.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrabbleCharInit implements Serializable {
    private char letter;
    private int quantity;
    private int points;

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrabbleCharInit that = (ScrabbleCharInit) o;
        return letter == that.letter;
    }

    public Collection<ScrabbleChar> toScrabbleChars() {
        return Stream.iterate(0, UnaryOperator.identity())
                .limit(quantity)
                .map(s -> new ScrabbleChar(letter, points))
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }
}

