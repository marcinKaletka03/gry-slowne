package com.slowna.game.gameData;

import com.slowna.game.extra.Notifiable;
import lombok.Getter;

@Getter
public class GameData {

    private final Notifiable<Integer> playerPoints = new Notifiable<>(0);
    private final Notifiable<Integer> opponentPoints = new Notifiable<>(0);
    private final TurnManager turnManager = new TurnManager();
    private int turnWithoutMoves = 0;
    private boolean pause = false;

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void resetTurnWithoutMoves() {
        this.turnWithoutMoves = 0;
    }

    public void incrementTurnWithoutMoves() {
        this.turnWithoutMoves++;
    }

    public void addPlayerPoints(int points) {
        int playerPoints = this.playerPoints.getValue();
        this.playerPoints.set(playerPoints + points);
    }

    public void addOpponentPoints(int points) {
        int opponentPoints = this.opponentPoints.getValue();
        this.opponentPoints.set(opponentPoints + points);
    }
}
