package com.slowna.game.asset;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.slowna.game.App;

import static com.badlogic.gdx.graphics.Color.LIGHT_GRAY;
import static com.badlogic.gdx.graphics.Color.WHITE;

public enum Fonts {

    STANDARD_WHITE("f3.ttf", 1,
            new Color(WHITE),
            new Color(Color.DARK_GRAY)
    ),
    STANDARD("f3.ttf", 1,
            new Color(Color.BLACK), LIGHT_GRAY
    ),
    SCRABBLE_FONT("f3.ttf", 1,
//            new Color(Color.BLACK), LIGHT_GRAY
            Color.valueOf("260701"),
            Color.valueOf("603601")
    ),
    BROWN("f3.ttf", 1,
            Color.valueOf("1C0A00"), Color.valueOf("260701")
    ),
//    SPACE("spaceFont.otf", 4, new Color(WHITE), BLACK)
//    BLACK("f2.otf", 4, Color.BLACK, WHITE)
    ;

    final static int FONT_SIZE = 50;
    final int shadowWidth;
    final String path;
    final Color color;
    final Color shadowColor;
    final Color borderColor;


    Fonts(String path, int shadowWidth, Color color, Color shadowColor) {
        this.shadowWidth = shadowWidth;
        this.path = path;
        this.color = color;
        this.borderColor = null;
        this.shadowColor = shadowColor;

    }

    Fonts(String path, int shadowWidth, Color color) {
        this.shadowWidth = shadowWidth;
        this.path = path;
        this.color = color;
        this.borderColor = null;
        this.shadowColor = null;
    }

    public BitmapFont get() {
        BitmapFont font = App.manager().getFont(this);
        font.setUseIntegerPositions(false);
        return font;
    }


}
