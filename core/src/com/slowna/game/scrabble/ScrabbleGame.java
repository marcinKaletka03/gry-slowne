package com.slowna.game.scrabble;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Textures;
import com.slowna.game.scrabble.actor.NeutralBatchStage;
import com.slowna.game.scrabble.actor.SmoothCamera;
import com.slowna.game.scrabble.bot.BotDifficult;
import com.slowna.game.scrabble.manager.GameManager;

import static com.slowna.game.config.ScrableConfig.SCREEN_HEIGHT;
import static com.slowna.game.config.ScrableConfig.SCREEN_WIDTH;

public class ScrabbleGame extends ScreenAdapter {

    private final Stage gameStage;
    private final Viewport gameViewport;
    private final Stage menuStage;
    private final GameManager gameManager;
    private final SpriteBatch batch;

    public static void play() {
        play(App.prefs().getPlayerData().getLastPlayedDifficult());
    }

    public static void play(BotDifficult difficult) {
        Game game = App.game();
        game.setScreen(new ScrabbleGame(difficult));
    }

    protected ScrabbleGame(BotDifficult difficult) {
        MyGdxGame game = App.game();
        game.preferences.getPlayerData().setLastPlayedDifficult(difficult);
        game.preferences.saveData();
        this.batch = game.batch;
        SmoothCamera gameCamera = new SmoothCamera();
        this.gameViewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, gameCamera);
        this.gameStage = new NeutralBatchStage(gameViewport, game.batch);
        gameCamera.setStage(gameStage);
        this.menuStage = new NeutralBatchStage(new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT), game.batch);
        Gdx.input.setInputProcessor(new InputMultiplexer(menuStage, gameStage));
        Table backgroundImage = new Table();
        backgroundImage.setBackground(new TextureRegionDrawable(Textures.CARPET.get()));
        backgroundImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.gameStage.addActor(backgroundImage);
        this.gameManager = new GameManager(gameStage, menuStage, difficult);
    }

    @Override
    public void show() {
        App.musicManager().playRandomGameMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1F);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        gameManager.update(delta);
        batch.begin();
        gameStage.act(delta);
        gameStage.draw();
        menuStage.act(delta);
        menuStage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void dispose() {
        this.batch.maxSpritesInBatch = 0;
    }
}
