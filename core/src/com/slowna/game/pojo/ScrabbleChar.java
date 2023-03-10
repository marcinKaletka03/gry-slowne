package com.slowna.game.pojo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.asset.Fonts;
import com.slowna.game.asset.RegionTextures;
import com.slowna.game.asset.Sounds;
import com.slowna.game.config.ScrabbleTableConfiguration;
import com.slowna.game.scrabble.actor.Star;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ScrabbleChar extends Table {

    private char letter;
    private final int points;
    private final Label charLabel;
    private final float defaultSize;
    private final float fontScale = 62;
    private static final Color propColor = new Color(Color.YELLOW) {{
        a = 0.85f;
    }};

    public ScrabbleChar(char letter, int points) {
        this(ScrabbleTableConfiguration.DEFAULT_FIELD_SIZE, letter, points);
    }

    public ScrabbleChar(ScrabbleChar scrabbleChar) {
        this(scrabbleChar.letter, scrabbleChar.getPoints());
    }

    public ScrabbleChar(float size, char letter, int points) {
        this.defaultSize = size * 0.85f;
        setSize(defaultSize, defaultSize);
        setPosition(MathUtils.random(size * 0.1f), MathUtils.random(size * 0.1f));
        setTransform(true);
        setBackground(new TextureRegionDrawable(RegionTextures.CHARS.getTexture(points)));
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.SCRABBLE_FONT.get(), Color.WHITE);
        this.charLabel = new Label(String.valueOf(letter).toUpperCase(), labelStyle);
        charLabel.setTouchable(Touchable.disabled);
        charLabel.setFontScale(defaultSize / fontScale);
        charLabel.setAlignment(Align.center, Align.center);
        top().add(charLabel).size(defaultSize);
        setOrigin(Align.center);
        align(Align.center);
        this.letter = letter;
        this.points = points;
        setTouchable(Touchable.enabled);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (charLabel != null) {
            charLabel.setFontScale(getWidth() / fontScale);
        }
    }

    public void resetProp() {
        setColor(Color.WHITE);
        addAction(Actions.parallel(
                Actions.scaleTo(1, 1),
                Actions.sizeTo(defaultSize, defaultSize)
        ));
    }

    public void setProp() {
        setColor(propColor);
        addAction(Actions.parallel(
                Actions.scaleTo(1, 1),
                Actions.sizeTo(defaultSize, defaultSize)));
    }

    public void applyChar(float delay, boolean makeStars) {
        setTouchable(Touchable.disabled);
        setSize(defaultSize, defaultSize);
        float duration = 0.5f;
        addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.run(() -> {
                    Sounds.POINT.play();
                    if (makeStars) {
                        Star.makeStars(5 + points * 3, points, this.getParent());
                    }
                }),
                Actions.scaleTo(1.2f, 1.2f, duration),
                Actions.parallel(
                        Actions.scaleTo(1, 1, duration),
                        Actions.rotateTo(MathUtils.random() * 5 * MathUtils.randomSign(), duration)
                )
        ));
        addAction(Actions.delay(delay,
                Actions.color(Color.WHITE, duration)));
        charLabel.addAction(
                Actions.sequence(
                        Actions.delay(delay),
                        Actions.color(Color.GOLD, duration),
                        Actions.color(Color.BLACK, duration)
                ));
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }

    @Override
    public String toString() {
        return "" + letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
        this.charLabel.setText(String.valueOf(letter).toUpperCase());
    }
}
