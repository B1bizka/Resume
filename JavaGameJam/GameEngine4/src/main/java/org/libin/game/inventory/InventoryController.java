package org.libin.game.inventory;

import org.libin.game.rendering.Camera;
import org.libin.game.systems.GameController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InventoryController extends KeyAdapter {
    private final InventoryModel model;
    private final InventoryView view;
    private Camera camera;

    public InventoryController(InventoryModel m, InventoryView v) {
        this.model = m; this.view = v;
    }

    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                view.moveSelectionLeft();
                break;
            case KeyEvent.VK_RIGHT:
                view.moveSelectionRight();
                break;
            case KeyEvent.VK_UP:
                view.moveSelectionUp();
                break;
            case KeyEvent.VK_DOWN:
                view.moveSelectionDown();
                break;
            case KeyEvent.VK_P:
                if (!view.isDragging()) {
                    InventoryItem it = findItemAt(view.getSelRow(), view.getSelCol());
                    if (it != null) {
                        model.removeItemFromIvn(it);
                        view.startDrag(it);
                    }
                } else {
                    InventoryItem drag = view.getDragItem();
                    int r = view.getSelRow(), c = view.getSelCol();
                    if (model.placeItem(drag, r, c)) {
                        view.dropDrag();
                    }
                }
                break;

            case KeyEvent.VK_O:
                if (view.isDragging()) {
                    InventoryItem drag = view.getDragItem();
                    model.removeItemFromIvn(drag);
                    view.cancelDrag();
                    //model.placeItem(drag, drag.getRow(), drag.getCol());
                }
                break;
            case KeyEvent.VK_I:
                if (view.isDragging()) {
                    camera = GameController.getCamera();
                    InventoryItem drag = view.getDragItem();

                    double frontX = camera.xPos + camera.xDir * Camera.INTERACTION_DISTANCE;
                    double frontY = camera.yPos + camera.yDir * Camera.INTERACTION_DISTANCE;
                    int cellX = (int) frontX;
                    int cellY = (int) frontY;

                    if (cellX >= 0 && cellX < Camera.mapWidth
                            && cellY >= 0 && cellY < Camera.mapHeight
                            && camera.map[cellX][cellY] == -2) {
                        model.removeItemFromIvn(drag);
                        view.cancelDrag();
                        GameController.droppedItems.add(drag);
                    }
                }
        }

        e.getComponent().repaint();

    }

    private InventoryItem findItemAt(int row, int col) {
        for (InventoryItem it : model.getItems()) {
            int r0 = it.getRow(),  c0 = it.getCol();
            int h  = it.getSpanRow(), w  = it.getSpanCol();
            if (row >= r0 && row < r0 + h &&
                    col >= c0 && col < c0 + w) {
                return it;
            }
        }
        return null;
    }
}
