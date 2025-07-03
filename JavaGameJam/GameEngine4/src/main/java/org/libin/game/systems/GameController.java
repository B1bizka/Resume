package org.libin.game.systems;

import lombok.Getter;
import org.libin.game.audio.Sound;
import org.libin.game.entities.Entity;
import org.libin.game.entities.Sprite;
import org.libin.game.inventory.InventoryItem;
import org.libin.game.inventory.InventoryModel;
import org.libin.game.rendering.Camera;
import org.libin.game.rendering.Screen3D;
import org.libin.game.world.Level;
import org.libin.game.world.LevelFactory;
import org.libin.game.world.Texture;
import org.libin.ui.panels.GameScreenPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.libin.game.rendering.Screen3D.applyFog;

public class GameController  {
    @Getter
    private final GameScreenPanel gameScreenPanel;

    private Thread gameThread;
    private volatile boolean running = false;
    public static Sound sound;

    private boolean walkingPrev = false;
    public static final List<InventoryItem> droppedItems = new ArrayList<>();


    @Getter
    public static Camera camera;
    public static Camera sellCamera(){
        return camera;
    }
    private Screen3D screen3D;

    private final int SCREEN_W = 500,SCREEN_H = 500;
    private int currentLevelNumber ;
    private Level currentLevel;

    private GameContext ctx;
    private List<Entity> entities;

    @Getter
    private int HP;
    private int playerInvulnTimer = 0;

    @Getter
    private final InventoryModel inventoryModel = new InventoryModel();

    public GameController(GameScreenPanel gamePanel) {
        this.gameScreenPanel = gamePanel;

    }
    public void initLevel(Level lvl) {
        camera = new Camera(lvl.getStartX(), lvl.getStartY(),
                lvl.getStartDirX(), lvl.getStartDirY(),
                lvl.getPlaneX(), lvl.getPlaneY(), lvl.getMap());
        screen3D = new Screen3D(
                lvl.getMap(), lvl.getWidth(), lvl.getHeight(),
                lvl.getTextures(), SCREEN_W, SCREEN_H, lvl.getFloorMap(), lvl.getCeilingMap()
        );
        HP=4;
        ctx = new GameContext(camera, currentLevel, HP, playerInvulnTimer, inventoryModel );
        sound = new Sound();
        entities = lvl.getEntities();
    }

    //TOdo
    public void startLevel(int i){

        currentLevelNumber = i;
        currentLevel =  LevelFactory.create(currentLevelNumber);
        initLevel(currentLevel);
        playMusic(0);
        //startGameLoop();
        if (!running) {
            startGameLoop();
        }

    }


    public void startGameLoop() {
        //System.out.println("[DEBUG] startGameLoop(): before start, active loops = "
          //      + countGameLoopThreads());
        if (running) return;
        running = true;
        final double TICKS_PER_SEC = 60.0;
        final long   NS_PER_TICK   = (long)(1_000_000_000 / TICKS_PER_SEC);

        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            while (running) {
                long now = System.nanoTime();
                if (now - lastTime >= NS_PER_TICK) {
                    updateLogic();
                    SwingUtilities.invokeLater(gameScreenPanel::repaint);
                    lastTime += NS_PER_TICK;
                } else {
                    try { Thread.sleep(1); }
                    catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            }
        }, "GameLoopThread");

        gameThread.setDaemon(true);
        gameThread.start();
        //System.out.println("[DEBUG] startGameLoop(): after start, active loops = "
          //      + countGameLoopThreads());
    }

    public void stopGameLoop() {
        //System.out.println("[DEBUG] stopGameLoop(): before stop, active loops = "
          //      + countGameLoopThreads());
        running = false;
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        if (sound != null) {
            sound.stopMusic();
            sound.stopEffect();
        }
        //System.out.println("[DEBUG] stopGameLoop(): after stop, active loops = "
          //      + countGameLoopThreads());

    }
    private long countGameLoopThreads() {
        return Thread.getAllStackTraces().keySet().stream()
                .filter(t -> "GameLoopThread".equals(t.getName()) && t.isAlive())
                .count();
    }


    private void updateLogic() {
        if (ctx.playerInvulnTimer > 0) {
            ctx.playerInvulnTimer--;
        }

        HP  = ctx.playerHp;
        camera.update(currentLevel.getMap());

        boolean walking = camera.forward || camera.back;
        if (walking && !walkingPrev) {
            sound.setEffect(1);
            sound.playEffectLoop();
        } else if (!walking && walkingPrev) {
            sound.stopEffect();
        }
        walkingPrev = walking;

        for ( Entity e: entities){
            if(e.isActive())
                if(!e.update(ctx)){
                    inventoryModel.clear();
                    stopGameLoop();
                    //stopMusic();
                    gameScreenPanel.toMainPanel();
                };
        }

        if (currentLevel.isExit(camera.xPos, camera.yPos)) {
            //stopGameLoop();
            stopMusic();
            //currentLevelNumber++;
            //startLevel(currentLevelNumber);
            if(currentLevelNumber == 1){
                gameScreenPanel.startNewLevel(2);
            }else {
                droppedItems.clear();
                stopGameLoop();
                gameScreenPanel.toEnd();
            }

        }



    }

    public void renderToBuffer(BufferedImage buffer) {
        int[] pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
        screen3D.update(camera, pixels);

        entities.stream()
                .filter(Entity::isActive)
                .sorted(Comparator.comparingDouble(e -> -distanceToCamera(e)))
                .forEach(e -> renderSpites(pixels,e.sprite,screen3D.getDepthBuffer()));

    }

    public double distanceToCamera(Entity e) {
        double dx = e.sprite.getX() - camera.xPos;
        double dy = e.sprite.getY() - camera.yPos;
        return Math.hypot(dx, dy);
    }


    public void renderSpites(int[] pixels, Sprite sp,double[] depth) {
        double dx = sp.getX() - camera.xPos;
        double dy = sp.getY() - camera.yPos;
        double invDet = 1.0 / (camera.xPlane * camera.yDir - camera.xDir * camera.yPlane);
        double transformX = invDet * (camera.yDir * dx - camera.xDir * dy);
        double transformY = invDet * (-camera.yPlane * dx + camera.xPlane * dy);

        if (transformY <= 0.1) return;

        int spriteScreenX = (int) ((SCREEN_W / 2.0) * (1 + transformX / transformY));
        int spriteH = Math.abs((int) (SCREEN_H / transformY));
        int spriteW = spriteH;
        int startY = -spriteH / 2 + SCREEN_H / 2 + (int) sp.getZ();
        int endY = startY + spriteH;
        int startX = spriteScreenX - spriteW / 2;
        int endX = startX + spriteW;


        spriteScreenX = (int) ((SCREEN_W / 2.0) * (1 + transformX / transformY));
        spriteH = Math.abs((int) (SCREEN_H / transformY));
        spriteW = spriteH;

        for (int stripe = startX; stripe < endX; stripe++) {
            if (stripe < 0 || stripe >= SCREEN_W) continue;
            if (transformY >= depth[stripe]) continue;

            int texX = (int) ((stripe - startX) * sp.baseTexture.SIZE / (double) spriteW);

            for (int y = startY; y < endY; y++) {
                if (y < 0 || y >= SCREEN_H) continue;
                int d = y - startY;
                int texY = (int) (d * sp.baseTexture.SIZE / (double) spriteH);


                Texture currentTexture = sp.getCurrentTexture();
                int color1 = currentTexture.pixels[texX + texY * currentTexture.SIZE];


                int rgb = color1 & 0x00FFFFFF;
                int r = (color1 >> 16) & 0xFF;
                int g = (color1 >> 8) & 0xFF;
                int b = (color1) & 0xFF;
                if (r > 90 && g > 90 && b > 90) continue;

                int foggedColor = applyFog(color1, transformY);
                pixels[stripe + y * SCREEN_W] = foggedColor;
            }
        }
    }

    private void playMusic(int i){
        sound.setMusic(i);
        sound.playMusicLoop();
    }

    private void stopMusic(){
        sound.stopMusic();
        sound.stopEffect();
    }


}
