package com.slowna.game.config;

import com.slowna.game.pojo.ScrabbleFieldBonus;

public interface TableConfiguration {
    /**
     * @return table size used for creating game
     */
    int getTableSize();


    int getLettersByPlayer();

    /**
     * @param x field position
     * @param y field position
     * @return ScrabbleFieldBonus assigned to given position
     */
    ScrabbleFieldBonus resolveFieldBonus(int x, int y);

}
