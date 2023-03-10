package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slowna.game.core.ScrabbleDictionary;
import com.slowna.game.pojo.ScrabbleChar;

import java.util.List;
import java.util.stream.Collectors;

import static com.slowna.game.tetris.TetrisConfig.STANDARD_START_POSITION;

public class TetrisTable extends Table {

    private final TetrisField[][] fields;
    private TetrisShape currentShape;
    private TetrisShape nextShape;
    private ShapeFactory shapeFactory;
    private final ScrabbleDictionary dictionary;
    private final TetrisTableAnimation animation;
    private final List<Character> lettersPool;
    private float timeToGoDown = 2;
    private float current;

    public TetrisTable(float width, TetrisDifficult difficult, ScrabbleDictionary dictionary) {
        this.dictionary = dictionary;
        this.lettersPool = dictionary.getScrabbleCharsPoolForNewGame().stream()
                .map(ScrabbleChar::getLetter)
                .collect(Collectors.toList());
        this.animation = null;

        this.shapeFactory = new ShapeFactory(lettersPool, difficult, STANDARD_START_POSITION);
        setSize(width, width * 2f);
        fields = new TetrisField[TetrisConfig.fieldsInRow][TetrisConfig.fieldsInColumn];
        float fieldSize = width / TetrisConfig.fieldsInRow;
        for (int y = TetrisConfig.fieldsInColumn - 1; y >= 0; y--) {
            for (int x = 0; x < TetrisConfig.fieldsInRow; x++) {
                TetrisField field = new TetrisField(fieldSize, x, y);
                fields[x][y] = field;
                add(field).size(fieldSize);
            }
            row();
        }
        setColor(Color.BLUE);
        prepareNewShape();
        this.currentShape = shapeFactory.createRandomShape();
        animation.start();
    }

    @Override
    public void act(float delta) {
        this.current += delta;
        animation.update(delta);
        if (current > timeToGoDown) {
            gameStep();
        }
        super.act(delta);
    }

    private void gameStep() {
        if (!moveDown()) {
            if (!currentShape.isFinished()) {
                currentShape.applyFields();
            }
            this.currentShape = nextShape;
            updateNextMoveTime();
            prepareNewShape();
        }
    }

    public void rotate() {
        currentShape.rotateAll(fields);
    }

    public boolean moveDown() {
        if (currentShape.canMoveDown(fields)) {
            currentShape.moveStepDown(fields);
            updateNextMoveTime();
            return true;
        }
        return false;
    }

    public void move(boolean right) {
        currentShape.moveIfCan(right, fields);
    }

    public void updateNextMoveTime() {
        this.timeToGoDown = current + 1.2f;
    }

    private void prepareNewShape() {
        this.nextShape = shapeFactory.createRandomShape();
    }
}
