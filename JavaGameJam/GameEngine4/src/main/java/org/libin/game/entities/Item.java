package org.libin.game.entities;

import org.libin.game.inventory.InventoryItem;
import org.libin.game.inventory.InventoryView;
import org.libin.game.systems.GameContext;


public class Item extends Entity{
    private int itemId;
    private final double pickRadius = 0.5;
    private InventoryItem item;


    public Item(Sprite sprite, InventoryItem item) {
        super(sprite);
        this.item = item;
    }

    @Override
    public boolean update(GameContext gameContext) {
        double dx = gameContext.camera.xPos - sprite.getX();
        double dy = gameContext.camera.yPos - sprite.getY();
        double dist = Math.hypot(dx, dy);

        if (dist < pickRadius && !InventoryView.isDragging()) {
            //Todo
            boolean added = gameContext.inventory.addItemToInv(item);
            if (added) {
                setActive(false);
            }
        }
        return true;
    }

}
