package org.libin.game.systems;

import lombok.Getter;
import org.libin.game.inventory.InventoryModel;
import org.libin.game.rendering.Camera;
import org.libin.game.world.Level;
import org.libin.ui.panels.GameAreaPanel;

@Getter
public class GameContext {
    public final Camera camera;
    public final Level level;
    public final InventoryModel inventory;
    public int playerHp;
    public int playerInvulnTimer = 0;


    //Todo inventory
    public GameContext(Camera cam, Level lvl, int hp, int timer, InventoryModel inventoryModel) {
        this.camera = cam;
        this.level  = lvl;
        this.inventory = inventoryModel;
        this.playerHp = hp;
        this.playerInvulnTimer = timer;
    }
}
