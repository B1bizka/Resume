package org.libin.game.world.levels;

import org.libin.game.entities.Sprite;
import org.libin.game.world.Level;
import org.libin.game.world.Texture;

public class Level1 extends Level {
    public Level1(int lvl) {
        super(lvl);
    }

    @Override
    protected void configure() {
        width  = 34;
        height = 34;
        loadMaps("/Maps/Map1/map1");

        startX    = 32.5;
        startY    = 16.5;
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

        exitCellX = 3;
        exitCellY = 18;

        sprites.add(new Sprite(Sprite.Type.DECORATION,  3.5, 18.5, 0, Texture.exit, 0));
        sprites.add(new Sprite(Sprite.Type.DECORATION,  6.5, 27.5, 0, Texture.chain, 0));

        sprites.add(new Sprite(Sprite.Type.ITEM,  29.5, 27.5, 20, Texture.item, 0));
        sprites.add(new Sprite(Sprite.Type.ITEM,  29.5, 26.5, 20, Texture.item, 0));
        sprites.add(new Sprite(Sprite.Type.ITEM,  29.5, 25.5, 20, Texture.item, 1));
        sprites.add(new Sprite(Sprite.Type.ITEM,  28.5, 27.5, 20, Texture.item, 1));
        sprites.add(new Sprite(Sprite.Type.ITEM,  28.5, 26.5, 20, Texture.item, 1));
        sprites.add(new Sprite(Sprite.Type.ITEM,  28.5, 25.5, 20, Texture.item, 2));

        Texture[] enemyWalkFrames = Texture.splitSpriteSheet("/Sprites/enemies/imp/walking.png", 3, 64);
        sprites.add(new Sprite(Sprite.Type.ENEMY,  2.5, 2.5, 20, enemyWalkFrames,Texture.imp_attack));
        sprites.add(new Sprite(Sprite.Type.ENEMY,  12.5, 7.5, 20, enemyWalkFrames,Texture.imp_attack));

       toListEntities();



    }
}
