package com.slowna.game.scrabble.bot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slowna.game.App;
import com.slowna.game.config.ScrableConfig;
import com.slowna.game.gameData.GameData;
import com.slowna.game.gameData.Subscriber;
import com.slowna.game.pojo.*;
import com.slowna.game.scrabble.actor.FinishMenu;
import com.slowna.game.scrabble.actor.ScrabbleTable;
import com.slowna.game.scrabble.actor.SmoothCamera;
import com.slowna.game.scrabble.actor.StageTitle;
import com.slowna.game.scrabble.extra.ChunkProcess;
import com.slowna.game.utill.CharUtils;
import com.slowna.game.utill.PositionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.slowna.game.config.ScrableConfig.BLANK_LETTER;
import static com.slowna.game.config.ScrableConfig.DELAY_BETWEEN_SIGN;

public class Bot implements Subscriber<Boolean> {

    private static final float OFF = -1;

    private final Stage stage;
    private final ScrabbleTable table;
    private final BotDifficult difficult;
    private final GameData gameData;
    private final List<ScrabbleChar> chars = new LinkedList<>();

    private float currentTime = 0;
    private float nextMoveTime = OFF;

    private final SmoothCamera camera;
    private Collection<String> firstWordsToPut;

    private Direction currentDirection = Direction.HORIZONTALLY;
    private final Queue<ScrabbleField> fieldsToCheck = new LinkedList<>();
    private ChunkProcess<String, ScrabbleWordProposition> chunkProcess = ChunkProcess.getEmpty();
    private final Set<ScrabbleWordProposition> chunkPool = new HashSet<>();

    public Bot(ScrabbleTable table, Stage stage, BotDifficult difficult, GameData gameData) {
        this.table = table;
        this.stage = stage;
        this.difficult = difficult;
        this.gameData = gameData;
        this.gameData.getTurnManager().addTurnSubscriber(this);
        this.camera = (SmoothCamera) table.getStage().getCamera();
    }


    public void update(float delta) {
        lookForNewWords();

        if (Float.compare(nextMoveTime, OFF) == 0 || gameData.isPause()) {
            return;
        }

        currentTime += delta;
        if (table.isFirstWord()) {
            if (currentTime < nextMoveTime) {
                return;
            }
            putFirstWord();
        } else if (currentTime > nextMoveTime) {
            decideAndFinish();
        }
    }

    private void lookForNewWords() {
        if (chunkProcess.isFinish() && fieldsToCheck.isEmpty()) {
            return;
        }
        chunkProcess.process(100);
        if (chunkProcess.isFinish()) {

            this.chunkPool.addAll(chunkProcess.getResult());
            if (fieldsToCheck.isEmpty()) {
                return;
            }

            ScrabbleField fieldToCheck;
            if (currentDirection == Direction.HORIZONTALLY) {
                fieldToCheck = fieldsToCheck.peek();
            } else {
                fieldToCheck = fieldsToCheck.poll();
            }
            int maxWordLength = table.getLettersLeft() == 0 ? table.getScrabbleFields().length : difficult.maxWordLength;
            this.chunkProcess = table.getProcess(fieldToCheck.getPosition(), chars, currentDirection, maxWordLength);
            currentDirection = currentDirection.getOpposite();
        }
    }

    //todo remove unnecessary fields
    private void resetSearchingProcess() {
        this.fieldsToCheck.clear();
        this.chunkPool.clear();
        this.fieldsToCheck.addAll(PositionUtils.getByInvertedSpiralOrder(table.getScrabbleFields(), t -> t.getScrabbleCharOn() != null && table.countEmptyFieldsAround(t) > 0));
    }

    public void putFirstWord() {
        String word = difficult.choseStringToPut(firstWordsToPut);
        if (word == null) {
            finishTurn(false);
            return;
        }
        ScrabbleField[][] fields = table.getScrabbleFields();
        int center = fields.length / 2;
        ScrabbleField centerField = fields[center][center];

        int startIndex = table.getScrabbleFields().length / 2 - word.length() / 2;
        char[] chars = word.toCharArray();
        int size = word.length();
        this.nextMoveTime = OFF;
        moveAndRun(centerField, size, () -> {
            int points = 0;
            for (int i = 0; i < size; i++) {
                char c = chars[i];
                ScrabbleChar scrabbleChar;
                scrabbleChar = CharUtils.findFirstBy(this.chars, s -> s.getLetter() == c);
                if (scrabbleChar == null) {
                    scrabbleChar = CharUtils.findFirstBy(this.chars, s -> s.getLetter() == BLANK_LETTER);
                    if (scrabbleChar == null) {
                        throw new RuntimeException("empty char " + c);
                    }
                    scrabbleChar.setLetter(c);
                }
                ScrabbleField field = fields[center][startIndex + i];
                points += scrabbleChar.getPoints();
                field.putPropOn(scrabbleChar);
                this.chars.remove(scrabbleChar);
                field.applyProposition(i * DELAY_BETWEEN_SIGN, false);
            }
            gameData.addOpponentPoints(points);
            firstWordsToPut.clear();
            table.setFirstWord(false);
            stage.addAction(Actions.delay(size * DELAY_BETWEEN_SIGN, Actions.run(this::finishTurn)));
        });

    }

    public void decideAndFinish() {
        if (chunkPool.size() == 0) {
            swapAndFinish();
            return;
        }
        Set<WordPropositionWrapper> propositionWrappers = chunkPool.stream()
                .map(table::resolveProposition)
                .collect(Collectors.toSet());

        propositionWrappers.remove(WordPropositionWrapper.EMPTY);

        WordPropositionWrapper proposition = difficult.chooseBestProposition(propositionWrappers, gameData, table.getLettersLeft());
        if (proposition == WordPropositionWrapper.EMPTY) {
            swapAndFinish();
            return;
        }
        this.nextMoveTime = OFF;
        moveAndRun(proposition, () -> {
            putProposition(proposition.getMain(), this::finishTurn);
            for (ScrabbleWordProposition scrabbleWordProposition : proposition.getSide()) {
                putProposition(scrabbleWordProposition, () -> {
                });
            }
            gameData.addOpponentPoints(proposition.getPoints());
        });
    }

    private void swapAndFinish() {
        if (table.getLettersLeft() == 0) {
            finishTurn(false);
            return;
        }
        List<ScrabbleChar> randomChars = chars.stream()
                .filter((s) -> MathUtils.randomBoolean())
                .collect(Collectors.toList());
        chars.removeAll(randomChars);
        chars.addAll(table.swapChars(randomChars));
        finishTurn(true);
    }


    private void moveAndRun(WordPropositionWrapper proposition, Runnable onFinish) {
        ScrabbleCharProposition[] charPropositions = proposition.getMain().getScrabbleCharPropositions();
        int charsLength = charPropositions.length;
        int c = charsLength / 2;
        int x = charPropositions[c].getX();
        int y = charPropositions[c].getY();
        ScrabbleField center = table.getScrabbleFields()[x][y];
        moveAndRun(center, charsLength, onFinish);
    }

    private void moveAndRun(ScrabbleField field, int length, Runnable onFinish) {
        Vector2 result = field.localToStageCoordinates(new Vector2());
        result.add(field.getWidth() / 2f, field.getHeight() / 2f);
        camera.freezeAndMoveSmoothlyTo(result.x, result.y, length, onFinish);
    }

    private void putProposition(ScrabbleWordProposition proposition, Runnable onFinish) {
        ScrabbleField[][] fields = table.getScrabbleFields();
        float delay = 0f;
        for (ScrabbleCharProposition prop : proposition.getScrabbleCharPropositions()) {
            ScrabbleField lookedFor = fields[prop.getX()][prop.getY()];
            if (lookedFor.getScrabbleCharOn() != null) {
                lookedFor.applyProposition(delay += DELAY_BETWEEN_SIGN, false);
                continue;
            }
            ScrabbleChar scrabbleChar;
            scrabbleChar = CharUtils.findFirstBy(this.chars, s -> s.getLetter() == prop.getC());
            if (scrabbleChar == null) {
                scrabbleChar = CharUtils.findFirstBy(this.chars, s -> s.getLetter() == BLANK_LETTER);
                if (scrabbleChar == null) {
                    //toDo fix this problem
                    scrabbleChar = chars.get(0);
                }
                scrabbleChar.setLetter(prop.getC());
            }
            lookedFor.putPropOn(scrabbleChar);
            lookedFor.setBonusCaught(true);
            chars.remove(scrabbleChar);
            lookedFor.applyProposition(delay += DELAY_BETWEEN_SIGN, false);
        }
        stage.addAction(Actions.delay(delay, Actions.run(onFinish)));
    }

    private void finishTurn() {
        this.finishTurn(true);
    }

    private void finishTurn(boolean anyChange) {
        if (anyChange) {
            gameData.resetTurnWithoutMoves();
        } else {
            gameData.incrementTurnWithoutMoves();
        }
        chars.addAll(table.poolChars(chars.size()));
        nextMoveTime = OFF;
        resetSearchingProcess();
        gameData.getTurnManager().setPlayerTurn(Boolean.TRUE);
    }

    public void addChars(Iterable<ScrabbleChar> chars) {
        for (ScrabbleChar aChar : chars) {
            this.chars.add(aChar);
        }
    }


    private void startTurn() {
        new StageTitle(stage, App.lang().opponentTurn);
        this.fieldsToCheck.addAll(table.getLastAffectedFields());
        nextMoveTime = MathUtils.random(3) + 2;
        currentTime = 0;
        if (table.isFirstWord()) {
            this.firstWordsToPut = table.getBestWordsToUse(-1, this.chars, difficult.maxWordLength);
        }
    }

    @Override
    public void onChange(Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            if (gameData.getTurnWithoutMoves() >= ScrableConfig.TURNS_LEFT_TO_FINISH_GAME) {
                new FinishMenu(stage, gameData);
            } else {
                startTurn();
            }
        }
    }
}
