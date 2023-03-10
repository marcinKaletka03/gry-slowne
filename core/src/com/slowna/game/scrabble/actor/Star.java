package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.asset.SingleTextures;

public class Star extends Image {

    private final float actorWidth;
    private final float actorHeight;

    public static void makeStars(int number, int points, Actor from) {
        for (int i = 0; i < number; i++) {
            new Star(from, points);
        }
    }

    protected Star(Actor from, int points) {
        super(SingleTextures.STAR.getTexture());
        setColor(new Color(Color.YELLOW).lerp(Color.GOLD, points / 10f));
        setVisible(false);
        this.actorWidth = from.getWidth() * from.getScaleX();
        this.actorHeight = from.getHeight() * from.getScaleY();
        Vector2 actorPosition = from.localToStageCoordinates(new Vector2());
        float size = actorWidth / 2f;
        setSize(size, size);
        setOrigin(Align.center);
        setPosition(actorPosition.x + from.getOriginX() - getWidth() / 2f, actorPosition.y + from.getOriginY() - getHeight() / 2f);

        from.getStage().addActor(this);
        show();
    }

    public void show() {
        float factor = MathUtils.random(1, 1.5f);
        float upMove = actorHeight * factor * MathUtils.random();
        float sideMove = factor * actorWidth * MathUtils.random() * MathUtils.randomSign();
        float time = factor / 2f;
        float rotation = MathUtils.random(360, 720) * MathUtils.randomSign();
        addAction(Actions.sequence(
                Actions.scaleTo(0.5f, 0.5f),
                Actions.show(),
                Actions.parallel(
                        Actions.scaleTo(factor, factor, time),
                        Actions.moveBy(sideMove, 0, time, Interpolation.linear),
                        Actions.rotateBy(rotation, time),
                        Actions.moveBy(0, upMove, time, Interpolation.circleOut)
                ),
                Actions.parallel(
                        Actions.moveBy(sideMove, 0, time),
                        Actions.moveBy(0, -upMove * 8, time, Interpolation.slowFast),
                        Actions.rotateBy(rotation, time),
                        Actions.scaleTo(0.2f, 0.2f, time)
                ),
                Actions.removeActor()
        ));

    }


}
