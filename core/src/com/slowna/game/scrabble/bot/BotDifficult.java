package com.slowna.game.scrabble.bot;

import com.badlogic.gdx.math.MathUtils;
import com.slowna.game.App;
import com.slowna.game.gameData.GameData;
import com.slowna.game.pojo.WordPropositionWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum BotDifficult {

    EASY(7) {
        @Override
        public String choseStringToPut(Collection<String> words) {
            return words.stream()
                    .max(String::compareTo)
                    .orElse(null);
        }

        @Override
        protected WordPropositionWrapper chooseProposition(Collection<WordPropositionWrapper> propositions) {
            if (propositions.isEmpty()) {
                return WordPropositionWrapper.EMPTY;
            }
            List<WordPropositionWrapper> propositionList = new ArrayList<>(propositions);
            Collections.sort(propositionList);
            return propositionList.get(0);
        }

        @Override
        public String getName() {
            return App.lang().easy;
        }
    },
    MEDIUM(12) {
        @Override
        public String choseStringToPut(Collection<String> words) {
            if (words.isEmpty()) {
                return null;
            }
            return new ArrayList<>(words).get(MathUtils.random(words.size() - 1));
        }

        @Override
        protected WordPropositionWrapper chooseProposition(Collection<WordPropositionWrapper> propositions) {
            if (propositions.isEmpty()) {
                return WordPropositionWrapper.EMPTY;
            }
            return new ArrayList<>(propositions).get(MathUtils.random(propositions.size() - 1));
        }

        @Override
        public String getName() {
            return App.lang().medium;
        }
    },
    HARD(15) {
        @Override
        public String choseStringToPut(Collection<String> words) {
            return words.stream()
                    .max(String::compareTo)
                    .orElse(null);
        }

        @Override
        protected WordPropositionWrapper chooseProposition(Collection<WordPropositionWrapper> propositions) {
            if (propositions.isEmpty()) {
                return WordPropositionWrapper.EMPTY;
            }
            List<WordPropositionWrapper> propositionList = new ArrayList<>(propositions);
            Collections.sort(propositionList);
            return propositionList.get(0);
        }

        @Override
        public String getName() {
            return App.lang().hard;
        }
    };

    public final int maxWordLength;

    BotDifficult(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    public abstract String choseStringToPut(Collection<String> words);

    abstract WordPropositionWrapper chooseProposition(Collection<WordPropositionWrapper> propositions);

    protected WordPropositionWrapper chooseBestProposition(Collection<WordPropositionWrapper> propositions, GameData gameData, int lettersLeft) {
        if (propositions.isEmpty()) {
            return WordPropositionWrapper.EMPTY;
        }
        if (gameData.getTurnWithoutMoves() >= 2 || lettersLeft == 0) {
            return propositions.stream()
                    .max(WordPropositionWrapper::compareTo)
                    .orElse(null);
        }
        return chooseProposition(propositions);
    }

    public abstract String getName();

}
