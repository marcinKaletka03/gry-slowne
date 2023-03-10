package com.slowna.game.gameData;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class TurnManager {

    private Boolean playerTurn = null;
    @Getter
    private KeyboardState keyboardState = KeyboardState.DISABLED;

    private final Set<Subscriber<Boolean>> playerTurnSubscribers = new HashSet<>();
    private final Set<Subscriber<KeyboardState>> keyboardLockedSubscribers = new HashSet<>();

    public void addTurnSubscriber(Subscriber<Boolean> subscriber) {
        this.playerTurnSubscribers.add(subscriber);
    }

    public void addKeyboardSubscriber(Subscriber<KeyboardState> subscriber) {
        this.keyboardLockedSubscribers.add(subscriber);
    }

    public void setPlayerTurn(Boolean newValue) {
        if (newValue == playerTurn) {
            return;
        }
        Boolean oldValue = playerTurn;
        this.playerTurn = newValue;

        for (Subscriber<Boolean> playerTurnSubscriber : playerTurnSubscribers) {
            playerTurnSubscriber.onChange(oldValue, newValue);
        }

    }

    public void setKeyboardState(KeyboardState newValue) {
        if (keyboardState == newValue) {
            return;
        }
        KeyboardState oldValue = keyboardState;
        this.keyboardState = newValue;
        for (Subscriber<KeyboardState> keyboardStateSubscriber : keyboardLockedSubscribers) {
            keyboardStateSubscriber.onChange(oldValue, newValue);
        }
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }
}
