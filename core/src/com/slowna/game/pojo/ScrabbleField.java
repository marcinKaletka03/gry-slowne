package com.slowna.game.pojo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.scrabble.exeption.IllegalMoveException;
import lombok.Getter;

import java.util.Objects;

import static com.slowna.game.config.ScrabbleTableConfiguration.DEFAULT_FIELD_SIZE;

@Getter
public class ScrabbleField extends Table implements Comparable<ScrabbleField> {

    private final int xPos;
    private final int yPos;
    private boolean bonusCaught = false;
    private final ScrabbleFieldBonus scrabbleFieldBonus;
    private ScrabbleChar propOn;
    private ScrabbleChar scrabbleCharOn;
    private final Position position;

    public ScrabbleField(int xPos, int yPos, ScrabbleFieldBonus scrabbleFieldBonus) {
        this(xPos, yPos, scrabbleFieldBonus, null);
    }

    public ScrabbleField(int xPos, int yPos, ScrabbleFieldBonus scrabbleFieldBonus, ScrabbleChar charOn) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.position = new Position(xPos, yPos);
        this.scrabbleFieldBonus = scrabbleFieldBonus;
        this.scrabbleCharOn = charOn;
        setTransform(true);
        setSize(DEFAULT_FIELD_SIZE, DEFAULT_FIELD_SIZE);
        setOrigin(Align.center);
        setBackground(new TextureRegionDrawable(scrabbleFieldBonus.getTexture()));
        this.scrabbleCharOn = charOn;
        center();
        if (charOn != null) {
            add(charOn).size(charOn.getWidth());
        }
        setTouchable(Touchable.enabled);
    }


    public void enableFocus() {
        addAction(Actions.scaleTo(1.1f, 1.1f, 0.2f));
    }

    public void disableFocus() {
        addAction(Actions.scaleTo(1, 1, 0.2f));
    }

    public boolean canPutOn() {
        return scrabbleCharOn == null;
    }

    public void removePropOn() {
        this.propOn = null;
        reset();
        invalidate();
    }

    public void alertWrong() {
        if (propOn != null && !propOn.hasActions()) {
            Color finishColor = propOn.getColor();
            propOn.addAction(Actions.sequence(
                    Actions.color(Color.RED, 0.1f),
                    Actions.color(finishColor, 0.1f),
                    Actions.color(Color.RED, 0.1f),
                    Actions.color(finishColor, 0.1f)
            ));
        }
    }

    public ScrabbleChar getPropOn() {
        return propOn;
    }

    public ScrabbleChar putPropOn(ScrabbleChar scrabbleChar) {
        if (scrabbleCharOn != null) {
            throw new IllegalMoveException("It's impossible to put another char on this field");
        }
        ScrabbleChar old = propOn;
        if (propOn != null) {
            clearChildren();
            invalidate();
        }
        scrabbleChar.setProp();
        scrabbleChar.setTouchable(Touchable.enabled);
        center().add(scrabbleChar).size(scrabbleChar.getWidth(), scrabbleChar.getHeight());
        this.propOn = scrabbleChar;
        return old;
    }

    public boolean isInLine(ScrabbleField scrabbleField, Direction direction) {
        switch (direction) {
            case HORIZONTALLY:
                return this.yPos == scrabbleField.yPos && this.xPos != scrabbleField.xPos;
            case VERTICALLY:
                return this.xPos == scrabbleField.xPos && this.yPos != scrabbleField.yPos;
            default:
                throw new RuntimeException(String.format("%s - direction not recognized", direction));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public int getIndexByDirection(Direction direction) {
        switch (direction) {
            case HORIZONTALLY:
                return xPos;
            case VERTICALLY:
                return yPos;
            default:
                throw new RuntimeException(String.format("%s - direction not recognized", direction));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScrabbleField field = (ScrabbleField) o;
        return xPos == field.xPos && yPos == field.yPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    @Override
    public String toString() {
        if (scrabbleCharOn != null) {
            return scrabbleCharOn.toString();
        } else if (propOn != null) {
            return propOn.toString();
        }
        return "x:" + xPos + ", y:" + yPos;
    }

    @Override
    public int compareTo(ScrabbleField o) {
        int result = Integer.compare(this.getXPos(), o.getXPos());
        if (result == 0) {
            return Integer.compare(this.getYPos(), o.getYPos());
        }
        return result;
    }

    public void applyProposition(float delay, boolean makeStars) {
        if (scrabbleCharOn == null) {
            this.scrabbleCharOn = propOn;
            this.propOn = null;
            this.setBonusCaught(true);
        }
        this.scrabbleCharOn.applyChar(delay, makeStars);
    }

    public void setBonusCaught(boolean bonusCaught) {
        this.bonusCaught = bonusCaught;
    }
}
