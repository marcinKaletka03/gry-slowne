package com.slowna.game.scrabble;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.slowna.game.App;
import com.slowna.game.asset.Fonts;
import com.slowna.game.asset.Textures;
import com.slowna.game.extra.Notified;
import com.slowna.game.gameData.GameData;
import com.slowna.game.scrabble.actor.PauseMenu;

import java.util.concurrent.atomic.AtomicInteger;

public class ScoresTable extends Table {

    public ScoresTable(Stage stage, GameData gameData) {
        setBackground(new TextureRegionDrawable(Textures.SCORES.get()));
        float width = stage.getWidth();
        float stageHeight = stage.getHeight();
        float height = stageHeight / 8f;
        float itemHeight = height * 0.65f;
        float labelWidth = stage.getWidth() / 4f;
        setSize(width, height);
        pad(itemHeight / 10f);
        setY(stageHeight - height);
        top().left();

        ScoreMenu playerMenu = new ScoreMenu(itemHeight, labelWidth, App.lang().you);
        gameData.getPlayerPoints().addNotifiers(playerMenu.scoreLabel);
        add(playerMenu).size(playerMenu.getWidth(), playerMenu.getHeight()).expandX();

        Button pause = new Button(new TextureRegionDrawable(Textures.PAUSE.get()));
        pause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new PauseMenu(stage, gameData);
            }
        });
        pause.setSize(itemHeight * 1.1f, itemHeight * 1.1f);

        add(pause).size(pause.getHeight()).expandX();

        String botName = App.lang().getRandomName();

        if (botName.length() < 6) {
            botName += MathUtils.random((6 - botName.length()) * 10);
        }

        ScoreMenu botMenu = new ScoreMenu(itemHeight, labelWidth, botName);
        gameData.getOpponentPoints().addNotifiers(botMenu.scoreLabel);
        add(botMenu).size(botMenu.getWidth(), botMenu.getHeight()).expandX();
        debug();
    }

    private static class ScoreMenu extends Table {

        private final ScoreLabel scoreLabel;

        public ScoreMenu(float height, float width, String name) {
            Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.BROWN.get(), Color.WHITE);
            labelStyle.background = new TextureRegionDrawable(Textures.NAME.get());
            setSize(width, height);
            float fontScale = height / 175;
            Label label = new Label(name, labelStyle);
            label.setAlignment(Align.center);
            label.setFontScale(fontScale);

            label.setSize(width, height * 0.7f);
            top();
            this.scoreLabel = new ScoreLabel(fontScale * 1.5f, height);
            add(scoreLabel).size(scoreLabel.getWidth(), scoreLabel.getHeight()).expandX();
            add(label).size(label.getWidth(), label.getHeight()).expandX().padLeft(scoreLabel.getWidth() * -.25f);
            scoreLabel.toFront();
        }

    }


    private static class ScoreLabel extends Label implements Notified<Integer> {

        public ScoreLabel(float fontScale, float size) {
            super("0", new LabelStyle(Fonts.BROWN.get(), Color.WHITE));
            setFontScale(fontScale);
            setAlignment(Align.center);
            getStyle().background = new TextureRegionDrawable(Textures.POINTS.get());
            setSize(size, size);
        }

        @Override
        public void onChange(Integer newValue) {
            Integer currentValue = Integer.valueOf(getText().toString());
            AtomicInteger accumulator = new AtomicInteger(0);
            int increment = newValue - currentValue;
            addAction(
                    Actions.sequence(
                            Actions.repeat(increment,
                                    Actions.parallel(
                                            Actions.run(() -> setText(currentValue + accumulator.incrementAndGet())),
                                            Actions.scaleTo(1 + accumulator.get() / 10f, 1 + accumulator.get() / 10f, 0.1f)
                                    )),
                            Actions.scaleTo(1, 1, 1f)
                    ));
        }
    }
}
