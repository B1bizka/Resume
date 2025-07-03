package org.libin.ui.panels;

import org.libin.game.inventory.InventoryController;
import org.libin.game.inventory.InventoryModel;
import org.libin.game.inventory.InventoryView;
import org.libin.game.rendering.Camera;
import org.libin.game.systems.GameContext;
import org.libin.game.systems.GameController;
import org.libin.ui.mainpanel.MainPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameAreaPanel extends JPanel {

    // game cords
    private static final int GAME_W = 500, GAME_H = 500;
    //inventory cords
    //private static final int INV_ROWS = 5, INV_COLS = 3;

    private static final int INV_X = 580, INV_Y = 240;
    private static final int INV_W = 150, INV_H = 250;

    private  final GameScreenPanel ParentGamePanel;
    private final BufferedImage screenBuffer;

    private Image inventaryImage;
    private GameController controller;
    //private final Camera camera;

    private final InventoryModel inventoryModel;
    private final InventoryView inventoryView;
    private final InventoryController inventoryController;

    public GameAreaPanel(GameScreenPanel parentPanel){
        ParentGamePanel = parentPanel;
        setDoubleBuffered(true);
        setFocusable(true);

        screenBuffer = new BufferedImage(GAME_W, GAME_H, BufferedImage.TYPE_INT_RGB);

        GameController gc = ParentGamePanel.getGameController();
        inventoryModel = gc.getInventoryModel();


        try {
            inventaryImage = ImageIO.read(getClass().getResource("/Interface/menu_background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inventoryView   = new InventoryView(inventoryModel, inventaryImage);
        inventoryController = new InventoryController(inventoryModel, inventoryView);

        addKeyListener(inventoryController);
        attachCameraListener();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameController gc = ParentGamePanel.getGameController();
        if (gc != null) {
            gc.renderToBuffer(screenBuffer);
        }
        g.drawImage(screenBuffer, 0, 0, null);

        Graphics2D g2 = (Graphics2D) g.create();
        inventoryView.paint(g2, INV_X, INV_Y, INV_W, INV_H);
        g2.dispose();

    }

    public void attachCameraListener() {
        GameController gc = ParentGamePanel.getGameController();
        if (gc == null) return;
        Camera cam = gc.getCamera();
        if (cam != null) {
            addKeyListener(cam);
            requestFocusInWindow();
        }
    }
}
