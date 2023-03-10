package com.slowna.game.scrabble.actor;

import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.gameData.GameData;
import com.slowna.game.scrabble.bot.BotDifficult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerData {

    private AppLanguage chosenLanguage = AppLanguage.PL;
    private boolean soundOn = true;
    private BotDifficult lastPlayedDifficult = BotDifficult.EASY;
    private List<Integer> points = new ArrayList<>();

    public void updateGameData(GameData gameData) {
        points.add(gameData.getPlayerPoints().getValue());
        points = points.stream()
                .sorted((c1, c2) -> c2 - c1)
                .limit(3)
                .collect(Collectors.toList());
    }
}
