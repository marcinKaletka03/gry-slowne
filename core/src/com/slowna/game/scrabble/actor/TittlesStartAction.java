package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slowna.game.config.ScrableConfig;

import java.util.HashMap;
import java.util.Map;

public enum TittlesStartAction {

    GROW_UP {
        @Override
        public Map<Integer, Action> actionMap(int number, float duration) {
            Map<Integer, Action> result = new HashMap<>();
            for (int i = 0; i < number; i++) {
                result.put(i,
                        Actions.delay((duration / 4f * i / number),
                                Actions.sequence(
                                        Actions.parallel(
                                                Actions.scaleTo(0, 0),
                                                Actions.rotateBy(MathUtils.random(180) + 180 * (MathUtils.randomBoolean() ? 1 : -1)),
                                                Actions.show()),
                                        Actions.parallel(
                                                Actions.scaleTo(1, 1, duration / 2f, Interpolation.elasticOut),
                                                Actions.rotateTo(0, duration / 2f, Interpolation.exp10Out)
                                        )
                                )));
            }
            return result;
        }
    },
    FLY_DOWN {
        @Override
        public Map<Integer, Action> actionMap(int number, float duration) {
            Map<Integer, Action> result = new HashMap<>();
            for (int i = 0; i < number; i++) {
                float amount = ScrableConfig.SCREEN_HEIGHT / 2f;
                result.put(i,
                        Actions.delay((duration / 4f * i / number),
                                Actions.sequence(
                                        Actions.parallel(
                                                Actions.moveBy(0, amount),
                                                Actions.rotateBy(MathUtils.random(180) + 180 * (MathUtils.randomBoolean() ? 1 : -1)),
                                                Actions.show()),
                                        Actions.parallel(
                                                Actions.moveBy(0, -amount, duration, new Interpolation.ElasticOut(2, 15f, 6, 0.3f)),
                                                Actions.rotateTo(0, duration, Interpolation.exp10Out)
                                        )
                                )));
            }
            return result;
        }
    },
    ;
    private static final TittlesStartAction[] VALUES = values();

    public static TittlesStartAction random() {
        return VALUES[MathUtils.random(VALUES.length - 1)];
    }

    public abstract Map<Integer, Action> actionMap(int number, float duration);
}
