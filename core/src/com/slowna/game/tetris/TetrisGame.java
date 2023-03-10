package com.slowna.game.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.core.Language;
import com.slowna.game.core.ScrabbleDictionaryImpl;
import com.slowna.game.scrabble.actor.NeutralBatchStage;

import static com.slowna.game.config.ScrableConfig.SCREEN_HEIGHT;
import static com.slowna.game.config.ScrableConfig.SCREEN_WIDTH;

public class TetrisGame extends ScreenAdapter {

    private final Stage gameStage;
    private final Batch batch;

    public static void play() {
        MyGdxGame game = App.game();
        game.setScreen(new TetrisGame());
    }

    protected TetrisGame() {
        MyGdxGame game = App.game();
        this.batch = game.batch;
        Viewport viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, new OrthographicCamera());
        gameStage = new NeutralBatchStage(viewport, batch);

        float tetrisTableWidth = SCREEN_WIDTH * .75f;
//        AnimatedImage image = new AnimatedImage(RegionTextures.SPACE, 1/30f);
//        image.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
//        gameStage.addActor(image);
        TetrisTable tetrisTable = new TetrisTable(tetrisTableWidth, TetrisDifficult.MEDIUM, new ScrabbleDictionaryImpl(Language.PL));
        float playerControllerHeight = gameStage.getHeight() / 10f;

        tetrisTable.setPosition((SCREEN_WIDTH - tetrisTableWidth) / 2f, playerControllerHeight);
        gameStage.addActor(tetrisTable);
        new PlayerController(gameStage, tetrisTable, playerControllerHeight);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        gameStage.act(delta);
        gameStage.draw();
        batch.end();
    }
}
