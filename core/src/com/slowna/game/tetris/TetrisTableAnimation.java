package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.slowna.game.asset.RegionTextures;

public class TetrisTableAnimation extends Animation<TextureRegionDrawable> {

    private final Array<Color> enabledColors;
    private final Table actor;
    private float current;
    private final Array<PlayMode> enabledPlayModes = new Array<>(new PlayMode[]{PlayMode.NORMAL, PlayMode.REVERSED});

    public TetrisTableAnimation(float frameDuration, Table actor, RegionTextures textures, Color... colors) {
        super(frameDuration, convertToRegions(textures).toArray(TextureRegionDrawable.class));
        this.actor = actor;
        this.enabledColors = new Array<>(colors);
        actor.setColor(enabledColors.random());
        actor.setBackground(getKeyFrame(0));
        setPlayMode(PlayMode.NORMAL);
        newAnimation();
    }

    public void start() {
        newAnimation();
    }

    public void update(float delta) {
        current += delta;
        actor.setBackground(getKeyFrame(current));
    }

    private void newAnimation() {
        keyFramesAnimation();
        colorAnimation();
    }

    private void keyFramesAnimation() {
        current = 0;
        setPlayMode(enabledPlayModes.random());
        boolean flipX = MathUtils.randomBoolean();
        boolean flipY = MathUtils.randomBoolean();
        for (TextureRegionDrawable keyFrame : getKeyFrames()) {
            keyFrame.getRegion().flip(flipX, flipY);
        }
        actor.addAction(Actions.delay(getAnimationDuration() * MathUtils.random(1, 3), Actions.run(this::keyFramesAnimation)));
    }

    private void colorAnimation() {
        actor.addAction(
                Actions.sequence(
                        Actions.color(enabledColors.random(), MathUtils.random(2, 5)),
                        Actions.run(this::colorAnimation)
                )
        );
    }


    private static Array<TextureRegionDrawable> convertToRegions(RegionTextures textures) {
        Array<TextureRegionDrawable> array = new Array<>();
        for (TextureAtlas.AtlasRegion region : textures.atlas().getRegions()) {
            array.add(new TextureRegionDrawable(region));
        }
        return array;
    }


}
