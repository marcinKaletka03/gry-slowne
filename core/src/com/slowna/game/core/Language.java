package com.slowna.game.core;

import com.slowna.game.App;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.pojo.ScrabbleCharInit;
import com.slowna.game.utill.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Words provider based on language provides all letters and words used in appropriate language
 * Use resource files to keep all words in separate lines
 */

public enum Language implements WordsProvider {

    PL, EN;

    public List<ScrabbleChar> getLettersPool() {
        ensureInit();
        return initChars.stream()
                .flatMap(s -> s.toScrabbleChars().stream())
                .collect(Collectors.toList());
    }

    public void ensureInit() {
        if (enabledWords == null) {
            this.enabledWords = FileUtils.loadGzipFile(this.name().toLowerCase() + ".zip");
        }
        if (initChars == null) {
            this.initChars = FileUtils.loadCharsFromFile(this.fileName + "Set.json").getChars();
        }
    }

    @Override
    public int getBasicPointsForChar(char ch) {
        ensureInit();
        return initChars.stream().filter(c -> c.getLetter() == ch)
                .mapToInt(ScrabbleCharInit::getPoints)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' char is not recognized", ch)));
    }

    @Override
    public void saveNewWord(String word) {
        FileUtils.saveNewLine(this.name().toLowerCase(), word);
    }

    @Override
    public List<String> getEnabledWords(int maxSize) {
        ensureInit();
        return enabledWords;
    }

    final String fileName;
    private List<ScrabbleCharInit> initChars;
    private List<String> enabledWords;

    Language() {
        this.fileName = name().toLowerCase();
    }

    public static Language ofAppLanguageOrEN() {
        AppLanguage appLanguage = App.lang();
        for (Language language : values()) {
            if (language.name().equals(appLanguage.name())) {
                return language;
            }
        }
        return Language.EN;
    }
}
