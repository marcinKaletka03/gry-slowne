package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TetrisField extends Table {

    private String charOn;
    //    private final Label label;
    private boolean append = false;

    public TetrisField(float size, int x, int y) {
//        setBackground(Textures.TETRIS_FIELD.getDrawable());
        setSize(size, size);
//        label = new Label("", new Label.LabelStyle(Fonts.SPACE.get(), Color.WHITE));
//        label.setSize(size,size);
//        label.setAlignment(Align.center);
//        label.setFontScale(0.3f);
//        center().add(label);
        setColor(Color.CLEAR);
    }

    public void resetField() {
        charOn = null;
        setColor(Color.CLEAR);
    }

    public void applyChar(TetrisChar tetrisChar) {
        this.charOn = String.valueOf(tetrisChar.getLetter()).toUpperCase();
//        label.setText(charOn);
        setColor(tetrisChar.getColor());
        setVisible(true);
    }

    public void append() {
        this.append = true;
    }
}
