package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slowna.game.asset.RegionTextures;
import com.slowna.game.asset.Sounds;

import java.util.LinkedList;
import java.util.List;

import static com.slowna.game.config.ScrableConfig.BLANK_LETTER;

public class StageTitle extends Table {

    private final static int MAX_LETTERS_IN_ROW = 7;
    private final LinkedList<ScrabbleMenuLetter> letters = new LinkedList<>();
    private final Runnable onFinish;

    public StageTitle(Stage stage, String text) {
        this(stage, text, () -> {
        });
    }

    public StageTitle(Stage stage, String text, Runnable onFinish) {
        setTouchable(Touchable.disabled);
        this.onFinish = onFinish;
        setFillParent(true);
        center();
        float letterSize = stage.getWidth() / 10f;
        List<Line> lines = toLines(text.split(" "));

        float letterPad = letterSize / 4f;
        TextureRegion texture = new TextureRegion(RegionTextures.CHARS.getTexture(null));
        for (Line line : lines) {
            Table table = new Table();
            char[] chars = line.toChars();
            for (char aChar : chars) {
                if (aChar == BLANK_LETTER) {
                    add().padRight(letterSize / 2f);
                    continue;
                }
                ScrabbleMenuLetter letter = new ScrabbleMenuLetter(letterSize, aChar, texture);
                letters.addLast(letter);
                table.add(letter).size(letterSize).padRight(letterPad).padTop(letterPad);
            }
            add(table).expandX();
            row();
        }
        validate();
        show(stage, onFinish);
    }


    private void show(Stage stage, Runnable onFinish) {
        float delay = 0.1f;
        float distance = stage.getHeight() + letters.get(0).getHeight();
        float stepTime = 0.8f;
        for (ScrabbleMenuLetter letter : letters) {
            letter.addAction(Actions.delay(delay,
                    Actions.sequence(
                            Actions.moveBy(0, distance),
                            Actions.show(),
                            Actions.moveBy(0, -distance, stepTime, Interpolation.smoother),
                            Actions.moveBy(0, 20f, stepTime, Interpolation.smooth),
                            Actions.moveBy(0, -distance, stepTime, Interpolation.smoother)
                    )));
            delay += 0.04f;
        }
        addAction(Actions.delay(delay, Actions.run(Sounds.DICES2::play)));
        addAction(Actions.delay(delay + stepTime, Actions.run(Sounds.DICES::play)));
        addAction(Actions.delay(delay + stepTime * 3,
                Actions.parallel(
                        Actions.run(onFinish),
                        Actions.removeActor()
                )
        ));
        stage.addActor(this);
    }


    private List<Line> toLines(String[] text) {
        List<Line> result = new LinkedList<>();
        Line current = new Line();
        for (String s : text) {
            if (current.canAddNextWord(s)) {
                current.add(s);
            } else {
                result.add(current);
                current = new Line();
                current.add(s);
            }
        }
        if (!result.contains(current)) {
            result.add(current);
        }
        return result;
    }

    private static class Line {
        private final List<String> words = new LinkedList<>();

        public void add(String word) {
            words.add(word);
        }

        public boolean canAddNextWord(String word) {
            if (word.isEmpty()) {
                return true;
            }
            return getLength() + word.length() + 1 < MAX_LETTERS_IN_ROW;
        }

        public int getLength() {
            return words.stream()
                    .mapToInt(String::length)
                    .sum() + words.size() - 1;

        }

        public char[] toChars() {
            return String.join(" ", words).toCharArray();
        }
    }
}
