package org.libin.game.entities;

import lombok.Getter;
import lombok.Setter;
import org.libin.game.world.Texture;

public class Sprite {
    public enum Type { ITEM, ENEMY, DECORATION }
    public enum State { WALK, ATTACK }
    @Getter
    private State state = State.WALK;
    @Getter
    private Type type;
    @Getter
    private int itemId;
    @Getter @Setter
    private int attackTimer = 0;

    private Texture[] Frames;
    private Texture attackFrame;
    public final Texture baseTexture;

    @Getter @Setter
    private int animFrame = 0;
    @Getter @Setter
    private int animTick  = 0;
    @Getter @Setter
    private int animMaxTick = 8;


    @Getter @Setter
    private double x,y,z;
    @Getter @Setter
    private boolean active = true;

    //Todo constructor for basic spites ( items )

    public Sprite(Type type, double x, double y, double z, Texture texture,int itemId) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.baseTexture = texture;
        this.itemId = itemId;
    }



    public Sprite(Type type,double x, double y, double z, Texture[] frames, Texture attackFrame) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.Frames   = frames;
        this.baseTexture = (Frames != null && Frames.length > 0) ? Frames[0] : null;
        this.attackFrame  = attackFrame;

    }

    public Texture getCurrentTexture() {
        if (state == State.ATTACK && attackFrame != null) {
            return attackFrame;
        }
        if (Frames != null && Frames.length > 0) {
            return Frames[animFrame];
        }
        return baseTexture;
    }

    public void nextAnimFrame() {
        if (Frames != null && Frames.length > 0) {
            animFrame = (animFrame + 1) % Frames.length;
        }
    }

    public void resetAnim() {
        animFrame = 0;
        animTick  = 0;
    }


    public void setState(State s) {
        state = s;
        if (s == State.WALK) {
            animFrame = 0;
            animTick = 0;
        }
    }

}
