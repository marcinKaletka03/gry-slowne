package com.slowna.game.gameData;

public interface Subscriber<T> {

    void onChange(T oldValue, T newValue);
}
