package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.slowna.game.asset.RegionTextures;


public class AnimatedImage extends Image {

    protected final Animation<TextureRegionDrawable> animation;
    protected float current = 0;

    public AnimatedImage(RegionTextures textures, float animationDuration) {
        Array<TextureAtlas.AtlasRegion> regionArray = textures.atlas().getRegions();
        Array<TextureRegionDrawable> result = new Array<>();
        for (TextureAtlas.AtlasRegion region : regionArray) {
            result.add(new TextureRegionDrawable(new TextureRegion(region)));
        }
        this.animation = new Animation<>(animationDuration, result, Animation.PlayMode.LOOP);
        setDrawable(animation.getKeyFrame(0));

    }

    @Override
    public void act(float delta) {
        current += delta;
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();

        animation.getKeyFrame(current).draw(batch, x + getImageX(), y + getImageY(), getImageWidth() * scaleX, getImageHeight() * scaleY);
    }
}
