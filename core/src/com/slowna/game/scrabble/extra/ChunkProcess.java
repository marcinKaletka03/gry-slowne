package com.slowna.game.scrabble.extra;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChunkProcess<T1, T2> {

    private static final ChunkProcess emptyProcess = new ChunkProcess<>(Collections.emptyList(), (t) -> false, t1 -> null);

    private final static int chunkSize = 1000;
    private final int max;
    private int cursor = 0;
    private boolean finish;
    private final Predicate<T1> predicate;
    private final Function<T1, T2> function;
    private final Set<T2> result = new HashSet<>();
    private final List<T1> payload;

    public ChunkProcess(List<T1> payload, Predicate<T1> predicate, Function<T1, T2> function) {
        if (payload.isEmpty()) {
            finish = true;
        }
        this.payload = payload;
        this.max = payload.size();
        this.predicate = predicate;
        this.function = function;
    }

    public static <T1, T2> ChunkProcess<T1, T2> getEmpty() {
        return emptyProcess;
    }

    public void process() {
        process(chunkSize);
    }

    public void process(int chunkSize) {
        if (finish) {
            return;
        }
        int start = cursor;
        int breakPoint = Math.min(cursor += chunkSize, max);
        for (int i = start; i < breakPoint; i++) {
            T1 obj = payload.get(i);
            if (predicate.test(obj)) {
                result.add(function.apply(obj));
            }
        }
        if (breakPoint == max) {
            finish = true;
        }
    }

    public boolean isFinish() {
        return finish;
    }

    public Set<T2> getResult() {
        return result;
    }
}
