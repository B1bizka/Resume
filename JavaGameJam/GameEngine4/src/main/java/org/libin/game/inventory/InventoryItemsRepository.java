package org.libin.game.inventory;

import org.libin.game.world.Texture;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemsRepository {
    private final static List<InventoryItem> ItemRep = new ArrayList<>();

    public static void addToRep(InventoryItem item){
        ItemRep.add(item);

    }
    public static InventoryItem getFromRep(int i){
        InventoryItem tmp = ItemRep.get(i);
        return new InventoryItem(tmp.getItemId(),tmp.getShape(),tmp.getImage());

    }

    public static final InventoryItem key = new InventoryItem(0,new boolean[][]{
            { true }}, "/Sprites/inventoryItems/key.png");

    public static final InventoryItem potion = new InventoryItem(1,new boolean[][]{
            { true, true },
            { true, true }}, "/Sprites/inventoryItems/potion.png");

    public static final InventoryItem sword = new InventoryItem(2,new boolean[][]{
            { true, true, true },
            { true, true, true}
    }, "/Sprites/inventoryItems/sword.png");

    static {
        addToRep(key);
        addToRep(potion);
        addToRep(sword);
    }
}
