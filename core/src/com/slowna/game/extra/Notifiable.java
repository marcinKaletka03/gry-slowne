package com.slowna.game.extra;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Notifiable<T> {

    private T value;

    private final List<Notified<T>> onChangeListeners = new ArrayList<>();

    public void addNotifiers(Notified<T>... notifiers) {
        this.onChangeListeners.addAll(Arrays.asList(notifiers));
    }

    public void set(T t) {
        this.value = t;
        for (Notified<T> onChangeListener : onChangeListeners) {
            onChangeListener.onChange(t);
        }
    }

    public T getValue() {
        return value;
    }
}
