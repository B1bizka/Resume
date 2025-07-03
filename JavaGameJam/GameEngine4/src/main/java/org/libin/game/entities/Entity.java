package org.libin.game.entities;

import org.libin.game.systems.GameContext;

public abstract class Entity {
    public Sprite sprite;
    protected boolean active;

    public Entity(Sprite sprite){
        this.sprite = sprite;
        active = true;

    }
    public abstract boolean update(GameContext gameContext);

    public boolean isActive(){
        return sprite.isActive();
    }
    public void setActive(boolean bool){
        active = bool;
        sprite.setActive(bool);
    }

}
