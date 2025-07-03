package org.libin.game.inventory;

import lombok.Getter;
import org.libin.game.rendering.Camera;

import java.awt.*;
import java.util.List;

public class InventoryView {
    private final InventoryModel model;
    private final Image background;
    private final int invRows, invCols;

    @Getter
    private  int selRow = 0, selCol = 0;

    @Getter
    private InventoryItem dragItem = null;
    @Getter
    public static boolean dragging = false;

    public InventoryView(InventoryModel model, Image background) {
        this.model = model;
        this.background = background;
        this.invRows = model.getRow();
        this.invCols = model.getCol();
    }

    public void moveSelectionLeft() {
        selCol = Math.max(0, selCol - 1);
    }

    public void moveSelectionRight() {
        int maxCol = dragging && dragItem != null
                ? invCols - dragItem.getSpanCol()
                : invCols - 1;
        selCol = Math.min(maxCol, selCol + 1);
    }

    public void moveSelectionUp() {
        selRow = Math.max(0, selRow - 1);
    }

    public void moveSelectionDown() {
        int maxRow = dragging && dragItem != null
                ? invRows - dragItem.getSpanRow()
                : invRows - 1;
        selRow = Math.min(maxRow, selRow + 1);
    }

    public void setSelection(int row, int col) {
        if (row >= 0 && row < invRows && col >= 0 && col < invCols) {
            selRow = row;
            selCol = col;
        }
    }


    public void startDrag(InventoryItem item) {
        this.dragItem = item;
        this.dragging = true;
        this.selRow = item.getRow();
        this.selCol = item.getCol();
    }

    public void dropDrag() {
        this.dragItem = null;
        this.dragging = false;
    }

    public void cancelDrag() {
        this.dragItem = null;
        this.dragging = false;
    }

    public void paint(Graphics2D g, int offX, int offY, int width, int height) {
        if (background != null) {
            g.drawImage(background, offX, offY, width, height, null);
        }

        int cellW = width  / invCols;
        int cellH = height / invRows;


        List<InventoryItem> items = model.getItems();
        for (InventoryItem it : items) {
            int x = offX + it.getCol() * cellW;
            int y = offY + it.getRow() * cellH;
            int w = it.getSpanCol() * cellW;
            int h = it.getSpanRow() * cellH;
            g.drawImage(it.getImage(), x, y, w, h, null);
        }

        if (dragging && dragItem != null) {
            int x = offX + selCol * cellW;
            int y = offY + selRow * cellH;
            int w = dragItem.getSpanCol() * cellW;
            int h = dragItem.getSpanRow() * cellH;
            Composite old = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g.drawImage(dragItem.getImage(), x, y, w, h, null);
            g.setComposite(old);
        }


        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawRect(offX + selCol * cellW, offY + selRow * cellH, cellW, cellH);
    }
}
