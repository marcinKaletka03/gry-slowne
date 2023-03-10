package com.slowna.game.pojo;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slowna.game.asset.Textures;

public enum ScrabbleFieldBonus {
    MULTIPLY_TWICE_LETTER {
        @Override
        public TextureRegion getTexture() {
            return Textures.L2.get();
        }

        @Override
        public int multiplyLetter(int value) {
            return value * 2;
        }
    },
    MULTIPLY_TRIPLE_LETTER {
        @Override
        public TextureRegion getTexture() {
            return Textures.L3.get();
        }

        @Override
        public int multiplyLetter(int value) {
            return value * 3;
        }
    },
    MULTIPLY_TWICE_WORD(true) {
        @Override
        public TextureRegion getTexture() {
            return Textures.W2.get();
        }

        @Override
        public int multiplyWord(int value) {
            return value * 2;
        }
    },
    MULTIPLY_TRIPLE_WORD(true) {
        @Override
        public TextureRegion getTexture() {
            return Textures.W3.get();
        }

        @Override
        public int multiplyWord(int value) {
            return value * 3;
        }
    },
    CENTER {
        @Override
        public TextureRegion getTexture() {
            return Textures.CENTER.get();
        }
    },
    DEFAULT {
        @Override
        public TextureRegion getTexture() {
            return Textures.ALT_FIELD.get();
        }
    };

    public abstract TextureRegion getTexture();

    public final boolean isWordBonus;

    ScrabbleFieldBonus(boolean isWordBonus) {
        this.isWordBonus = isWordBonus;
    }

    ScrabbleFieldBonus() {
        this.isWordBonus = false;
    }

    public int multiplyLetter(int value) {
        return value;
    }

    public int multiplyWord(int value) {
        return value;
    }

}
