package com.slowna.game.config;


import com.slowna.game.pojo.ScrabbleFieldBonus;

public class DefaultTableConfiguration implements TableConfiguration {

    private static final int TABLE_SIZE = ScrableConfig.DEFAULT_TABLE_SIZE;
    private static final int MAX_INDEX = TABLE_SIZE - 1;

    @Override
    public int getTableSize() {
        return TABLE_SIZE;
    }

    @Override
    public int getLettersByPlayer() {
        return 7;
    }

    @Override
    public ScrabbleFieldBonus resolveFieldBonus(int x, int y) {
        if (x == TABLE_SIZE / 2 && y == TABLE_SIZE / 2) {
            return ScrabbleFieldBonus.CENTER;
        }
        if (resolveTripleWordMultiplier(x, y)) {
            return ScrabbleFieldBonus.MULTIPLY_TRIPLE_WORD;
        }
        if (resolveTwiceWordMultiplier(x, y)) {
            return ScrabbleFieldBonus.MULTIPLY_TWICE_WORD;
        }
        if (resolveTripleLetterMultiplier(x, y)) {
            return ScrabbleFieldBonus.MULTIPLY_TRIPLE_LETTER;
        }
        if (resolveTwiceLetterMultiplier(x, y)) {
            return ScrabbleFieldBonus.MULTIPLY_TWICE_LETTER;
        }
        return ScrabbleFieldBonus.DEFAULT;
    }

    private boolean resolveTripleWordMultiplier(int x, int y) {
        return ((x == MAX_INDEX || x == 0) && (y == MAX_INDEX || y == 0 || y == MAX_INDEX / 2)) ||
                ((x == MAX_INDEX / 2) && (y == MAX_INDEX || y == 0));
    }

    private boolean resolveTwiceWordMultiplier(int x, int y) {
        return ((x == y) && ((x > 0 && x < 5) || (x > 9 && x < MAX_INDEX))) ||
                ((x + y) == MAX_INDEX) && ((x > 0 && x < 5) || (x > 9 && x < MAX_INDEX));
    }

    private boolean resolveTripleLetterMultiplier(int x, int y) {
        return          //center
                ((x == y) && (x == 5 || x == 9)) ||
                        (Math.min(x, y) == 5 && Math.max(x, y) == 9) ||

                        //around
                        (Math.max(x, y) == 13 && (Math.min(x, y) == 5 || Math.min(x, y) == 9)) ||
                        (Math.min(x, y) == 1 && (Math.max(x, y) == 5 || Math.max(x, y) == 9));
    }

    private boolean resolveTwiceLetterMultiplier(int x, int y) {
        return           //around
                (Math.min(x, y) == 0 && (Math.max(x, y) == 3 || Math.max(x, y) == 11)) ||
                        (Math.max(x, y) == MAX_INDEX && (Math.min(x, y) == 3 || Math.min(x, y) == 11)) ||

                        //inner
                        (Math.min(x, y) == 2 && (Math.max(x, y) == 6 || Math.max(x, y) == 8)) ||
                        (Math.max(x, y) == 12 && (Math.min(x, y) == 6 || Math.min(x, y) == 8)) ||
                        (Math.min(x, y) == 3 && (Math.max(x, y) == 7)) ||
                        (Math.max(x, y) == 11 && (Math.min(x, y) == 7)) ||

                        //center
                        ((x == y) && (x == 6 || x == 8) || (Math.min(x, y) == 6 && Math.max(x, y) == 8));
    }

}
