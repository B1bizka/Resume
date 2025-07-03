package org.libin.game.entities;

import org.libin.game.systems.GameContext;

public class Enemy extends Entity{
    private Behavior behavior;


    public Enemy(Sprite sprite,Behavior initialBehavior) {
        super(sprite);
        this.behavior = initialBehavior;
    }

    @Override
    public boolean update(GameContext gameContext) {
        return behavior.update(this,gameContext);

    }
    public void changeBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}
