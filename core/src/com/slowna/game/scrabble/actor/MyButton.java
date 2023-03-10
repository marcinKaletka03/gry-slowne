package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.asset.Fonts;
import com.slowna.game.asset.Sounds;
import com.slowna.game.asset.Textures;
import com.slowna.game.extra.MyBooleanSupplier;

public class MyButton extends Button {

    private MyBooleanSupplier supplier;

    public MyButton(String text, float width, float height, Runnable onClick) {
        this(text, width, height, Textures.BUTTON, onClick);
    }

    public MyButton(String text, float width, float height, Textures buttonTexture, Runnable onClick) {
        super(new TextureRegionDrawable(buttonTexture.get()));
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.BROWN.get(), Color.WHITE);
        Label textLabel = new Label(text, labelStyle);
        textLabel.setSize(width, height);
        setSize(width, height);
        textLabel.setFontScale(height / 120f);
        textLabel.setAlignment(Align.center, Align.center);
        textLabel.setTouchable(Touchable.disabled);
        center().add(textLabel);
        setTouchable(Touchable.enabled);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.run();
                Sounds.CLICK.play();
                update();
            }
        });
    }

    public void setEnabledFunction(MyBooleanSupplier supplier) {
        this.supplier = supplier;
    }

    public void update() {
        if (supplier != null) {
            boolean enabled = supplier.apply();
            Color color = enabled ? Color.WHITE : Color.GRAY;
            this.setDisabled(!supplier.apply());
            setColor(color);
        }
    }
}
