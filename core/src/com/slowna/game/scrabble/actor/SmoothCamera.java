package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slowna.game.config.ScrableConfig;
import com.slowna.game.scrabble.action.CameraMoveAction;
import com.slowna.game.scrabble.action.CameraZoomAction;

public class SmoothCamera extends OrthographicCamera {

    private Stage stage;
    private static final float maxZoom = 1;
    private static final float minZoom = 0.3f;
    private static final float step = (maxZoom - minZoom) / 10f;
    private boolean canMove = true;

    public void freezeAndMoveSmoothlyTo(float x, float y, int letters, Runnable onFinish) {
        this.canMove = false;

        float requiredZoom = Math.max(minZoom, Math.min(maxZoom, step * letters));
        Vector2 finalPos = getNormalizedPosition(requiredZoom, x, y);
        stage.addAction(Actions.sequence(
                Actions.parallel(
                        new CameraMoveAction(this, finalPos.x, finalPos.y),
                        new CameraZoomAction(this, requiredZoom)
                ),
                Actions.run(() -> {
                    this.canMove = true;
                    onFinish.run();
                }))
        );
    }

    public void moveBy(float x, float y) {
        if (!canMove) {
            return;
        }
        translate(x, y);
        updatePosition();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void zoom(float initialDistance, float distance) {
        if (!canMove) {
            return;
        }
        zoom = Math.max(minZoom, Math.min((initialDistance / distance) * zoom, maxZoom));
        updatePosition();
    }

    public void updatePosition() {
        Vector2 camMin = new Vector2(viewportWidth, viewportHeight);
        camMin.scl(zoom / 2);
        Vector2 camMax = new Vector2(ScrableConfig.SCREEN_WIDTH, ScrableConfig.SCREEN_HEIGHT);
        camMax.sub(camMin);

        float camX = Math.min(camMax.x, Math.max(position.x, camMin.x));
        float camY = Math.min(camMax.y, Math.max(position.y, camMin.y));

        position.set(camX, camY, 0);
        update();
    }

    public Vector2 getNormalizedPosition(float finalZoom, float finalX, float finalY) {
        Vector2 camMin = new Vector2(viewportWidth, viewportHeight);
        camMin.scl(finalZoom / 2);
        Vector2 camMax = new Vector2(ScrableConfig.SCREEN_WIDTH, ScrableConfig.SCREEN_HEIGHT);
        camMax.sub(camMin);

        float camX = Math.min(camMax.x, Math.max(finalX, camMin.x));
        float camY = Math.min(camMax.y, Math.max(finalY, camMin.y));
        return new Vector2(camX, camY);
    }
}
