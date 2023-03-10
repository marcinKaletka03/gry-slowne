package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Sounds;
import com.slowna.game.config.ScrabbleCharConfig;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.scrabble.extra.MenuTittle;

import java.util.ArrayList;
import java.util.List;

import static com.slowna.game.config.ScrableConfig.MENU_LETTERS_INDEX;

public class ScrabbleMenu extends Table {

    public List<ScrabbleTitle> titles = new ArrayList<>();
    private final MyGdxGame game;
    private final float CHAR_SIZE;

    public ScrabbleMenu(MyGdxGame game, Stage stage, MenuTittle... tittles) {
        this.game = game;
        this.CHAR_SIZE = stage.getWidth() / 13f;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / 2f);
        this.setY(stage.getHeight() / 4f);
        top().center();
        updateMenu(tittles);
        setTouchable(Touchable.childrenOnly);
        addAction(Actions.run(() -> setZIndex(MENU_LETTERS_INDEX)));
    }

    public void updateMenu(MenuTittle... menuTittles) {
        setTouchable(Touchable.disabled);
        float delayTime = titles.size() > 0 ? ScrabbleCharConfig.DEFAULT_ACTION_TIME : 0f;
        removeMenu(delayTime / 2f, delayTime, () -> this.applyNewChildren(menuTittles));
    }

    public void clearMenu(Runnable onFinish) {
        setTouchable(Touchable.disabled);
        float delayTime = titles.size() > 0 ? ScrabbleCharConfig.DEFAULT_ACTION_TIME : 0f;
        removeMenu(delayTime / 2f, delayTime, onFinish);
    }

    public void removeMenu(float clearDuration, float startDuration, Runnable onFinish) {
        TitlesFinishAction action = TitlesFinishAction.random();
        for (ScrabbleTitle title : titles) {
            title.hide(clearDuration, action);
        }
        Sounds.WHISTLE_Out.play();
        addAction(Actions.delay(startDuration, Actions.run(onFinish)));
    }

    private void applyNewChildren(MenuTittle... menuTittles) {
        clearChildren();
        this.titles.clear();
        AppLanguage chosenLanguage = App.lang();
        for (MenuTittle menuTittle : menuTittles) {
            ScrabbleTitle holder = new ScrabbleTitle(menuTittle.getName(chosenLanguage), CHAR_SIZE, menuTittle.getTexture());
            this.titles.add(holder);
            holder.setZIndex(getZIndex());
            holder.onClick(menuTittle.onClick(this, game));
            add(holder).spaceBottom(CHAR_SIZE);
            row();
        }
        setTouchable(Touchable.enabled);
        validate();

        Sounds.WHISTLE_IN.play();

        TittlesStartAction action = TittlesStartAction.random();
        for (ScrabbleTitle title : titles) {
            title.show(ScrabbleCharConfig.DEFAULT_ACTION_TIME, action);
        }
    }

}
