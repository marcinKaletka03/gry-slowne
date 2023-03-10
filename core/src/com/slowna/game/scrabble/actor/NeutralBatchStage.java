package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NeutralBatchStage extends Stage {

    private final Viewport viewport;
    private final Batch batch;
    private final Group root;

    public NeutralBatchStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        this.viewport = viewport;
        this.batch = batch;
        this.root = getRoot();
    }

    @Override
    public void draw() {
        Camera camera = viewport.getCamera();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        root.draw(batch, 1);
    }
}
