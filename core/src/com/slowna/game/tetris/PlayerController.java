package com.slowna.game.tetris;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PlayerController extends Table {

    public PlayerController(Stage stage, TetrisTable table, float size) {
        setSize(stage.getWidth(), size);
        center();

//        TextureRegion region = new TextureRegion(Textures.TETRIS_BUTTON.get());
//        region.flip(true, false);
//        Button left = new Button(new TextureRegionDrawable(region));
//        left.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                table.move(false);
//            }
//        });
//        add(left).size(size).expandX();


//        Button rotate = new Button(Textures.TETRIS_ROTATE.getDrawable());
//        rotate.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                table.rotate();
//            }
//        });
//        add(rotate).size(size).expandX();

//        Button down = new Button(Textures.TETRIS_BUTTON.getDrawable());
//        down.setSize(size,size);
//        down.setOrigin(Align.center);
//        down.setRotation(-90);
//        down.setTransform(true);
//        down.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                table.moveDown();
//            }
//        });
//        add(down).size(size).expandX();


//        Button right = new Button(Textures.TETRIS_BUTTON.getDrawable());
//        right.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                table.move(true);
//            }
//        });
//        add(right).size(size).expandX();

        stage.addActor(this);
    }
}
