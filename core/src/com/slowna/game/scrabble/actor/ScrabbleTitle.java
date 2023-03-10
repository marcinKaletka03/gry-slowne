package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Map;

import static com.slowna.game.config.ScrableConfig.BLANK_LETTER;

public class ScrabbleTitle extends Table {

    private final ScrabbleMenuLetter[] scrabbleMenuLetters;

    public ScrabbleTitle(String title, float letterSize, TextureRegion texture) {
        top().left();
        int index = 0;
        this.scrabbleMenuLetters = new ScrabbleMenuLetter[title.replace(" ", "").length()];
        for (char c : title.toCharArray()) {
            if (c == BLANK_LETTER) {
                add().padRight(letterSize / 3f);
            } else {
                ScrabbleMenuLetter scrabbleMenuLetter = new ScrabbleMenuLetter(letterSize, c, texture);
                scrabbleMenuLetters[index++] = scrabbleMenuLetter;
                scrabbleMenuLetter.setTouchable(Touchable.disabled);
                add(scrabbleMenuLetter).size(letterSize).padRight(letterSize / 8f);
            }
        }
        setTouchable(Touchable.enabled);
    }

    public void onClick(Runnable onClick) {
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });
    }

    public void show(float showTime, TittlesStartAction tittlesStartAction) {
        Map<Integer, Action> actionMap = tittlesStartAction.actionMap(scrabbleMenuLetters.length, showTime);
        for (int i = 0; i < scrabbleMenuLetters.length; i++) {
            ScrabbleMenuLetter letter = scrabbleMenuLetters[i];
            letter.show(actionMap.get(i));
        }

    }

    public void hide(float removeTime, TitlesFinishAction action) {
        action.init(scrabbleMenuLetters.length, removeTime, getStage());
        for (int i = 0; i < scrabbleMenuLetters.length; i++) {
            ScrabbleMenuLetter scrabbleMenuLetter = scrabbleMenuLetters[i];
            scrabbleMenuLetter.clearActions();
            scrabbleMenuLetter.addAction(action.getAction(i));
        }
    }
}
