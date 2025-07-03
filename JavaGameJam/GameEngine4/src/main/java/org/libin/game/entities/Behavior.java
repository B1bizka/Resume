package org.libin.game.entities;

import org.libin.game.systems.GameContext;

public interface Behavior {
    boolean update(Entity entity, GameContext gameContext);
}
