package com.slowna.game.scrabble.extra;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Textures;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.scrabble.actor.ScrabbleMenu;

public interface MenuTittle {

    String getName(AppLanguage language);

    Runnable onClick(ScrabbleMenu menu, MyGdxGame game);

    default TextureRegion getTexture() {
        return  Textures.SCRABBLE.get();
    }

}
