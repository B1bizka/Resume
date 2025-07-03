package org.libin.game.world;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Texture {
    public final int SIZE;
    public final int[] pixels;

    public Texture(String location, int size) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        loadFromFile(location);
    }

    private void loadFromFile(String resourcePath) {
        try {
            URL imgURL = getClass().getResource(resourcePath);
            if (imgURL == null) {
                throw new RuntimeException("Не удалось найти ресурс: " + resourcePath);
            }
            BufferedImage image = ImageIO.read(imgURL);
            if (image == null) {
                throw new RuntimeException("Не удалось загрузить изображение: " + resourcePath);
            }

            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Todo for walking enemies loader

    public Texture(BufferedImage image, int size) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        int w = image.getWidth();
        int h = image.getHeight();
        image.getRGB(0, 0, w, h, pixels, 0, w);
    }
    public static Texture[] splitSpriteSheet(String location, int frameCount, int frameSize) {
        Texture[] frames = new Texture[frameCount];
        try {
            java.net.URL imgURL = Texture.class.getResource(location);
            BufferedImage spriteSheet = ImageIO.read(imgURL);
            for (int i = 0; i < frameCount; i++) {
                BufferedImage frame = spriteSheet.getSubimage(i * frameSize, 0, frameSize, frameSize);
                Texture tex = new Texture(frame, frameSize);
                frames[i] = tex;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frames;
    }



    public static final Texture floor1      = new Texture("/Textures/floor1/floor1floor.PNG",     64);
    public static final Texture stone1      = new Texture("/Textures/floor1/floor1stone.PNG",     64);
    public static final Texture wall1      = new Texture("/Textures/floor1/floor1wall.PNG",     64);
    public static final Texture ceiling1      = new Texture("/Textures/floor1/floor1ceiling.PNG",     64);
    public static final Texture entrance = new Texture("/Textures/floor1/entrance.PNG",     64);

    public static final Texture floor2      = new Texture("/Textures/floor2/floor2floor.PNG",64);
    public static final Texture stone2      = new Texture("/Textures/floor2/floor2stone.PNG",64);
    public static final Texture wall2      = new Texture("/Textures/floor2/floor2wall.PNG",64);
    public static final Texture ceiling2      = new Texture("/Textures/floor2/floor2corrupt.PNG",64);

    public static final Texture floor3      = new Texture("/Textures/floor3/floor3corrupt.PNG",     64);
    public static final Texture stone3      = new Texture("/Textures/floor3/floor3stone.PNG",     64);
    public static final Texture wall3      = new Texture("/Textures/floor3/floor3wall.PNG",     64);
    public static final Texture ceiling3      = new Texture("/Textures/floor3/floor3corrupt.PNG",     64);

    //Todo
    public static final Texture door      = new Texture("/Textures/doors/door1.PNG",      64);
    public static final Texture door_floor     = new Texture("/Textures/doors/door.PNG",      64);
    public static final Texture exit      = new Texture("/Sprites/decoration/exit.png",      64);
    public static final Texture chain      = new Texture("/Sprites/decoration/chain.png",      64);
    public static final Texture yellow_exit      = new Texture("/Textures/doors/exit.PNG",      64);


    public static final Texture imp_attack   = new Texture("/Sprites/enemies/imp/attack.PNG",   64);
    public static final Texture item   = new Texture("/Sprites/items/item.PNG",   64);


}
