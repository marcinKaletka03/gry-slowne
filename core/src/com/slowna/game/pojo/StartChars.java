package com.slowna.game.pojo;

import java.util.LinkedList;
import java.util.List;

public class StartChars {

    private List<ScrabbleCharInit> chars = new LinkedList<>();

    public StartChars() {
    }

    public StartChars(List<ScrabbleCharInit> chars) {
        this.chars = chars;
    }

    public List<ScrabbleCharInit> getChars() {
        return chars;
    }

    public void setChars(List<ScrabbleCharInit> chars) {
        this.chars = chars;
    }
}
