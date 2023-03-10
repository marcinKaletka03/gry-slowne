package com.slowna.game.scrabble.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slowna.game.asset.RegionTextures;
import com.slowna.game.config.DefaultTableConfiguration;
import com.slowna.game.core.Language;
import com.slowna.game.core.ScrabbleDictionary;
import com.slowna.game.core.ScrabbleDictionaryImpl;
import com.slowna.game.gameData.GameData;
import com.slowna.game.scrabble.ScoresTable;
import com.slowna.game.scrabble.actor.AnimatedImage;
import com.slowna.game.scrabble.actor.PlayerControl;
import com.slowna.game.scrabble.actor.ScrabbleTable;
import com.slowna.game.scrabble.actor.SmoothCamera;
import com.slowna.game.scrabble.bot.Bot;
import com.slowna.game.scrabble.bot.BotDifficult;

public class GameManager {

    private final Bot bot;
    private final GameData gameData;
    private final PlayerControl player;
    private final ScrabbleTable scrabbleTable;
    private final Stage gameStage;
    private final SmoothCamera camera;

    public GameManager(Stage gameStage, Stage menuStage, BotDifficult botDifficult) {
        this.gameStage = gameStage;
        this.camera = (SmoothCamera) gameStage.getCamera();
        ScrabbleDictionary dictionary = new ScrabbleDictionaryImpl(Language.ofAppLanguageOrEN());
        this.scrabbleTable = new ScrabbleTable(dictionary, new DefaultTableConfiguration());
        AnimatedImage background = new AnimatedImage(RegionTextures.TABLE, 1 / 15f);
        background.setSize(scrabbleTable.getWidth(), scrabbleTable.getHeight());
        background.setPosition(scrabbleTable.getX() + 1, scrabbleTable.getY() + 1);
        gameStage.addActor(background);
        gameStage.addActor(scrabbleTable);
        this.gameData = new GameData();
        this.bot = new Bot(scrabbleTable, menuStage, botDifficult, gameData);
        this.player = new PlayerControl(scrabbleTable, gameData, gameStage, menuStage);
        menuStage.addActor(this.player);
        ScoresTable table = new ScoresTable(menuStage, gameData);
        menuStage.addActor(table);
        start();
    }

    private void start() {
        gameStage.addAction(Actions.delay(4f, Actions.run(() -> {
            this.bot.addChars(scrabbleTable.poolChars(0));
            Boolean playerTurn = MathUtils.randomBoolean();
            camera.freezeAndMoveSmoothlyTo(gameStage.getWidth() / 2f, gameStage.getHeight() / 2f, 1, () -> {
                this.player.addNewChars(scrabbleTable.poolChars(0), () -> {
                    this.gameData.getTurnManager().setPlayerTurn(playerTurn);
                    this.player.updateButtons();
                });
            });
        })));
    }

    public void update(float delta) {
        this.bot.update(delta);
    }
}
