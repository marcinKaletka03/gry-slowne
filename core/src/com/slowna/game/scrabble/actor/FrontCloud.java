package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slowna.game.asset.RegionTextures;
import com.slowna.game.config.ScrableConfig;

public class FrontCloud extends Image {

    public FrontCloud(float scale) {
        super(new TextureRegion(RegionTextures.FRONT_CLOUDS.getRandom()));
        TextureRegionDrawable region = (TextureRegionDrawable) getDrawable();
        region.getRegion().flip(MathUtils.randomBoolean(), false);
        setScale(scale * MathUtils.randomSign());
        setTouchable(Touchable.disabled);

        addAction(Actions.scaleTo(scale, scale));
    }

    public void move(float time, float x, float y) {
        setZIndex(ScrableConfig.FRONT_CLOUD_INDEX);
        addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(x, 0, time, Interpolation.smooth),
                        Actions.moveBy(0, y, time, Interpolation.smooth)
                ),
                Actions.removeActor()
        ));
    }
}
