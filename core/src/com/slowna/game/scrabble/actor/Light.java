package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.slowna.game.asset.SingleTextures;


public class Light extends Image {

    private final float time;
    private static final Array<Color> colors = new Array<>(new Color[]{
            Color.WHITE, Color.CYAN, Color.GREEN, Color.CHARTREUSE, Color.GREEN,
            Color.SCARLET, Color.RED, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD
    });

    public Light(float x, float y, float size, float time, int points) {
        super(SingleTextures.LIGHT.getTexture());
        setPosition(x, y);
        setSize(size, size);
        setColor(getOrDefault(points));
        setVisible(false);
        setOrigin(Align.center);
        setAlign(Align.center);
        this.time = time;
    }

    public void startAction(Stage stage) {
        addAction(Actions.sequence(
                Actions.scaleTo(4f, 4f),
                Actions.show(),
                Actions.scaleTo(8, 8, time / 2f, Interpolation.pow2),
                Actions.scaleTo(0, 0, time / 2f, Interpolation.pow2),
                Actions.removeActor()
        ));
        addAction(Actions.rotateBy(90, time, Interpolation.pow2In));
        stage.addActor(this);
        this.toBack();
    }

    public Color getOrDefault(int points) {
        if (points == 0) {
            return Color.GOLD;
        }
        return colors.get(points - 1);
    }

}
