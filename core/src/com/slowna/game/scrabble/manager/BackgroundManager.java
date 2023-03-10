package com.slowna.game.scrabble.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.slowna.game.scrabble.actor.BackgroundCloud;
import com.slowna.game.scrabble.actor.Bociek;
import com.slowna.game.scrabble.actor.FrontCloud;


public class BackgroundManager {

    private final Stage stage;
    private float current;
    private float nextMoveTime;
    private static final float scale = 6;
    private static final float frontScale = 10;
    private static final float bociekScaleMin = 1 / 3f;
    private static final float bociekScaleMaX = 1.2f;

    public BackgroundManager(Stage stage) {
        this.stage = stage;
    }

    public void update(float delta) {
        current += delta;
        if (current < nextMoveTime) {
            return;
        }

        boolean front = MathUtils.randomBoolean();
        if (front) {
            makeBackgroundCloud(MathUtils.random(2) + 1);
        } else {
            makeFrontCloud(MathUtils.random(2) + 1);
        }
        if (MathUtils.randomBoolean()) {
            makeBociek();
        }
        makeBociek();
        setNextMoveTime();

    }

    public void makeBociek() {
        float bociekScale = MathUtils.random(bociekScaleMin, bociekScaleMaX);
        Bociek bociek = new Bociek(bociekScale, stage, MathUtils.randomBoolean());
        stage.addActor(bociek);
        bociek.move();
    }

    public void makeStartClouds() {
        for (int i = 0; i < MathUtils.random(2, 5); i++) {
            BackgroundCloud backgroundCLoud = new BackgroundCloud(scale);
            backgroundCLoud.setPosition(-stage.getWidth() / 2 * MathUtils.random(), (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());
            stage.addActor(backgroundCLoud);
            float time = MathUtils.random() * 10 + 10;
            backgroundCLoud.move(time, stage.getWidth() + backgroundCLoud.getWidth() * scale, (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());
        }
    }

    private void setNextMoveTime() {
        this.nextMoveTime = current + MathUtils.random() * 5 + 5;
    }

    private void makeFrontCloud(int number) {
        for (int i = 0; i < number; i++) {
            FrontCloud frontCloud = new FrontCloud(frontScale);
            frontCloud.setPosition(-frontCloud.getWidth() * frontScale, (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());
            stage.addActor(frontCloud);
            float time = MathUtils.random() * 5 + 7;
            frontCloud.move(time, stage.getWidth() + frontCloud.getWidth() * frontScale, (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());

        }
    }

    private void makeBackgroundCloud(int number) {
        for (int i = 0; i < number; i++) {
            BackgroundCloud backgroundCLoud = new BackgroundCloud(scale);
            backgroundCLoud.setPosition(-backgroundCLoud.getWidth() * scale, (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());
            stage.addActor(backgroundCLoud);
            float time = MathUtils.random() * 15 + 20;
            backgroundCLoud.move(time, stage.getWidth() + backgroundCLoud.getWidth() * scale, (MathUtils.random() * stage.getHeight()) - 0.25f * stage.getHeight());
        }
    }


}
