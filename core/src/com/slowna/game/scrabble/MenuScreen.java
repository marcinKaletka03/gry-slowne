package com.slowna.game.scrabble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Musics;
import com.slowna.game.asset.SingleTextures;
import com.slowna.game.asset.Textures;
import com.slowna.game.config.ScrableConfig;
import com.slowna.game.scrabble.actor.Menus;
import com.slowna.game.scrabble.actor.ScrabbleMenu;
import com.slowna.game.scrabble.manager.BackgroundManager;

public class MenuScreen extends ScreenAdapter {
    private final Stage stage;
    private final Viewport viewport;
    private final BackgroundManager backgroundManager;

    public static void play() {
        MyGdxGame game = App.game();
        game.setScreen(new MenuScreen(game));
    }

    public MenuScreen(MyGdxGame game) {
        Camera camera = new OrthographicCamera();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.stage = new Stage(viewport, game.batch);
        ScrabbleMenu menu = new ScrabbleMenu(game, stage, Menus.START_MENUS.getTitles());
        Image backgroundImage = new Image(Textures.SKY.get());
        backgroundImage.setFillParent(true);
        backgroundImage.toBack();
        stage.addActor(backgroundImage);
        Image sun = new Image(SingleTextures.SUN.getTexture());
        sun.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(sun);
        sun.setZIndex(ScrableConfig.SUN_INDEX);
        this.backgroundManager = new BackgroundManager(stage);
        this.backgroundManager.makeStartClouds();
        stage.addActor(menu);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        backgroundManager.update(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        App.musicManager().playMusic(Musics.THEME);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
