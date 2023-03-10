package com.slowna.game.pojo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;


@AllArgsConstructor
@EqualsAndHashCode
public class Position {
    public int x;
    public int y;

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }

}
