package com.slowna.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.slowna.game.asset.MusicManager;
import com.slowna.game.asset.MyAssetManager;
import com.slowna.game.extra.Notificator;
import com.slowna.game.gameData.PlayerPreferences;
import com.slowna.game.scrabble.MenuScreen;

public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public final Notificator notificator;
    public MyAssetManager manager = new MyAssetManager();
    public PlayerPreferences preferences;
    public MusicManager musicManager = new MusicManager();

    public MyGdxGame(Notificator not) {
        notificator = not;
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        preferences = new PlayerPreferences();
        manager.init();
        setScreen(new ScreenAdapter() {
            @Override
            public void render(float delta) {
                ScreenUtils.clear(Color.WHITE);
                if (manager.isLoaded()) {
                    musicManager.initMap();
                    setScreen(new MenuScreen(MyGdxGame.this));
                }
            }
        });
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
