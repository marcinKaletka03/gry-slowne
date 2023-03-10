package com.slowna.game.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.slowna.game.pojo.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShapeFactory {

    private final List<Character> enabledChars;
    private final List<TetrisShapeType> enabledShapes;
    private final Position startPosition;

    public ShapeFactory(List<Character> enabledChars, TetrisDifficult difficult, Position startPosition) {
        this.startPosition = startPosition;
        this.enabledChars = enabledChars;
        this.enabledShapes = Arrays.stream(TetrisShapeType.values())
                .filter(s -> s.enableDifficulties.contains(difficult))
                .collect(Collectors.toList());
    }

    public TetrisShape createRandomShape() {
        TetrisShapeType random = enabledShapes.get(MathUtils.random(enabledShapes.size() - 1));
        Character[] randomChars = new Character[random.fieldsNumber];
        for (int i = 0; i < randomChars.length; i++) {
            randomChars[i] = enabledChars.get(MathUtils.random(enabledChars.size() - 1));
        }
        return random.createShape(new Position(startPosition), randomChars);
    }


    private enum TetrisShapeType {
        FIELD(1, Color.GREEN) {
            @Override
            public TetrisShape createShape(Position startPosition, Character... letters) {
                TetrisChar[] chars = new TetrisChar[1];
                chars[0] = new TetrisChar(0, 0, letters[0], color);

                return new TetrisShape(chars, startPosition);
            }
        },
        SHORT_LINE(2, Color.RED) {
            @Override
            public TetrisShape createShape(Position startPosition, Character... letters) {
                TetrisChar[] chars = new TetrisChar[2];
                chars[0] = new TetrisChar(0, 0, letters[0], color);
                chars[1] = new TetrisChar(0, 1, letters[1], color);

                return new TetrisShape(chars, startPosition);
            }
        },
        SHORT_L(3, Color.SKY) {
            @Override
            public TetrisShape createShape(Position startPosition, Character... letters) {
                TetrisChar[] chars = new TetrisChar[3];
                chars[0] = new TetrisChar(0, 0, letters[0], color);
                chars[1] = new TetrisChar(0, 1, letters[1], color);
                chars[2] = new TetrisChar(1, 0, letters[2], color);
                return new TetrisShape(chars, startPosition);
            }
        },
        Z(4, Color.BLUE) {
            @Override
            public TetrisShape createShape(Position startPosition, Character... letters) {
                TetrisChar[] chars = new TetrisChar[4];
                chars[0] = new TetrisChar(-1, 0, letters[0], color);
                chars[1] = new TetrisChar(0, 0, letters[1], color);
                chars[2] = new TetrisChar(0, -1, letters[2], color);
                chars[3] = new TetrisChar(1, -1, letters[3], color);
                return new TetrisShape(chars, startPosition);
            }
        };

        private final List<TetrisDifficult> enableDifficulties;
        private final int fieldsNumber;
        protected final Color color;

        TetrisShapeType(int fieldsNumber, Color color) {
            this.fieldsNumber = fieldsNumber;
            this.color = new Color(color);
            this.enableDifficulties = Arrays.asList(TetrisDifficult.values());
        }

        TetrisShapeType(List<TetrisDifficult> difficults, int fieldsNumber, Color color) {
            this.enableDifficulties = new ArrayList<>(difficults);
            this.fieldsNumber = fieldsNumber;
            this.color = new Color(color);
        }

        public abstract TetrisShape createShape(Position startPosition, Character... letters);
    }


}
