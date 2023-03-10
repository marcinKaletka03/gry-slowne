package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import com.slowna.game.pojo.Position;

import static com.badlogic.gdx.graphics.Color.*;

public class TetrisConfig {

    public static final int fieldsInRow = 10;
    public static final int maxRowIndex = fieldsInRow - 1;


    public static final int fieldsInColumn = 20;
    public static final int maxColumnIndex = fieldsInColumn - 1;

    public static final Color[] TABLE_COLORS = new Color[]{BLUE, SCARLET, GREEN, ORANGE, PINK, PURPLE, BROWN};


    public static final Position STANDARD_START_POSITION = new Position(fieldsInRow / 2, fieldsInColumn - 1);
}
