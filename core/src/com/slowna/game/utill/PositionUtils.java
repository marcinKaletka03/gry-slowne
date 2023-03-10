package com.slowna.game.utill;

import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

@UtilityClass
public class PositionUtils {

    public static <T> List<T> getByInvertedSpiralOrder(T[][] arr, Predicate<T> predicate) {
        List<T> result = new LinkedList<>();
        int size = arr.length;
        int i, j, N;

        int left, top;
        left = 0;
        top = size - 1;
        N = 1;
        for (i = 1; i <= size / 2; i++, left++, top--) {
            for (j = left; j <= top; j++, N++) {
                T obj = arr[left][j];
                if (predicate.test(obj)) {
                    result.add(obj);
                }
            }
            for (j = left + 1; j <= top; j++, N++) {
                T obj = arr[j][top];
                if (predicate.test(obj)) {
                    result.add(obj);
                }
            }
            for (j = top - 1; j >= left; j--, N++) {
                T obj = arr[top][j];
                if (predicate.test(obj)) {
                    result.add(obj);
                }
            }
            for (j = top - 1; j >= left + 1; j--, N++) {
                T obj = arr[j][left];
                if (predicate.test(obj)) {
                    result.add(obj);
                }
            }
        }
        return result;
    }


}
