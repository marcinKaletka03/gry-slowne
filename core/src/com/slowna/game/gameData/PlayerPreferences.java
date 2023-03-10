package com.slowna.game.gameData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.slowna.game.scrabble.actor.PlayerData;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Locale;

@Getter
public class PlayerPreferences {

    private static final Json json = new Json();
    private static final String PREFERENCES_NAME = "com.slowna.gamess";
    private static final String SAVE_NAME = "SAVED_DATA";

    private PlayerData playerData;


    public PlayerPreferences() {
        init();
    }

    public void updateGameData(GameData gameData) {
        playerData.updateGameData(gameData);
        saveData();
    }


    @SneakyThrows
    private void init() {
        if (this.playerData == null) {
            Preferences savedInstance = Gdx.app.getPreferences(PREFERENCES_NAME);
            if (!savedInstance.contains(SAVE_NAME)) {
                this.playerData = new PlayerData();
                this.playerData.setChosenLanguage(checkLanguage());
                this.saveData();
            } else {
                this.playerData = json.fromJson(PlayerData.class, savedInstance.getString(SAVE_NAME));
            }
        }
    }

    private AppLanguage checkLanguage() {
        try {
            return AppLanguage.ofLocaleOrEn(Locale.getDefault());
        } catch (Exception x) {
            return AppLanguage.EN;
        }
    }

    public void setLanguage(AppLanguage language) {
        playerData.setChosenLanguage(language);
        saveData();
    }

    @SneakyThrows
    public void saveData() {
        Preferences savedInstance = Gdx.app.getPreferences(PREFERENCES_NAME);
        savedInstance.putString(SAVE_NAME, json.toJson(playerData));
        savedInstance.flush();
    }


}
