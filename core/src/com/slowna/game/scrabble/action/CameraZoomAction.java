package com.slowna.game.scrabble.action;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class CameraZoomAction extends TemporalAction {

    private final OrthographicCamera camera;

    private float startZoom;
    private final float endZoom;

    public CameraZoomAction(OrthographicCamera camera, float endZoom) {
        super(1f, Interpolation.smooth);
        this.camera = camera;
        this.endZoom = endZoom;
    }

    protected void begin() {
        startZoom = camera.zoom;
    }

    protected void update(float percent) {
        float currentZoom;
        if (percent == 0) {
            currentZoom = startZoom;
        } else if (percent == 1) {
            currentZoom = endZoom;
        } else {
            currentZoom = startZoom + (endZoom - startZoom) * percent;
        }
        camera.zoom = currentZoom;
    }
}
