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

import java.util.concurrent.atomic.AtomicInteger;

public class FinishMenu extends Table {

    private final String scores;
    private final Label pointsLabel;
    private final int finishPoints;


    public FinishMenu(Stage stage, GameData gameData) {
        setFillParent(true);
        setBackground(new TextureRegionDrawable(Textures.ALPHA5.get()));
        setFillParent(true);
        setVisible(false);
        gameData.setPause(true);
        setTouchable(Touchable.enabled);

        Table main = new Table();
        TextureRegionDrawable drawable = new TextureRegionDrawable(Textures.FINISH_MENU.get());
        main.setBackground(drawable);
        float scale = stage.getWidth() * 0.95f / drawable.getMinWidth();
        main.setSize(drawable.getMinWidth() * scale, drawable.getMinHeight() * scale);

        main.padTop(main.getHeight() * 0.1f);
        this.scores = App.lang().scores;

        int playerScore = gameData.getPlayerPoints().getValue();
        this.finishPoints = playerScore;
        int botScore = gameData.getOpponentPoints().getValue();

        boolean win = playerScore > botScore;

        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.STANDARD_WHITE.get(), Color.WHITE);
        Label winLabel = new Label(win ? App.lang().won : App.lang().tryAgain, labelStyle);
        winLabel.setFontScale(stage.getWidth() / 375f);
        winLabel.setAlignment(Align.center);
        winLabel.setSize(main.getWidth() * 0.9f, main.getHeight() * 0.2f);
        main.add(winLabel).size(winLabel.getWidth(), winLabel.getHeight()).colspan(2);
        main.row();

        Label.LabelStyle labelStyle2 = new Label.LabelStyle(Fonts.STANDARD_WHITE.get(), Color.WHITE);
        pointsLabel = new Label(scores + " : 0", labelStyle2);
        pointsLabel.setFontScale(stage.getWidth() / 375f);
        pointsLabel.setAlignment(Align.center);
        pointsLabel.setSize(main.getWidth() * 0.9f, main.getHeight() * 0.2f);
        main.add(pointsLabel).size(pointsLabel.getWidth(), pointsLabel.getHeight()).colspan(2);
        main.row();

        float buttonSize = main.getHeight() / 4.5f;

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
        main.add(menu).size(buttonSize).colspan(1).padTop(buttonSize * 0.7f).expandX();

        center().add(main).size(main.getWidth(), main.getHeight());

        Button revert = new Button(new TextureRegionDrawable(Textures.REVERT.get()));
        revert.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide(ScrabbleGame::play);
            }
        });
        revert.setSize(buttonSize, buttonSize);
        main.add(revert).size(buttonSize).colspan(1).padTop(buttonSize * 0.7f).expandX();

        App.prefs().updateGameData(gameData);
        setZIndex(100);
        stage.addActor(this);
        show();
    }

    private void show() {
        addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.show(),
                Actions.alpha(1, 1),
                Actions.run(this::startCountPoints)
        ));
    }

    private void startCountPoints() {
        AtomicInteger currentPoints = new AtomicInteger();
        pointsLabel.addAction(
                Actions.repeat(finishPoints,
                        Actions.run(() -> {
                            pointsLabel.setText(scores + " : " + currentPoints.incrementAndGet());
                        }))
        );

    }

    private void hide(Runnable onFinish) {
        setTouchable(Touchable.disabled);
        addAction(Actions.sequence(
                Actions.alpha(0, 0.5f),
                Actions.run(onFinish)
        ));
    }
}
