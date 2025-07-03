package org.libin.game.entities;

import org.libin.game.systems.GameContext;

public class Decoration extends Entity{
    public Decoration(Sprite sprite) {
        super(sprite);
    }

    @Override
    public boolean update(GameContext gameContext) {
        return true;
    }
}
