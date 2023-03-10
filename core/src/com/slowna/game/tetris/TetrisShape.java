package com.slowna.game.tetris;

import com.slowna.game.pojo.Position;

import java.util.Arrays;

public class TetrisShape {

    protected TetrisChar[] chars;
    protected Position position;
    private boolean finished = false;

    public TetrisShape(TetrisChar[] chars, Position position) {
        this.chars = chars;
        this.position = position;
    }

    public void rotateAll(TetrisField[][] fields) {
        if (!canRotate(fields)) {
            return;
        }
        for (TetrisChar aChar : chars) {
            Position position = getNextRotationPosition(aChar);
            aChar.setYOffset(position.y);
            aChar.setXOffset(position.x);
        }
        estimatePosition();
        updateChars(fields);
    }

    public void moveIfCan(boolean right, TetrisField[][] fields) {
        if (canMove(right, fields)) {
            int xFactor = right ? 1 : -1;
            position.x += xFactor;
            updateChars(fields);
        }
    }

    private void updateChars(TetrisField[][] fields) {
        for (TetrisChar aChar : chars) {
            int yOffset = position.y + aChar.getYOffset();
            if (yOffset > TetrisConfig.maxColumnIndex) {
                continue;
            }
            aChar.changeField(fields[position.x + aChar.getXOffset()][yOffset]);
        }
        for (TetrisChar aChar : chars) {
            aChar.applyCurrentChar();
        }
    }

    public void moveStepDown(TetrisField[][] fields) {
        if (!canMoveDown(fields)) {
            return;
        }
        position.y -= 1;
        updateChars(fields);
    }

    public void applyFields() {
        for (TetrisChar aChar : chars) {
            TetrisField field = aChar.getCurrentField();
            if (field != null) {
                field.append();
            }
        }
        this.finished = true;
    }

    private void estimatePosition() {
        int minX = getMinX();
        if (minX < 0) {
            position.x -= minX;
            return;
        }
        int maxX = getMaX();
        if (maxX > TetrisConfig.maxRowIndex) {
            position.x += TetrisConfig.maxRowIndex - maxX;
        }
    }

    public boolean canRotate(TetrisField[][] fields) {
        for (TetrisChar aChar : chars) {
            Position nextPosition = getNextRotationPosition(aChar);
            if (nextPosition.x + position.x < 0 || nextPosition.x + position.x >= TetrisConfig.maxRowIndex) {
                continue;
            }
            TetrisField field = fields[nextPosition.x + position.x][nextPosition.y + position.y];
            if (field.isAppend()) {
                return false;
            }
        }
        return true;
    }

    public boolean canMove(boolean right, TetrisField[][] fields) {
        if (finished) {
            return false;
        }
        if (right && getMaX() >= TetrisConfig.maxRowIndex) {
            return false;
        } else if (!right && getMinX() <= 0) {
            return false;
        }
        int xMove = right ? 1 : -1;
        for (TetrisChar aChar : chars) {
            int yOffset = aChar.getYOffset() + position.y;
            if (yOffset >= TetrisConfig.maxColumnIndex) {
                continue;
            }
            TetrisField field = fields[aChar.getXOffset() + position.x + xMove][yOffset];
            if (field.isAppend()) {
                return false;
            }
        }
        return true;
    }

    private int getMaX() {
        return Arrays.stream(chars)
                .mapToInt(TetrisChar::getXOffset)
                .max()
                .orElse(0) + position.x;
    }

    private int getMinX() {
        return Arrays.stream(chars)
                .mapToInt(TetrisChar::getXOffset)
                .min()
                .orElse(0) + position.x;
    }

    public boolean canMoveDown(TetrisField[][] fields) {
        if (finished) {
            return false;
        }
        for (TetrisChar aChar : chars) {
            int nextY = position.y + aChar.getYOffset() - 1;
            if (nextY < 0) {
                return false;
            }
            if (nextY > fields[0].length) {
                continue;
            }
            TetrisField fieldUnder = fields[aChar.getXOffset() + position.x][nextY];
            if (fieldUnder.isAppend()) {
                return false;
            }
        }
        return true;
    }

    private Position getNextRotationPosition(TetrisChar tetrisChar) {
        return new Position(tetrisChar.getYOffset(), tetrisChar.getXOffset() * -1);
    }

    public boolean isFinished() {
        return finished;
    }
}
