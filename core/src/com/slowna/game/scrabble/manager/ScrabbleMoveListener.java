package com.slowna.game.scrabble.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.slowna.game.asset.Sounds;
import com.slowna.game.config.ScrableConfig;
import com.slowna.game.gameData.GameData;
import com.slowna.game.gameData.KeyboardState;
import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.pojo.ScrabbleField;
import com.slowna.game.scrabble.actor.PlayerControl;
import com.slowna.game.scrabble.actor.SmoothCamera;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ScrabbleMoveListener extends ActorGestureListener {
    private final Stage gameStage;
    private final SmoothCamera gameStageCamera;
    private ScrabbleChar holdenChar;
    private ScrabbleField focusedField;
    private final PlayerControl scrabblesTable;
    private final Set<ScrabbleField> affectedFields = new HashSet<>();
    private final GameData gameData;

    public ScrabbleMoveListener(Stage gameStage, PlayerControl scrabblesTable, GameData gameData) {
        this.gameData = gameData;
        this.gameStage = gameStage;
        this.scrabblesTable = scrabblesTable;
        this.gameStageCamera = (SmoothCamera) gameStage.getCamera();
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (pointer != 0 || gameData.getTurnManager().getKeyboardState() == KeyboardState.DISABLED) {
            return;
        }
        Actor actor = event.getStage().hit(event.getStageX(), event.getStageY(), true);
        if (actor != null && actor.getClass() == ScrabbleChar.class) {
            this.holdenChar = (ScrabbleChar) actor;
            this.scrabblesTable.removePlayerChar(this.holdenChar);
            setHoldenChar((ScrabbleChar) actor, event);
            Sounds.TAKE.play();
        } else {
            float reversedY = ScrableConfig.SCREEN_HEIGHT - y;
            Vector2 possible = gameStage.screenToStageCoordinates(new Vector2(x, reversedY));
            Actor possibleChar = gameStage.hit(possible.x, possible.y, true);
            if (possibleChar == null || possibleChar.getClass() != ScrabbleChar.class) {
                return;
            }
            ScrabbleChar scrabbleChar = (ScrabbleChar) possibleChar;
            ScrabbleField scrabbleField = (ScrabbleField) possibleChar.getParent();
            if (scrabbleField.canPutOn()) {
                this.affectedFields.remove(scrabbleField);
                this.focusedField = scrabbleField;
                scrabbleField.removePropOn();
                this.holdenChar = scrabbleChar;
                setHoldenChar(scrabbleChar, event);
                Sounds.TAKE.play();
            }
        }
    }

    private void setHoldenChar(ScrabbleChar scrabbleChar, InputEvent event) {
        Objects.requireNonNull(holdenChar);
        float scale = 1 / gameStageCamera.zoom;
        scrabbleChar.addAction(Actions.parallel(
                Actions.scaleTo(scale, scale),
                Actions.moveTo(event.getStageX() - scrabbleChar.getOriginX(), event.getStageY() - scrabbleChar.getOriginX()),
                Actions.touchable(Touchable.disabled)
        ));
        event.getStage().addActor(scrabbleChar);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (holdenChar != null && focusedField != null) {
            ScrabbleChar scrabbleChar = focusedField.putPropOn(holdenChar);
            this.affectedFields.add(focusedField);
            if (scrabbleChar != null) {
                scrabblesTable.addPlayerChar(scrabbleChar);
            }
            affectedFields.add(focusedField);
            this.focusedField.disableFocus();
            Sounds.PUT.play();
        } else if (holdenChar != null) {
            holdenChar.remove();
            holdenChar.setScale(1);
            this.scrabblesTable.addPlayerChar(this.holdenChar);
            Sounds.PUT.play();
        }
        this.focusedField = null;
        this.holdenChar = null;
        scrabblesTable.updateButtons();
    }


    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        if (holdenChar == null) {
            gameStageCamera.moveBy(-deltaX * 0.4f, -deltaY * 0.4f);
            return;
        }
        holdenChar.moveBy(deltaX, deltaY);
        float reversedY = ScrableConfig.SCREEN_HEIGHT - y;
        Vector2 possible = gameStage.screenToStageCoordinates(new Vector2(x, reversedY));
        Actor possibleField = gameStage.hit(possible.x, possible.y, true);
        if (possibleField != focusedField) {
            resetFocusedField();
        }
        if (possibleField == null) {
            return;
        }
        if (possibleField.getClass() == ScrabbleChar.class) {
            possibleField = possibleField.getParent();
        }

        if (possibleField != null && possibleField.getClass() == ScrabbleField.class) {
            ScrabbleField field = (ScrabbleField) possibleField;
            if (field.canPutOn()) {
                this.focusedField = field;
                this.focusedField.enableFocus();
            }
        }
    }

    private void resetFocusedField() {
        if (focusedField != null) {
            focusedField.disableFocus();
            this.focusedField = null;
        }
    }

    @Override
    public void zoom(InputEvent event, float initialDistance, float distance) {
        gameStageCamera.zoom(initialDistance, distance);
    }

    public Set<ScrabbleField> getAffectedFields() {
        return affectedFields;
    }

}
