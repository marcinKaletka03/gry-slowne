package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.asset.Fonts;
import com.slowna.game.config.ScrabbleCharConfig;
import com.slowna.game.utill.MathUtil;

public class ScrabbleMenuLetter extends Table {

    public Vector2 startPosition;
    private final float size;

    public ScrabbleMenuLetter(float size, char on, TextureRegion texture) {
        this.size = size;
        setTransform(true);
        setBackground(new TextureRegionDrawable(texture));
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.STANDARD.get(), Color.WHITE);
        Label charLabel = new Label(String.valueOf(on).toUpperCase(), labelStyle);
        charLabel.setFontScale(size / 55);
        charLabel.setAlignment(Align.center, Align.center);
        center().add(charLabel).fill();
        row();
        setSize(size, size);
        setOrigin(Align.center);
        setVisible(false);
    }

    public void show() {
        Vector2 randomPosition = MathUtil.getRandomPointOnCircle(startPosition.x, startPosition.y, Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        addAction(Actions.sequence(
                Actions.moveTo(randomPosition.x, randomPosition.y, 0.001f),
                Actions.visible(true),
                Actions.moveTo(startPosition.x, startPosition.y, 1f, Interpolation.exp10Out),
                Actions.run(this::infinityMove)
        ));
    }

    public void show(Action showAction) {
        this.startPosition = localToParentCoordinates(new Vector2());
        addAction(Actions.sequence(showAction, Actions.run(this::infinityMove)));
    }

    private void infinityMove() {
        Vector2 nextPosition = MathUtil.getRandomPointInCircle(startPosition.x, startPosition.y, size / 10f);
        float actionTime = MathUtils.random(ScrabbleCharConfig.DEFAULT_ACTION_TIME) + ScrabbleCharConfig.DEFAULT_ACTION_TIME / 2f;
        addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateTo(getRandomRotation(), actionTime, Interpolation.smooth),
                                Actions.sequence(
                                        Actions.moveTo(nextPosition.x, nextPosition.y, actionTime, Interpolation.smooth),
                                        Actions.moveTo(startPosition.x, startPosition.y, actionTime, Interpolation.smooth)
                                )
                        ),
                        Actions.run(this::infinityMove)
                ));
    }

    public void hideAndRemove(float removeTime) {
        Vector2 randomPosition = MathUtil.getRandomPointOnCircle(startPosition.x, startPosition.y, Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        clearActions();
        addAction(
                Actions.sequence(
                        Actions.moveTo(randomPosition.x, randomPosition.y, removeTime, Interpolation.exp10Out),
                        Actions.removeActor()
                )
        );
    }

    private float getRandomRotation() {
        return MathUtils.random(ScrabbleCharConfig.MAX_ROTATION * 2) - ScrabbleCharConfig.MAX_ROTATION;
    }

}
