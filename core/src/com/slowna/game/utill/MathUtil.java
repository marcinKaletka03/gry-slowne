package com.slowna.game.utill;

import com.badlogic.gdx.math.Vector2;
import lombok.experimental.UtilityClass;

import static java.lang.Math.*;

@UtilityClass
public class MathUtil {

    public static Vector2 getRandomPointInCircle(float x, float y, float radius) {
        double a = random() * 2 * PI;
        double r = radius * sqrt(random());

        x += r * cos(a);
        y += r * sin(a);
        return new Vector2(x, y);
    }

    public static Vector2 getRandomPointOnCircle(float x, float y, float radius) {
        double a = Math.random() * Math.PI * 2;

        x += radius * cos(a);
        y += radius * sin(a);
        return new Vector2(x, y);
    }
}
