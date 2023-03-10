package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;

@Getter
public class TetrisChar implements Comparable<TetrisChar> {

    private int xOffset;
    private int yOffset;
    private final char letter;
    private final Color color;

    public TetrisChar(int xOffset, int yOffset, char letter, Color color) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.letter = letter;
        this.color = color;
    }

    private TetrisField currentField;

    public void changeField(TetrisField nextField) {
        if (currentField != null) {
            currentField.resetField();
        }
        this.currentField = nextField;
    }

    public void applyCurrentChar() {
        if (this.currentField != null) {
            this.currentField.applyChar(this);
        }
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    public int compareTo(TetrisChar o) {
        int result = -Integer.compare(xOffset, o.xOffset);
        if (result == 0) {
            return Integer.compare(yOffset, o.yOffset);
        }
        return result;
    }
}
