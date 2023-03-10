package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slowna.game.App;
import com.slowna.game.asset.Sounds;
import com.slowna.game.asset.Textures;
import com.slowna.game.config.ScrableConfig;
import com.slowna.game.scrabble.manager.WordInputListener;
import com.slowna.game.gameData.GameData;
import com.slowna.game.gameData.KeyboardState;
import com.slowna.game.gameData.Subscriber;
import com.slowna.game.pojo.Direction;
import com.slowna.game.pojo.ScrabbleChar;
import com.slowna.game.pojo.ScrabbleField;
import com.slowna.game.scrabble.exeption.ScrabbleGameException;
import com.slowna.game.scrabble.manager.ScrabbleMoveListener;
import com.slowna.game.utill.CharUtils;

import java.util.*;

import static com.slowna.game.gameData.KeyboardState.DISABLED;
import static com.slowna.game.gameData.KeyboardState.ENABLED;

public class PlayerControl extends Table implements Subscriber<KeyboardState> {

    private final GameData gameData;
    private final ScrabbleTable scrabbleTable;
    private final WordInputListener wordInputListener = new WordInputListener();
    private final Table scrabbleChars;
    private final Stage menuStage;
    private final List<ScrabbleChar> playerCharsOnTable = new LinkedList<>();
    private final ScrabbleMoveListener scrabbleMoveListener;
    private final List<MyButton> buttonsToUpdate = new LinkedList<>();
    private final float charSize;

    public PlayerControl(ScrabbleTable scrabbleTable, GameData gameData, Stage gameStage, Stage menuStage) {
        addSubscriber(gameData, menuStage);
        setBackground(new TextureRegionDrawable(Textures.HOLDER.get()));
        this.scrabbleTable = scrabbleTable;
        this.menuStage = menuStage;
        this.gameData = gameData;
        float itemHeight = menuStage.getWidth() / 12f;
        this.charSize = menuStage.getWidth() / 10f;
        setSize(menuStage.getWidth(), itemHeight * 2.5f);
        this.scrabbleChars = createScrabbleTable();
        this.scrabbleChars.setHeight(itemHeight * 1.5f);
        top().left();
        gameData.getTurnManager().addKeyboardSubscriber(this);
        this.scrabbleMoveListener = new ScrabbleMoveListener(gameStage, this, gameData);


        add(scrabbleChars).size(scrabbleChars.getWidth(), scrabbleChars.getHeight()).colspan(4);
        row();
        MyButton[] buttons = createButtons(menuStage.getWidth() / 4.5f, itemHeight);

        for (MyButton button : buttons) {
            add(button).size(button.getWidth(), button.getHeight()).colspan(1).expand().padTop(-button.getHeight() / 6f);
            button.setDisabled(true);
            buttonsToUpdate.add(button);
        }
        menuStage.addListener(this.scrabbleMoveListener);
    }

    private void addSubscriber(GameData gameData, Stage menuStage) {
        gameData.getTurnManager().addTurnSubscriber((oldValue, newValue) -> {
            if (newValue) {
                if (gameData.getTurnWithoutMoves() >= ScrableConfig.TURNS_LEFT_TO_FINISH_GAME) {
                    new FinishMenu(menuStage, gameData);
                } else {
                    gameData.getTurnManager().setKeyboardState(ENABLED);
                    updateButtons();
                    new StageTitle(menuStage, App.lang().yourTurn);
                }
            } else {
                gameData.getTurnManager().setKeyboardState(DISABLED);
            }
        });
    }

    private MyButton[] createButtons(float width, float height) {
        MyButton[] buttons = new MyButton[4];


        MyButton apply = new MyButton(App.lang().accept, width, height, this::acceptWord);
        apply.setEnabledFunction(() -> !scrabbleMoveListener.getAffectedFields().isEmpty() && gameData.getTurnManager().getKeyboardState() == ENABLED);
        buttons[0] = apply;


        MyButton restore = new MyButton(App.lang().restore, width, height, this::restoreAllPropositions);
        restore.setEnabledFunction(() -> !scrabbleMoveListener.getAffectedFields().isEmpty() && gameData.getTurnManager().getKeyboardState() == ENABLED);
        buttons[1] = restore;


        MyButton swap = new MyButton(App.lang().swap, width, height, () -> {
            restoreAllPropositions();
            new SwapMenu(playerCharsOnTable, gameData, menuStage, (list) -> {
                list.forEach(ScrabbleChar::resetProp);
                playerCharsOnTable.removeAll(list);
                removePlayerChar(null);
                scrabbleTable.returnChars(list);
                poolLettersAndFinishTurn();
            });
        });

        swap.setEnabledFunction(() -> !playerCharsOnTable.isEmpty() && gameData.getTurnManager().getKeyboardState() == ENABLED && scrabbleTable.getLettersLeft() > 0);
        buttons[2] = swap;

        MyButton turn = new MyButton(App.lang().turn, width, height, () -> {
            restoreAllPropositions();
            poolLettersAndFinishTurn(false);
        });

        turn.setEnabledFunction(() -> gameData.getTurnManager().getKeyboardState() == ENABLED);
        buttons[3] = turn;
        return buttons;
    }

    private void restoreAllPropositions() {
        Set<ScrabbleField> fields = scrabbleMoveListener.getAffectedFields();
        for (ScrabbleField affectedField : fields) {
            addPlayerChar(affectedField.getPropOn());
            affectedField.removePropOn();
        }
        removePlayerChar(null);
        fields.clear();
    }

    public void updateButtons() {
        for (MyButton button : this.buttonsToUpdate) {
            button.update();
        }
    }

    public void removePlayerChar(ScrabbleChar scrabbleChar) {
        playerCharsOnTable.remove(scrabbleChar);
        scrabbleChars.reset();
        addAllCharsAndInvalidate();
    }

    public void addNewChars(Queue<ScrabbleChar> chars, Runnable onFinish) {
        if (chars.isEmpty()) {
            scrabbleChars.reset();
            addAllCharsAndInvalidate();
            onFinish.run();
            return;
        }
        ScrabbleChar scrabbleChar = chars.poll();
        scrabbleChar.setVisible(false);
        float time = 1.2f;
        menuStage.addActor(scrabbleChar);
        Sounds.BONUS.play();
        Light light = new Light(menuStage.getWidth() / 2f, menuStage.getHeight() / 2f, scrabbleChar.getWidth() * scrabbleChar.getScaleX(), time, scrabbleChar.getPoints());
        light.startAction(menuStage);
        scrabbleChar.addAction(Actions.sequence(
                Actions.scaleTo(0.5f, 0.5f),
                Actions.moveTo(menuStage.getWidth() / 2f, menuStage.getHeight() / 2f),
                Actions.show(),
                Actions.scaleTo(4, 4, time, Interpolation.elasticOut),
                Actions.run(() -> {
                    playerCharsOnTable.add(new ScrabbleChar(scrabbleChar));
                    scrabbleChars.reset();
                    addAllCharsAndInvalidate();
                    scrabbleChar.remove();
                })
        ));
        menuStage.addAction(
                Actions.delay(time * 1.05f, Actions.run(
                        () -> addNewChars(chars, onFinish)
                ))
        );
    }

    public void addPlayerChar(ScrabbleChar scrabbleChar) {
        playerCharsOnTable.add(scrabbleChar);
        scrabbleChar.resetProp();
        scrabbleChars.reset();
        addAllCharsAndInvalidate();
    }


    private Table createScrabbleTable() {
        Table table = new Table();
        table.setTouchable(Touchable.childrenOnly);
        table.setWidth(ScrableConfig.SCREEN_WIDTH);
        table.top();
        return table;
    }

    private void addAllCharsAndInvalidate() {
        for (ScrabbleChar scrabbleChar : playerCharsOnTable) {
            float scale = charSize / scrabbleChar.getWidth();
            scrabbleChar.addAction(Actions.scaleTo(scale, scale));
            scrabbleChar.setTouchable(Touchable.enabled);
            scrabbleChars.add(scrabbleChar).size(scrabbleChar.getWidth()).pad(charSize / 3f).padBottom(charSize / 2f);
        }
        scrabbleChars.addAction(Actions.run(scrabbleChars::invalidate));
    }

    private void acceptWord() {
        List<ScrabbleField> fields = new ArrayList<>(scrabbleMoveListener.getAffectedFields());
        Direction direction;
        ScrabbleField[] array = fields.toArray(new ScrabbleField[0]);

        try {
            direction = Direction.recognizeDirection(array);
        } catch (ScrabbleGameException e) {
            App.notificator().notify(App.lang().fieldsNotification);
            Sounds.ALERT.play();
            for (ScrabbleField field : fields) {
                field.alertWrong();
            }
            return;
        }

        Collections.sort(fields);

        if (CharUtils.containBlankLetters(array)) {
            acceptTypedWord(fields, direction);
            return;
        }
        try {
            gameData.getTurnManager().setKeyboardState(DISABLED);
            int points = scrabbleTable.putWord(fields, direction, () -> {
                scrabbleMoveListener.getAffectedFields().clear();
                scrabbleTable.setFirstWord(false);
                poolLettersAndFinishTurn();
            });
            gameData.addPlayerPoints(points);
        } catch (ScrabbleGameException x) {
            gameData.getTurnManager().setKeyboardState(ENABLED);
            Sounds.ALERT.play();
            App.notificator().notify(x.getMessage());
            for (ScrabbleField field : fields) {
                field.alertWrong();
            }
        }
    }

    private void acceptTypedWord(List<ScrabbleField> fields, Direction direction) {
        List<String> availableWords = scrabbleTable.getAvailableWords(fields, direction);
        if (availableWords.isEmpty()) {
            App.notificator().notify(App.lang().word_not_found);
            return;
        }
        wordInputListener.askForWord(availableWords, s -> {
            gameData.getTurnManager().setKeyboardState(DISABLED);
            int points = scrabbleTable.putWord(fields, direction, s, () -> {
                scrabbleMoveListener.getAffectedFields().clear();
                scrabbleTable.setFirstWord(false);
                poolLettersAndFinishTurn();
            });
            gameData.addPlayerPoints(points);
        });
    }

    private void poolLettersAndFinishTurn(boolean anyChange) {
        addNewChars(scrabbleTable.poolChars(playerCharsOnTable.size()), () -> {
            gameData.getTurnManager().setPlayerTurn(Boolean.FALSE);
            if (anyChange) {
                gameData.resetTurnWithoutMoves();
            } else {
                gameData.incrementTurnWithoutMoves();
            }
        });
    }

    private void poolLettersAndFinishTurn() {
        poolLettersAndFinishTurn(true);
    }

    @Override
    public void onChange(KeyboardState oldValue, KeyboardState newValue) {
        updateButtons();
    }
}
