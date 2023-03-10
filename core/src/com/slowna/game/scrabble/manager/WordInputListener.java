package com.slowna.game.scrabble.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.slowna.game.App;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class WordInputListener implements Input.TextInputListener {

    private List<String> availableWords;
    private int wordLength;
    private Consumer<String> onHit;
    private final Pattern onlyLettersPattern = Pattern.compile("^\\w+$");

    public void askForWord(List<String> availableWords, Consumer<String> onHit) {
        Objects.requireNonNull(onHit);
        this.availableWords = availableWords;
        this.onHit = onHit;
        this.wordLength = availableWords.get(0).length();
        Gdx.input.getTextInput(this, App.lang().put_word, "", "");
    }

    @Override
    public void input(String text) {
        text = text.toLowerCase().trim();
        if (!onlyLettersPattern.matcher(text).matches()) {
            repeatAsk("word must contains only letters");

        } else if (text.length() != wordLength) {
            repeatAsk(App.lang().bad_length);

        } else if (availableWords.contains(text)) {
            onHit.accept(text);
        } else {
            repeatAsk(App.lang().word_not_found);
        }
    }

    private void repeatAsk(String text) {
        Gdx.input.getTextInput(this, text, "", "");
    }


    @Override
    public void canceled() {

    }
}
