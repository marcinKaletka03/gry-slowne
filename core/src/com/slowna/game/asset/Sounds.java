package com.slowna.game.asset;

import com.badlogic.gdx.audio.Sound;
import com.slowna.game.App;

public enum Sounds {

    TAKE("take.wav"),
    PUT("put.wav"),
    BONUS("bonus.mp3"),
    ALERT("alert.wav"),
    POINT("point.mp3"),
    DICES("fly3.wav"),
    DICES2("fly2.wav"),
    WHISTLE_IN("whistleIn.mp3"),
    WHISTLE_Out("whistleOut.mp3"),
    CLICK("click.wav"),
    ;

    public final String path;

    Sounds(String name) {
        this.path = "sounds/" + name;
    }

    public void play() {
        if (App.prefs().getPlayerData().isSoundOn()) {
            get().play();
        }
    }

    public Sound get() {
        return App.manager().getSound(this);
    }
}
