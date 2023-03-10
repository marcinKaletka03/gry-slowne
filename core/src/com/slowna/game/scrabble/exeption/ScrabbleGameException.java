package com.slowna.game.scrabble.exeption;

public class ScrabbleGameException extends RuntimeException {

    public ScrabbleGameException() {
    }

    public ScrabbleGameException(String message) {
        super(message);
    }

    public ScrabbleGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrabbleGameException(Throwable cause) {
        super(cause);
    }

    public ScrabbleGameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
