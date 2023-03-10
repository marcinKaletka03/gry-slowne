package com.slowna.game.scrabble.action;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CameraMoveAction extends TemporalAction {

    private final Camera camera;

    private float startX, startY;
    private final float endX;
    private final float endY;

    public CameraMoveAction(Camera camera, float xPos, float yPos) {
        super(1f, Interpolation.smooth);
        this.camera = camera;
        this.endX = xPos;
        this.endY = yPos;
    }


    protected void begin() {
        startX = camera.position.x;
        startY = camera.position.y;
    }

    protected void update(float percent) {
        float x, y;
        if (percent == 0) {
            x = startX;
            y = startY;
        } else if (percent == 1) {
            x = endX;
            y = endY;
        } else {
            x = startX + (endX - startX) * percent;
            y = startY + (endY - startY) * percent;
        }
        camera.position.set(x, y, 0);
    }


}
