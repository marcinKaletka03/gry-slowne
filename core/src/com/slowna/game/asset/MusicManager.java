package com.slowna.game.asset;

import com.badlogic.gdx.audio.Music;
import com.slowna.game.App;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {

    private Music current;

    private final Map<Musics, Music> map = new HashMap<>();

    public MusicManager() {
    }

    public void initMap(){
        for (Musics value : Musics.values()) {
            map.put(value, App.manager().getMusic(value));
        }
    }

    public void playMusic(Musics musics) {
        if (!App.prefs().getPlayerData().isSoundOn()) {
            return;
        }
        stopCurrent();
        this.current = map.get(musics);
        current.play();
    }

    public void stopCurrent() {
        if (current != null) {
            current.stop();
            current = null;
        }
    }

    public void playRandomGameMusic() {
        if (!App.prefs().getPlayerData().isSoundOn()) {
            return;
        }
        stopCurrent();
        Music music = map.get(Musics.gameSounds.random());
        this.current = music;
        music.play();
        music.setOnCompletionListener(s -> this.playRandomGameMusic());
    }


}
