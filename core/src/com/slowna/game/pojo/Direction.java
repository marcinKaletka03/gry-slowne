package com.slowna.game.pojo;

import com.slowna.game.scrabble.exeption.DirectionException;

public enum Direction {
    VERTICALLY,
    HORIZONTALLY,
    BOTH;

    public static Direction recognizeDirection(ScrabbleCharProposition[] fields) {
        if (fields.length < 1) {
            throw new DirectionException("Unknown direction");
        } else if (fields.length == 1) {
            return BOTH;
        }
        boolean xFlow = true;
        boolean yFlow = true;
        ScrabbleCharProposition previous = fields[0];
        for (int i = 1; i < fields.length; i++) {
            ScrabbleCharProposition current = fields[i];
            if (yFlow && !previous.isInLine(current, Direction.VERTICALLY)) {
                yFlow = false;
            }
            if (xFlow && !previous.isInLine(current, Direction.HORIZONTALLY)) {
                xFlow = false;
            }
            previous = current;
        }
        if (xFlow) return Direction.HORIZONTALLY;
        if (yFlow) return Direction.VERTICALLY;
        throw new DirectionException("Unknown direction");
    }

    public static Direction recognizeDirection(ScrabbleField[] fields) {
        if (fields.length < 1) {
            throw new DirectionException("Unknown direction");
        } else if (fields.length == 1) {
            return BOTH;
        }
        boolean xFlow = true;
        boolean yFlow = true;
        ScrabbleField previous = fields[0];
        for (int i = 1; i < fields.length; i++) {
            ScrabbleField current = fields[i];
            if (yFlow && !previous.isInLine(current, Direction.VERTICALLY)) {
                yFlow = false;
            }
            if (xFlow && !previous.isInLine(current, Direction.HORIZONTALLY)) {
                xFlow = false;
            }
            previous = current;
        }
        if (xFlow) return Direction.HORIZONTALLY;
        if (yFlow) return Direction.VERTICALLY;
        throw new DirectionException("Unknown direction");
    }

    public Direction getOpposite() {
        if (this == BOTH) {
            return BOTH;
        }
        if (this == VERTICALLY) {
            return HORIZONTALLY;
        }
        return VERTICALLY;
    }

    ;
}
