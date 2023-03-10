package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slowna.game.utill.ShapeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TitlesFinishAction {
    GO_AROUND {
        @Override
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            List<Vector2> pointsAround = ShapeUtil.makeCirclesPoints(stage.getWidth(), number);
            for (int i = 0; i < number; i++) {
                Vector2 pos = pointsAround.get(i);
                float randomRotation = randomSignValue(500);
                Action action = Actions.parallel(
                        Actions.sequence(
                                Actions.delay(duration / 3f),
                                Actions.moveBy(pos.x, pos.y, duration / 2f, Interpolation.circleIn),
                                Actions.removeActor()),
                        Actions.rotateBy(randomRotation, duration, Interpolation.exp5In));
                actionsMap.put(i, action);
            }

        }
    },

    SPLIT_HORIZONTAL_LEFT {
        @Override
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            for (int i = 0; i < number; i++) {
                float delay = duration / 2f - (duration / 2f * i / number);
                actionsMap.put(number - i - 1,
                        delayedRemoveAction(Actions.delay(delay,
                                parallelMoveAction(-stage.getWidth(), MathUtils.random(stage.getHeight()) - stage.getHeight() / 2f,
                                        randomSignValue(360), duration)), delay + duration));
            }
        }
    },
    SPLIT_HORIZONTAL_RIGHT {
        @Override
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            for (int i = 0; i < number; i++) {
                float delay = duration / 2f - (duration / 2f * i / number);
                actionsMap.put(i,
                        delayedRemoveAction(
                                Actions.delay(delay, parallelMoveAction(stage.getWidth(), MathUtils.random(stage.getHeight()) - stage.getHeight() / 2f,
                                        randomSignValue(360), duration)), delay + duration));
            }

        }
    },
    SPLIT_HORIZONTAL_ODD {
        boolean left = false;

        @Override
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            float xMove = left ? stage.getWidth() : -stage.getWidth();
            float rotation = left ? -400 : 400;
            for (int i = 0; i < number; i++) {
                actionsMap.put(i, delayedRemoveAction(
                        parallelMoveAction(xMove, 0,
                                rotation, duration), duration));
            }
            left = !left;
        }

    },

    SPLIT_VERTICAL_DOWN {
        @Override
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            for (int i = 0; i < number; i++) {
                float delay = duration / 2f - (duration / 2f * i / number);
                actionsMap.put(i, delayedRemoveAction(
                        Actions.delay(delay,
                                parallelMoveAction(MathUtils.random(stage.getWidth()) - stage.getWidth() / 2f, -stage.getHeight(),
                                        randomSignValue(360), duration)
                        ),
                        delay + duration));
            }

        }
    },


    GROW_UP_AND_FLY {
        public void init(int number, float duration, Stage stage) {
            actionsMap.clear();
            List<Vector2> pointsAround = ShapeUtil.makeCirclesPoints(stage.getWidth() * 1.5f, number);
            for (int i = 0; i < number; i++) {
                Vector2 pos = pointsAround.get(i);
                float randomRotation = randomSignValue(500);
                float randomScale = MathUtils.random() + 4;
                Action action =
                        Actions.sequence(
                                Actions.parallel(
                                        Actions.scaleTo(randomScale, randomScale, duration, Interpolation.pow2),
                                        Actions.moveBy(pos.x, pos.y, duration, Interpolation.pow2),
                                        Actions.rotateBy(randomRotation, duration, Interpolation.pow2)),
                                Actions.removeActor());
                actionsMap.put(i, action);
            }

        }
    };

    private static Action parallelMoveAction(float xMove, float yMove, float rotation, float duration) {
        return Actions.parallel(
                Actions.moveBy(0, yMove, duration, Interpolation.circleIn),
                Actions.moveBy(xMove, 0, duration, Interpolation.circleIn),
                Actions.rotateBy(rotation, duration, Interpolation.pow2)
        );
    }

    private static Action delayedRemoveAction(Action action, float delay) {
        return Actions.sequence(action, Actions.delay(delay, Actions.removeActor()));
    }

    public float randomSignValue(float max) {
        return MathUtils.random(max * 2) - max;
    }


    protected final Map<Integer, Action> actionsMap = new HashMap<>();

    private static final TitlesFinishAction[] ACTIONS = values();

    public abstract void init(int number, float duration, Stage stage);

    public Action getAction(int index) {
        return actionsMap.get(index);
    }

    public static TitlesFinishAction random() {
        return ACTIONS[MathUtils.random(ACTIONS.length - 1)];
    }
}
