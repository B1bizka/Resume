package org.libin.game.world;

import org.libin.game.inventory.InventoryItem;
import org.libin.game.systems.GameController;
import org.libin.game.world.levels.Level1;
import org.libin.game.world.levels.Level2;

import java.util.List;

public class LevelFactory {
    public static Level create(int lvl) {
        return switch (lvl) {
            case 1 -> new Level1(1);
            case 2 -> new Level2(2, GameController.droppedItems);
            //case 3: return new Level3();
            default -> new Level1(1);
        };
    }
}
