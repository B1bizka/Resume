package org.libin.game.world.levels;

import org.libin.game.entities.Sprite;
import org.libin.game.inventory.InventoryItem;
import org.libin.game.world.Level;
import org.libin.game.world.Texture;

import java.util.List;

public class Level2 extends Level {
    public Level2(int lvl, List<InventoryItem> items) {
        super(lvl,items);
    }

    @Override
    protected void configure() {
        width  = 34;
        height = 34;
        loadMaps("/Maps/Map2/map2");

        startX    = 32.5;
        startY    = 15.5;
        startDirX = -1.0;
        startDirY = 0.0;
        planeX    = 0.0;
        planeY    = 0.66;

        textures.add(Texture.door_floor);
        textures.add(Texture.door);
        textures.add(Texture.stone1);
        textures.add(Texture.wall1);
        textures.add(Texture.ceiling1);
        textures.add(Texture.floor1);

        textures.add(Texture.stone2);
        textures.add(Texture.wall2);
        textures.add(Texture.ceiling2);
        textures.add(Texture.floor2);

        textures.add(Texture.stone3);
        textures.add(Texture.wall3);
        textures.add(Texture.ceiling3);
        textures.add(Texture.floor3);

        textures.add(Texture.entrance);
        textures.add(Texture.yellow_exit);

        exitCellX = 6;
        exitCellY = 12;

        sprites.add(new Sprite(Sprite.Type.DECORATION,  15.5, 14.5, 0, Texture.chain, 0));
        sprites.add(new Sprite(Sprite.Type.DECORATION,  6.5, 12.5, 0, Texture.exit, 0));

        for (InventoryItem i : droppedItems){
           sprites.add(new Sprite(Sprite.Type.ITEM,  16.5, 16.5, 20, Texture.item, i.getItemId()));
        }
        toListEntities();

    }
}
