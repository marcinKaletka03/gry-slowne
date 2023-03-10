package com.slowna.game.asset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public enum Textures {
    ALPHA5("alpha5"),
    SKY("sky"),
    HOLDER("holder1"),
    SWAP_TABLE("swapTable"),
    RED_S("redScrabble"),
    ALT_FIELD("altField"),
    CENTER("center"),
    POINTS("points"),
    SCORES("scores"),
    NAME("name"),
    GREEN_S("greenScrabble"),
    SCRABBLE("scrabble"),
    PAUSE("pause"),
    MENU("menu"),
    PLAY("play"),
    REVERT("revert"),
    CARPET("carpet1"),
    PAUSE_MENU("pauseMenu"),
    FINISH_MENU("finishMenu"),
    X("x"),
    L2("l2"),
    L3("l3"),
    W2("w2"),
    W3("w3"),
    BUTTON("button"),
    GREEN("green"),
    ;
    final String path;

    Textures(String path) {
        this.path = path;
    }

    public TextureRegion get() {
        return RegionTextures.ALL.getTexture(this.path);
    }


}
