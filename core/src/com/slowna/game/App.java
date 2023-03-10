package com.slowna.game;

import com.badlogic.gdx.Gdx;
import com.slowna.game.asset.MusicManager;
import com.slowna.game.asset.MyAssetManager;
import com.slowna.game.extra.Notificator;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.gameData.PlayerPreferences;
import lombok.experimental.UtilityClass;

@UtilityClass
public class App {

    public static MyGdxGame game() {
        return (MyGdxGame) Gdx.app.getApplicationListener();
    }

    public static MyAssetManager manager() {
        return game().manager;
    }

    public static Notificator notificator() {
        return game().notificator;
    }

    public static AppLanguage lang() {
        return prefs().getPlayerData().getChosenLanguage();
    }

    public static PlayerPreferences prefs() {
        return game().preferences;
    }

    public static MusicManager musicManager() {
        return game().musicManager;
    }
}
