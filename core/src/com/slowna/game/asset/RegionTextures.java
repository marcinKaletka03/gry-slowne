package com.slowna.game.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slowna.game.App;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public enum RegionTextures {
    CHARS("char.atlas") {
        public TextureRegion getTexture(Object points) {
            TextureAtlas atlas = atlas();
            TextureRegion region = atlas.findRegion(Objects.toString(points));
            if (region == null) {
                region = atlas.findRegion("alt");
            }
            return region;
        }
    },
    //    TETRIS_TABLE("tetrisTable.atlas"),
    ALL("All.atlas"),
    TABLE("table.atlas"),
    //    SPACE("space.atlas"),
    BG_CLOUDS("bgClouds.atlas"),
    FRONT_CLOUDS("frontClouds.atlas"),
    BOCIEK("bociek.atlas"),
    ;

    public final String path;


    public TextureRegion getTexture(Object o) {
        return atlas().findRegion(o.toString());
    }

    public TextureAtlas atlas() {
        return App.manager().getAtlas(this);
    }

    public TextureRegion getRandom() {
        return atlas().getRegions().random();
    }

}
