package org.libin.game.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InventoryItem {
    private int row, col;
    private int spanRow, spanCol;
    private final boolean[][] shape;
    private final Image image;
    private final int itemId;
    // toDo
    //public final List<InventoryItem> ItemRepository = new ArrayList<>();

    public InventoryItem(int id, boolean[][] shape, String location){
        itemId = id;
        this.shape = shape;
        this.image = loadItemImage(location);
        spanRow = shape.length;
        spanCol = shape[0].length;
    }

    public InventoryItem(int id, boolean[][] shape, Image image){
        itemId = id;
        this.shape = shape;
        this.image = image;
        spanRow = shape.length;
        spanCol = shape[0].length;
    }

    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;

    }

    public Image loadItemImage(String resourcePath){
        try {
            return ImageIO.read(getClass().getResource(resourcePath));
        }catch (IOException e) {
            throw new RuntimeException("Не удалось найти ресурс: " + resourcePath);
        }
    }
    // Todo


    // Todo перенести в отдельный класс



}
