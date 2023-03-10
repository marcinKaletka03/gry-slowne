package com.slowna.game.asset;

import com.badlogic.gdx.graphics.Texture;
import com.slowna.game.App;

public enum SingleTextures {

    STAR("star.png"),
    LIGHT("light.png"),
    SUN("sun.png"),
    ;
    final String path;

    SingleTextures(String path) {
        this.path = path;
    }

    public Texture getTexture() {
        return App.manager().getTexture(this);
    }
}
