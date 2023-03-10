package com.slowna.game.asset;

import com.badlogic.gdx.utils.Array;
import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Musics {

    THEME(false, "theme.mp3"),
    DUCK("duck.mp3"),
    FROG("frog.mp3"),
    SNITH("snith.mp3"),
    RUN("run.mp3"),
    ;

    public final boolean gameMusic;

    public final String path;

    Musics(String name) {
        this.gameMusic = true;
        this.path = "sounds/" + name;
    }

    Musics(boolean gameMusic, String name) {
        this.gameMusic = gameMusic;
        this.path = "sounds/" + name;
    }

    public static final Array<Musics> gameSounds = new Array<>(Arrays.stream(Musics.values()).filter(Musics::isGameMusic).toArray(Musics[]::new));
}
