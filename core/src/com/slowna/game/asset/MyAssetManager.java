package com.slowna.game.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class MyAssetManager {
    private final AssetManager assetManager;

    public MyAssetManager() {
        this.assetManager =  new AssetManager();
    }

    public void init() {
        for (SingleTextures txt : SingleTextures.values()) {
            assetManager.load(txt.path, Texture.class, new TextureLoader.TextureParameter() {{
                this.magFilter = Texture.TextureFilter.Linear;
                this.minFilter = Texture.TextureFilter.Linear;
            }});
        }

        for (RegionTextures textures : RegionTextures.values()) {
            assetManager.load(textures.path, TextureAtlas.class);
        }

        for (Sounds sounds : Sounds.values()) {
            assetManager.load(sounds.path, Sound.class);
        }

        for (Musics music : Musics.values()) {
            assetManager.load(music.path, Music.class);
        }

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(resolver));
        loadFonts();
        Texture.setAssetManager(assetManager);
    }

    public void dispose() {
        assetManager.dispose();
    }

    public boolean isLoaded() {
        assetManager.update();
        return assetManager.isFinished();
    }

    Texture getTexture(SingleTextures texture) {
        return assetManager.get(texture.path);
    }

    TextureAtlas getAtlas(RegionTextures text) {
        return assetManager.get(text.path);
    }

    Sound getSound(Sounds sound) {
        return assetManager.get(sound.path);
    }

    Music getMusic(Musics music) {
        return assetManager.get(music.path);
    }

    BitmapFont getFont(Fonts font) {
        return assetManager.get(font.name());
    }


    private void loadFonts() {
        int fontSize = Fonts.FONT_SIZE;
        for (Fonts font : Fonts.values()) {
            FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            parameter.fontFileName = font.path;
            parameter.fontParameters.characters = "\u0000AĄBCĆDEĘFGHIJKLŁMNŃOOPQRSŚTUVWXYZŻŹaąbcćdeęfghijklłmnńoópqrsśtuvwxyzżź1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~*\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F\u00A0¡¢£¤¥¦§¨©ª«¬\u00AD®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
            parameter.fontParameters.size = fontSize;
            parameter.fontParameters.color = font.color;
            parameter.fontParameters.genMipMaps = true;
            if (font.shadowColor != null) {
                parameter.fontParameters.shadowColor = font.shadowColor;
                parameter.fontParameters.shadowOffsetY = font.shadowWidth;
                parameter.fontParameters.shadowOffsetX = font.shadowWidth;
            }
            parameter.fontParameters.minFilter = Texture.TextureFilter.Linear;
            parameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
            assetManager.load(font.name(), BitmapFont.class, parameter);
        }
    }

}
