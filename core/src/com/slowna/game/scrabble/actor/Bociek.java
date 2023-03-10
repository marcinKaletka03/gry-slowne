package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slowna.game.asset.RegionTextures;
import com.slowna.game.config.ScrableConfig;

public class Bociek extends AnimatedImage {

    private int index = ScrableConfig.BOCIEK_MIN_INDEX;
    private final boolean left;

    public Bociek(float scale, Stage stage, boolean left) {
        super(RegionTextures.BOCIEK, 1 / 30f);
        this.left = left;
        if (scale > 1) {
            index = ScrableConfig.BOCIEK_MAX_INDEX;
        }
        setScale(scale, scale);
        Drawable drawable = getDrawable();
        setTouchable(Touchable.disabled);
        setSize(drawable.getMinWidth(), drawable.getMinHeight());
        float y = stage.getHeight() / 2f * MathUtils.random() + stage.getHeight() / 8f;
        float x = left ? -getWidth() * scale : stage.getWidth() + getWidth() * scale;
        setPosition(x, y);
        if (left) {
            Object[] keyFrames = animation.getKeyFrames();
            for (Object keyFrame : keyFrames) {
                ((TextureRegionDrawable) keyFrame).getRegion().flip(true, false);
            }
        }
    }


    public void move() {
        float finalX = !left ? -getWidth() * getScaleX() : getStage().getWidth() + getWidth() * getScaleX();
        setZIndex(index);
        addAction(
                Actions.sequence(
                        Actions.moveTo(finalX, getY(), MathUtils.random() * 5 + 5),
                        Actions.removeActor()
                )
        );
    }

}
