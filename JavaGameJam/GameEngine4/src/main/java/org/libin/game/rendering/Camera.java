package org.libin.game.rendering;

import org.libin.game.audio.Sound;
import org.libin.game.systems.GameController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    public boolean left, right, forward, back;
    private static final double MOVE_SPEED   = 0.06;
    private static final double SIDE_SPEED   = 0.04;
    private static final double ROT_SPEED    = 0.06;
    private static final double COLLISION_OFFSET = 0.2;
    public static final double INTERACTION_DISTANCE = 0.6;


    public static final int mapWidth= 34, mapHeight =34;

    public int[][] map;

    public Camera(double x, double y, double xd, double yd, double xp, double yp, int[][] m) {
        xPos    = x;
        yPos    = y;
        xDir    = xd;
        yDir    = yd;
        xPlane  = xp;
        yPlane  = yp;
        map = m;
    }
    // key handler
    @Override
    public void keyPressed(KeyEvent key) { // handle interaction
        if (key.getKeyCode()  == KeyEvent.VK_E) {

            int playerCellX = (int) xPos;
            int playerCellY = (int) yPos;

            double distance = INTERACTION_DISTANCE;

            double frontX = xPos + xDir * distance;
            double frontY = yPos + yDir * distance;

            int cellX = (int) frontX;
            int cellY = (int) frontY;

            if (cellX < 0 || cellX >= mapWidth || cellY < 0 || cellY >= mapHeight) {
                return;
            }

            if (playerCellX == cellX && playerCellY == cellY) {
                return;
            }

            int tile = map[cellX][cellY];
            if (tile == 2) {
                map[cellX][cellY] = -1; // logic for doors
            } else if (tile == -1) {
                map[cellX][cellY] = 2;
            }
            //} else if (tile == 4) {
            //  map[cellX][cellY] = 0; // logic for "invisible" walls
            //}
        }
        switch (key.getKeyCode()) { // handle movement
            case KeyEvent.VK_A -> left  = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_W -> forward = true;
            case KeyEvent.VK_S -> back = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) { // handle movement
        switch (key.getKeyCode()) {
            case KeyEvent.VK_A -> left  = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_W -> forward = false;
            case KeyEvent.VK_S -> back = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void update(int[][] map) { //movement
        if (forward) { // movement forward and backward requires addition checks in order not to get through walls

            double newX = xPos + xDir * MOVE_SPEED; // i draw addition "point" in front of player to check collision
            int mapX = (int) (newX + xDir * COLLISION_OFFSET);
            int mapY = (int) yPos;
            if (mapX >= 0 && mapX < mapWidth && mapY >= 0 && mapY < mapHeight
                    && map[mapX][mapY] <= 0) {
                xPos = newX;
            }

            double newY = yPos + yDir * MOVE_SPEED;
            mapX = (int) xPos;
            mapY = (int) (newY + yDir * COLLISION_OFFSET);
            if (mapX >= 0 && mapX < mapWidth && mapY >= 0 && mapY < mapHeight
                    && map[mapX][mapY] <= 0) {
                yPos = newY;
            }
        }
        if (back) { // likewise for the backward movement
            double newX = xPos - xDir * MOVE_SPEED;
            int mapX = (int) (newX - xDir * COLLISION_OFFSET);
            int mapY = (int) yPos;
            if (mapX >= 0 && mapX < mapWidth && mapY >= 0 && mapY < mapHeight
                    && map[mapX][mapY] <= 0) {
                xPos = newX;
            }
            double newY = yPos - yDir * MOVE_SPEED;
            mapX = (int) xPos;
            mapY = (int) (newY - yDir * COLLISION_OFFSET);
            if (mapX >= 0 && mapX < mapWidth && mapY >= 0 && mapY < mapHeight
                    && map[mapX][mapY] <= 0) {
                yPos = newY;
            }
        }
        if (right) { // nothing special - just ordinary turning formula
            double oldDirX = xDir;
            xDir = xDir * Math.cos(-ROT_SPEED) - yDir * Math.sin(-ROT_SPEED);
            yDir = oldDirX * Math.sin(-ROT_SPEED) + yDir * Math.cos(-ROT_SPEED);
            double oldPlaneX = xPlane;
            xPlane = xPlane * Math.cos(-ROT_SPEED) - yPlane * Math.sin(-ROT_SPEED);
            yPlane = oldPlaneX * Math.sin(-ROT_SPEED) + yPlane * Math.cos(-ROT_SPEED);
        }
        if (left) {
            double oldDirX = xDir;
            xDir = xDir * Math.cos(ROT_SPEED) - yDir * Math.sin(ROT_SPEED);
            yDir = oldDirX * Math.sin(ROT_SPEED) + yDir * Math.cos(ROT_SPEED);
            double oldPlaneX = xPlane;
            xPlane = xPlane * Math.cos(ROT_SPEED) - yPlane * Math.sin(ROT_SPEED);
            yPlane = oldPlaneX * Math.sin(ROT_SPEED) + yPlane * Math.cos(ROT_SPEED);
        }

    }
}
