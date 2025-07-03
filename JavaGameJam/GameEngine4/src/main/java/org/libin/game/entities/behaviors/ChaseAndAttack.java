package org.libin.game.entities.behaviors;

import lombok.Getter;
import lombok.Setter;
import org.libin.game.entities.Behavior;
import org.libin.game.entities.Entity;
import org.libin.game.entities.Sprite;
import org.libin.game.rendering.Camera;
import org.libin.game.systems.GameContext;
import org.libin.game.world.Level;

public class ChaseAndAttack implements Behavior {

    private final double Speed = 0.03;

    @Override
    public boolean update(Entity self, GameContext ctx) {

        Sprite sp = self.sprite;
        double dx = ctx.camera.xPos - sp.getX();
        double dy = ctx.camera.yPos - sp.getY();
        double dist = Math.hypot(dx, dy);
        Level currentLevel = ctx.level;
        Camera camera = ctx.camera;
        int HP = ctx.playerHp;
        int invulnerability = ctx.playerInvulnTimer;

        if (sp.getAttackTimer() > 0 && sp.getState() != Sprite.State.ATTACK) {
            sp.setAttackTimer(sp.getAttackTimer() - 1);
        }

        if (sp.getState() == Sprite.State.ATTACK) {
            sp.setAttackTimer(sp.getAttackTimer() - 1);
            if (sp.getAttackTimer() <= 0) {
                double curLen = Math.hypot(camera.xPos - sp.getX(), camera.yPos - sp.getY());
                if (curLen < 1.2 && invulnerability == 0) {
                    HP--;
                    if(HP<=0){
                        return false;
                    }
                    invulnerability = 60;
                    ctx.playerHp = HP;
                    ctx.playerInvulnTimer = invulnerability;
                }
                sp.resetAnim();
                sp.setState(Sprite.State.WALK);
                sp.setAttackTimer(30);
            }
            return true;
        }

        if (sp.getAttackTimer() > 0) {
            return true;
        }

        if (dist < 1 && canSeePlayer(sp, camera, currentLevel.getMap())) {
            sp.setState(Sprite.State.ATTACK);
            sp.setAttackTimer(30);
            return true;
        }

        if (!canSeePlayer(sp, camera, currentLevel.getMap())) {
            sp.resetAnim();
            return true;
        }

        double oldX = sp.getX();
        double oldY = sp.getY();

        double dx1 = camera.xPos - sp.getX();
        double dy1 = camera.yPos - sp.getY();
        double length = Math.hypot(dx1, dy1);
        if (length == 0) return true;

        double speed = Speed;

        double nx = sp.getX() + (dx1 / length) * speed;
        int cx = (int)(nx + Math.signum(dx1) * 0.2), cy = (int)sp.getY();
        if (cx >= 0 && cx < currentLevel.getWidth()
                && cy >= 0 && cy < currentLevel.getHeight()
                && currentLevel.getMap()[cx][cy] <= 0) {
            sp.setX(nx);
        }
        double ny = sp.getY() + (dy1 / length) * speed;
        cx = (int)sp.getX(); cy = (int)(ny + Math.signum(dy1) * 0.2);
        if (cx >= 0 && cx < currentLevel.getWidth()
                && cy >= 0 && cy < currentLevel.getHeight()
                && currentLevel.getMap()[cx][cy] <= 0) {
            sp.setY(ny);
        }

        boolean moved = (oldX != sp.getX()) || (oldY != sp.getY());
        if (moved) {
            sp.setAnimTick(sp.getAnimTick() + 1);
            if (sp.getAnimTick() >= sp.getAnimMaxTick()) {
                sp.setAnimTick(0);
                sp.nextAnimFrame();
            }
        } else {
            sp.resetAnim();
        }
        return true;
    }

    private boolean canSeePlayer(Sprite sp, Camera camera, int[][] map) {
        int x0 = (int)sp.getX();
        int y0 = (int)sp.getY();
        int x1 = (int)camera.xPos;
        int y1 = (int)camera.yPos;

        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2;

        while (true) {
            if (map[x0][y0] > 0) return false; //wall
            if (x0 == x1 && y0 == y1) break; // got da player
            e2 = 2 * err;
            if (e2 >= dy) { err += dy; x0 += sx; }
            if (e2 <= dx) { err += dx; y0 += sy; }
        }
        return true;
    }
}
