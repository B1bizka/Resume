package org.libin.game.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapLoader {
    public static int[][] loadMap(String resourcePath, int width, int height) {
        InputStream is = MapLoader.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("Не удалось найти ресурс: " + resourcePath);
        }
        try {
            return loadMapFromStream(is, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return new int[width][height];
        }
    }
    private static int[][] loadMapFromStream(InputStream is, int width, int height) throws IOException {
        int[][] map = new int[width][height];
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < height) {
                String[] tokens = line.trim().split("\\s+");
                for (int col = 0; col < tokens.length && col < width; col++) {
                    try {
                        map[row][col] = Integer.parseInt(tokens[col]);
                    } catch (NumberFormatException ex) {
                        map[col][row] = 0;
                    }
                }
                row++;
            }
        }
        return map;
    }
}

