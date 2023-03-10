package com.slowna.game.pojo;

import com.slowna.game.scrabble.exeption.DirectionException;
import lombok.Data;

@Data
public class ScrabbleCharProposition implements Comparable<ScrabbleCharProposition> {
    private final int x;
    private final int y;
    private final char c;

    public boolean isInLine(ScrabbleCharProposition prop, Direction direction) {
        switch (direction) {
            case HORIZONTALLY:
                return this.y == prop.y && this.x != prop.x;
            case VERTICALLY:
                return this.x == prop.x && this.y != prop.y;
            default:
                throw new DirectionException(String.format("%s - direction not recognized", direction));
        }
    }


    @Override
    public int compareTo(ScrabbleCharProposition o) {
        int result = Integer.compare(this.x, o.x);
        if (result == 0) {
            return Integer.compare(o.y, this.y);
        }
        return result;
    }

}
