package org.libin.game.world;

import lombok.Getter;
import org.libin.game.entities.*;
import org.libin.game.entities.behaviors.ChaseAndAttack;
import org.libin.game.inventory.InventoryItem;
import org.libin.game.inventory.InventoryItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class Level {
    protected int levelNumber, width, height;
    protected int[][] map, floorMap, ceilingMap;
    protected double startX, startY, startDirX, startDirY, planeX, planeY;
    protected int exitCellX, exitCellY;
    protected List<Entity> entities = new ArrayList<>();
    protected List<Texture> textures = new ArrayList<>();
    protected List<Sprite>  sprites  = new ArrayList<>();
    protected List<InventoryItem> droppedItems = new ArrayList<>();

    public Level(int lvl) {
        this.levelNumber = lvl;
        configure();
    }
    public Level(int lvl,List<InventoryItem> droppedItems ) {
        this.levelNumber = lvl;
        this.droppedItems = droppedItems;
        configure();
    }

    protected abstract void configure();

    protected void loadMaps(String basePath) {
        map        = MapLoader.loadMap(basePath + ".txt",  width, height);
        floorMap   = MapLoader.loadMap(basePath + "f.txt", width, height);
        ceilingMap = MapLoader.loadMap(basePath + "c.txt", width, height);
    }

    public boolean isExit(double camX, double camY) {
        int cellX = (int) camX;
        int cellY = (int) camY;
        return (cellX == exitCellX && cellY == exitCellY);
    }

    protected void toListEntities(){
        entities = sprites.stream().map(sp -> {
            if (sp.getType() == Sprite.Type.ITEM) {
                return new Item(sp,InventoryItemsRepository.getFromRep(sp.getItemId()));
            } else if(sp.getType() == Sprite.Type.ENEMY){
                return new Enemy(sp, new ChaseAndAttack());
            } else{
                return new Decoration(sp);
            }
        }).collect(Collectors.toList());
    }


}
