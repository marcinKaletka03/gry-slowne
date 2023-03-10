package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slowna.game.App;
import com.slowna.game.asset.Textures;
import com.slowna.game.gameData.GameData;
import com.slowna.game.gameData.KeyboardState;
import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.scrabble.extra.MyConsumer;

import java.util.LinkedList;
import java.util.List;

public class SwapMenu extends Table {

    private final List<ScrabbleChar> charsToChange = new LinkedList<>();
    private final List<ScrabbleChar> allChars;
    private final MyButton swapButton;

    public SwapMenu(List<ScrabbleChar> enabledChars, GameData gameData, Stage stage, MyConsumer<List<ScrabbleChar>> onSwap) {
        this.allChars = enabledChars;
        gameData.getTurnManager().setKeyboardState(KeyboardState.DISABLED);
        setBackground(new TextureRegionDrawable(Textures.ALPHA5.get()));
        setSize(stage.getWidth(), stage.getHeight());
        setTouchable(Touchable.childrenOnly);
        top();

        float exitButtonSize = stage.getWidth() / 5f;
        Button exitButton = new Button(new TextureRegionDrawable(Textures.X.get()));
        exitButton.setSize(exitButtonSize, exitButtonSize);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAndRemove(() -> {
                    gameData.getTurnManager().setKeyboardState(KeyboardState.ENABLED);
                });
            }
        });
        add(exitButton).size(exitButtonSize).padBottom(exitButtonSize / 2f).padTop(stage.getHeight() / 6f).expandX();
        row();

        Table swapTable = new Table();
        float charSize = stage.getWidth() / 8f;
        float buttonWidth = stage.getWidth() * 0.45f;

        TextureRegionDrawable drawable = new TextureRegionDrawable(Textures.SWAP_TABLE.get());

        float scale = stage.getWidth() / drawable.getMinWidth();
        swapTable.setBackground(drawable);
        swapTable.setTouchable(Touchable.childrenOnly);
        swapTable.setSize(drawable.getMinWidth() * scale, drawable.getMinHeight() * scale);
        swapTable.pad(charSize / 2f);
        swapTable.top();
        for (ScrabbleChar scrabbleChar : enabledChars) {
            CharWrapper wrapper = new CharWrapper(scrabbleChar, charSize);
            swapTable.add(wrapper).size(wrapper.getWidth(), wrapper.getHeight()).colspan(2).spaceRight(wrapper.getWidth() / 20f).spaceBottom(charSize / 4f);
        }
        swapTable.row();
        swapButton = new MyButton(App.lang().swap, buttonWidth, charSize, () -> {
            hideAndRemove(() -> onSwap.consume(charsToChange));
        });
        swapTable.add(swapButton).size(swapButton.getWidth(), swapButton.getHeight()).colspan(enabledChars.size());

        MyButton changeAll = new MyButton(App.lang().swapAll, buttonWidth, charSize, () -> {
            hideAndRemove(() -> onSwap.consume(allChars));
        });

        swapTable.add(changeAll).size(swapButton.getWidth(), swapButton.getHeight()).colspan(enabledChars.size());

        swapButton.setEnabledFunction(() -> !charsToChange.isEmpty());
        updateSwapButton();
        setVisible(false);
        add(swapTable).size(swapTable.getWidth(), swapTable.getHeight()).expandX();

        stage.addActor(this);
        addAction(Actions.sequence(
                        Actions.alpha(0),
                        Actions.show(),
                        Actions.alpha(1, 0.5f)
                )
        );
    }

    private void hideAndRemove(Runnable onFinish) {
        setTouchable(Touchable.disabled);
        addAction(Actions.sequence(
                Actions.alpha(0, 0.5f),
                Actions.run(onFinish),
                Actions.removeActor()
        ));
    }

    private void updateSwapButton() {
        swapButton.update();
    }

    private class CharWrapper extends Table {

        private final ScrabbleChar charToChange;
        private boolean enable = false;

        private CharWrapper(ScrabbleChar charToChange, float size) {
            this.charToChange = charToChange;
            this.pad(size / 6f);
            setSize(size, size);
            ScrabbleChar c = new ScrabbleChar(charToChange);
            c.setTouchable(Touchable.disabled);
            this.center().add(c).size(size / 6 * 5f);
            setTouchable(Touchable.enabled);
            setEnable(enable);
            setZIndex(1);
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    enable = !enable;
                    setEnable(enable);
                    updateSwapButton();
                }
            });
        }

        public void setEnable(boolean enable) {
            if (enable) {
                charsToChange.add(charToChange);
                setBackground(new TextureRegionDrawable(Textures.GREEN.get()));
            } else {
                charsToChange.remove(charToChange);
                setBackground((Drawable) null);
            }
        }


    }
}
