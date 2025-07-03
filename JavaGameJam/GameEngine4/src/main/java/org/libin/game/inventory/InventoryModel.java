package org.libin.game.inventory;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InventoryModel {
    private final int rows, cols;
    private final boolean[][] occupied;
    @Getter
    private final List<InventoryItem> items = new ArrayList<>();

    @Getter
    private final int row = 5;
    @Getter
    private final int col = 3;

    public InventoryModel(){
        this.rows = row;
        this.cols = col;
        this.occupied = new boolean[rows][cols];
    }

    public boolean canBePlaced(int row, int col, InventoryItem item){
        int spanRow = item.getSpanRow();
        int spanCol = item.getSpanCol();
        boolean [][] shape  = item.getShape();
        if(row < 0 || col < 0
                || row + spanRow > rows
                || col + spanCol > cols){
            return false;
        }

        for (int i = 0; i < spanRow; i++) {
            for (int j = 0; j < spanCol; j++) {
                if (shape[i][j] && occupied[row + i][col + j]) {
                    return false;
                }
            }
        }
        return true;

    }
    public boolean addItemToInv(InventoryItem item){
        boolean[][] itemShape = item.getShape();
        int spanRow = item.getSpanRow();
        int spanCol = item.getSpanCol();
        if (itemShape == null || item.getImage() == null) return false;

        for( int r=0; r + spanRow <= rows;r++){
            for(int c=0; c+ spanCol <= cols; c++){
                if(!canBePlaced(r,c,item)){
                    continue;
                }
                item.setPosition(r,c);
                items.add(item);
                occupy(r,c,item,true);
                return true;
            }
        }
        return false;

    }

    private void occupy(int row, int col,InventoryItem item, boolean bool ){
        boolean[][] itemShape = item.getShape();
        int spanRow = item.getSpanRow();
        int spanCol = item.getSpanCol();
        for (int i=0; i<spanRow;i++){
            for (int j=0;j<spanCol;j++){
                occupied[row+i][col+j] = bool;
            }
        }
    }

    public boolean removeItemFromIvn(InventoryItem item){
        int spanRow = item.getSpanRow();
        int spanCol = item.getSpanCol();
        if (!items.contains(item)) {
            return false;
        }
        occupy(item.getRow(), item.getCol(), item, false);
        items.remove(item);
        return true;

    }

    public boolean placeItem(InventoryItem item, int row, int col) {
        if (!canBePlaced(row, col, item)) return false;
        item.setPosition(row, col);
        items.add(item);
        occupy(row, col, item, true);
        return true;
    }

    public void clear(){
        items.clear();
        for(int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                occupied[i][j] = false;
            }
        }
    }
}
