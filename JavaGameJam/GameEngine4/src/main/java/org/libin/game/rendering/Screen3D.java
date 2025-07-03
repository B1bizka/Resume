package org.libin.game.rendering;

import lombok.Getter;
import org.libin.game.world.Texture;

import java.awt.*;
import java.util.List;

public class Screen3D {
    public int[][] map;
    public int[][] floorMap;
    public int[][] ceilingMap;
    public int mapWidth, mapHeight, width, height;
    public List<Texture> textures;
    @Getter
    private double[] depthBuffer;


    public Screen3D(int[][] m, int mapW, int mapH, List<Texture> tex, int w, int h, int[][] mf, int[][] mc) {
        map       = m;
        mapWidth  = mapW;
        mapHeight = mapH;
        textures  = tex;
        width     = w;  // 500
        height    = h;  // 500
        floorMap = mf;
        ceilingMap = mc;
    }


    public int[] update(Camera camera, int[] pixels) {

        if (depthBuffer == null || depthBuffer.length != width) {
            depthBuffer = new double[width];
        }

        // make default sky and floor
        for (int i = 0; i < height / 2; i++) {
            int rowOffset = i * width;
            for (int x = 0; x < width; x++) {
                pixels[rowOffset + x] = Color.CYAN.getRGB(); // sky
            }
        }
        for (int i = height / 2; i < height; i++) {
            int rowOffset = i * width;
            for (int x = 0; x < width; x++) {
                pixels[rowOffset + x] = Color.BLUE.getRGB(); // floor
            }
        }

        // Ray-casting each ray
        for (int x = 0; x < width; x++) {
            double cameraX  = 2 * x / (double) width - 1;
            double rayDirX  = camera.xDir + camera.xPlane * cameraX;
            double rayDirY  = camera.yDir + camera.yPlane * cameraX;

            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;

            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double sideDistX, sideDistY;

            int stepX, stepY;
            int side = 0; // 0 - vertical wall, 1 - horizontal

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }

            boolean hit = false;
            double perpWallDist = 0;

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                // out of bounds
                if (mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight) {
                    perpWallDist = Double.POSITIVE_INFINITY;
                    hit = true;
                    break;
                }

                if (map[mapX][mapY] > 0) {
                    // if we hit da wall - calc distance towards it
                    if (side == 0) {
                        perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2.0) / rayDirX);
                    } else {
                        perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2.0) / rayDirY);
                    }
                    hit = true;
                }
            }

            // skip drawing inf rays
            if (perpWallDist == Double.POSITIVE_INFINITY) {
                continue;
            }


            depthBuffer[x] = perpWallDist; //spite magic


            // wall height
            int lineH = (int) (height / perpWallDist);

            //if (lineH > height) lineH = height;
            // my mistake for future generations to giggle ( makes funny warping effect)


            // pixel draw cord
            int drawStart = -lineH / 2 + height / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineH / 2 + height / 2;
            if (drawEnd >= height) drawEnd = height - 1;

            // texture
            int texNum = map[mapX][mapY] - 1;
            if (texNum < 0) texNum = 0;
            if (texNum >= textures.size()) texNum = textures.size() - 1;
            Texture tex = textures.get(texNum);
            int texSize = tex.SIZE;


            double wallX;
            if (side == 0) {
                wallX = camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2.0) / rayDirX) * rayDirY;
            } else {
                wallX = camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2.0) / rayDirY) * rayDirX;
            }
            wallX -= Math.floor(wallX);
            if (wallX < 0) wallX = 0;
            if (wallX >= 1) wallX = 0.999999;

            int texX = (int) (wallX * texSize);
            if (texX < 0) texX = 0;
            if (texX >= texSize) texX = texSize - 1;


            for (int y = drawStart; y <= drawEnd; y++) {
                int d = y * 256 - height * 128 + lineH * 128; // 256 = 2*128
                int texY = ((d * texSize) / lineH) / 256;
                if (texY < 0) texY = 0;
                if (texY >= texSize) texY = texSize - 1;

                int rawColor = tex.pixels[texX + texY * texSize];

                // skip white pixels
                int r = (rawColor >> 16) & 0xFF;
                int g = (rawColor >>  8) & 0xFF;
                int b = (rawColor      ) & 0xFF;

                if (r > 200 && g > 200 && b > 200) {
                    continue;
                }

                int color = rawColor;
                if (side == 1) {
                    color = (color >> 1) & 0x7F7F7F; // horizontal walls shading
                }
                color = applyFog(color, perpWallDist);
                pixels[x + y * width] = color;
            }


            // floor and ceiling

            double posX   = camera.xPos;
            double posY   = camera.yPos;
            double dirX   = camera.xDir;
            double dirY   = camera.yDir;
            double planeX = camera.xPlane;
            double planeY = camera.yPlane;
            double halfH = height / 2.0;

            for (int y = drawEnd + 1; y < height; y++) {
                double row = y - halfH;
                double perpDistFloor = halfH / row;

                double rayDirFX = dirX + planeX * cameraX;
                double rayDirFY = dirY + planeY * cameraX;

                double floorX = posX + rayDirFX * perpDistFloor;
                double floorY = posY + rayDirFY * perpDistFloor;

                int cellX = (int) Math.floor(floorX);
                int cellY = (int) Math.floor(floorY);
                if (cellX < 0)            cellX = 0;
                else if (cellX >= mapWidth)  cellX = mapWidth - 1;
                if (cellY < 0)            cellY = 0;
                else if (cellY >= mapHeight) cellY = mapHeight - 1;


                int local = floorMap[cellX][cellY];
                if (local <= 0) {
                    continue;
                }

                int globalFloorIdx = local - 1;
                if (globalFloorIdx < 0) globalFloorIdx = 0;
                else if (globalFloorIdx >= textures.size())
                    globalFloorIdx = textures.size() - 1;

                Texture floorTex = textures.get(globalFloorIdx);
                int ftSize = floorTex.SIZE;

                int txF = (int) ((floorX - cellX) * ftSize);
                int tyF = (int) ((floorY - cellY) * ftSize);
                if (txF < 0)      txF = 0;
                else if (txF >= ftSize) txF = ftSize - 1;
                if (tyF < 0)      tyF = 0;
                else if (tyF >= ftSize) tyF = ftSize - 1;

                double floorDist = Math.hypot(floorX - camera.xPos, floorY - camera.yPos);
                int floorColor = applyFog(floorTex.pixels[txF + tyF * ftSize], floorDist);
                pixels[x + y * width] = floorColor;


                // ceiling

                int ceilingY = height - y ;
                int localCeil = ceilingMap[cellX][cellY];
                if (localCeil > 0) {
                    int globalCeilIdx = localCeil - 1;
                    if (globalCeilIdx < 0) globalCeilIdx = 0;
                    else if (globalCeilIdx >= textures.size())
                        globalCeilIdx = textures.size() - 1;

                    Texture ceilTex = textures.get(globalCeilIdx);
                    int ctSize = ceilTex.SIZE;

                    int txC = (int) ((floorX - cellX) * ctSize);
                    int tyC = (int) ((floorY - cellY) * ctSize);
                    if (txC < 0) txC = 0;
                    else if (txC >= ctSize) txC = ctSize - 1;
                    if (tyC < 0) tyC = 0;
                    else if (tyC >= ctSize) tyC = ctSize - 1;

                    int ceilColor = applyFog(ceilTex.pixels[txC + tyC * ctSize], floorDist);
                    pixels[x + ceilingY * width] = ceilColor;
                }
            }
        }
        return pixels;
    }

    public static double FixAng(double a) {
        a %= 360.0;
        if (a < 0) a += 360.0;
        return a;
    }

    public static int applyFog(int rgb, double dist) {
        final double maxDist = 8.0;
        double fogFactor = 1.0 - Math.min(dist / maxDist, 1.0);

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        r = (int)(r * fogFactor);
        g = (int)(g * fogFactor);
        b = (int)(b * fogFactor);

        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
