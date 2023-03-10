package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Fonts;
import com.slowna.game.asset.Textures;
import com.slowna.game.gameData.GameData;
import com.slowna.game.scrabble.MenuScreen;
import com.slowna.game.scrabble.ScrabbleGame;

public class PauseMenu extends Table {

    public PauseMenu(Stage stage, GameData gameData) {
        setBackground(new TextureRegionDrawable(Textures.ALPHA5.get()));
        setFillParent(true);
        setVisible(false);
        gameData.setPause(true);
        setTouchable(Touchable.enabled);

        Table main = new Table();
        TextureRegionDrawable drawable = new TextureRegionDrawable(Textures.PAUSE_MENU.get());
        float scale = stage.getWidth() * 0.95f / drawable.getMinWidth();
        main.setBackground(drawable);
        main.setSize(drawable.getMinWidth() * scale, drawable.getMinHeight() * scale);
        main.padTop(main.getHeight() / 5.5f);
        main.top();
        Label.LabelStyle style = new Label.LabelStyle(Fonts.STANDARD_WHITE.get(), Color.WHITE);
        Label pauseLabel = new Label(App.lang().pause, style);
        pauseLabel.setAlignment(Align.center);
        pauseLabel.setHeight(main.getHeight() * 0.135f);
        pauseLabel.setWidth(main.getWidth() * 0.9f);
        pauseLabel.setFontScale(stage.getWidth() / 300f);
        main.add(pauseLabel).size(pauseLabel.getWidth(), pauseLabel.getHeight()).colspan(3).expandX();
        main.row();

        float buttonSize = main.getWidth() * 0.25f;

        Button revert = new Button(new TextureRegionDrawable(Textures.REVERT.get()));
        revert.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(ScrabbleGame::play);
            }
        });
        revert.setSize(buttonSize, buttonSize);
        main.add(revert).size(buttonSize).colspan(1).padTop(pauseLabel.getHeight()).expandX();

        Button menu = new Button(new TextureRegionDrawable(Textures.MENU.get()));
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(() -> {
                    MyGdxGame game = App.game();
                    game.setScreen(new MenuScreen(game));
                });
            }
        });
        menu.setSize(buttonSize, buttonSize);
        main.add(menu).size(buttonSize).colspan(1).padTop(pauseLabel.getHeight() * 2f).expandX();


        Button play = new Button(new TextureRegionDrawable(Textures.PLAY.get()));
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(() -> {
                    gameData.setPause(false);
                });
            }
        });
        play.setSize(buttonSize, buttonSize);
        main.add(play).size(buttonSize).colspan(1).padTop(pauseLabel.getHeight()).expandX();


        this.add(main).size(main.getWidth(), main.getHeight());
        setZIndex(100);
        stage.addActor(this);
        show();
    }


    private void show() {
        addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.show(),
                Actions.alpha(1, 1)
        ));
    }

    private void hide(Runnable onFinish) {
        setTouchable(Touchable.disabled);
        addAction(Actions.sequence(
                Actions.alpha(0, 0.5f),
                Actions.run(onFinish)
        ));
    }
}
