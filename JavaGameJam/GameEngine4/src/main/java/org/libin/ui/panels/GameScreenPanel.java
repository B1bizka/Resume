package org.libin.ui.panels;

import lombok.Getter;
import org.libin.game.systems.GameController;
import org.libin.ui.mainpanel.MainPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.libin.ui.mainpanel.ScreenName.END_SCREEN_DEMO;
import static org.libin.ui.mainpanel.ScreenName.START_MENU;


public class GameScreenPanel extends JPanel {

    private static final int GAME_AREA_X   = 25;
    private static final int GAME_AREA_Y   = 25;
    private static final int GAME_AREA_W   = 750;
    private static final int GAME_AREA_H   = 500;

    private static MainPanel ParentMainMenu;
    private static GameAreaPanel gameArea;
    @Getter
    private GameController gameController;
    private final JLayeredPane layeredPane;
    private final ImageIcon overlayImage = new ImageIcon(getClass().getResource("/Interface/screen.PNG"));
    private Image[] hpIcons = new Image[4];

    public GameScreenPanel(MainPanel mainPanel){
        ParentMainMenu = mainPanel;
        layeredPane = new JLayeredPane();
        loadHpIcons();
        init();
    }
    private void init(){
        setLayout(new BorderLayout());

        gameController = new GameController(this);
        //gameController.startLevel(1);

        gameArea = new GameAreaPanel(this);

        gameArea.setBounds(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_W, GAME_AREA_H);
        layeredPane.add(gameArea, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));

        gameArea.attachCameraListener();

        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(overlayImage.getImage(), 0, 0, getWidth(), getHeight(), null);

                if (gameController != null) {
                    int hp = gameController.getHP();
                    if (hp > 0 && hp <= 4) {
                        Image icon = hpIcons[hp-1];
                        g.drawImage(icon, 0, 0, 800, 600, null);
                    }
                }
            }
        };

        overlay.setOpaque(false);
        overlay.setFocusable(false);
        overlay.setBounds(0, 0, 800, 600);
        layeredPane.add(overlay, Integer.valueOf(JLayeredPane.PALETTE_LAYER));

        add(layeredPane, BorderLayout.CENTER);

        gameArea.setFocusable(true);
        SwingUtilities.invokeLater(() -> gameArea.requestFocusInWindow());

    }

    public void startNewLevel(int levelIndex) {
        //gameController.stopGameLoop();
        gameController.startLevel(levelIndex);
        gameArea.attachCameraListener();

        SwingUtilities.invokeLater(() -> {
            gameArea.requestFocusInWindow();
        });
    }

    public GameAreaPanel getGameArea() {
        return gameArea;
    }
    public void toMainPanel(){
        ParentMainMenu.showScreen(START_MENU);
    }
    public void toEnd(){
        ParentMainMenu.showScreen(END_SCREEN_DEMO);
    }
    private void loadHpIcons(){
        try {
            for (int i = 1; i <= 4; i++) {
                hpIcons[i-1] = ImageIO.read(
                        getClass().getResource("/Interface/hp" + i + ".PNG"));
            }
        } catch (IOException e) {e.printStackTrace();}
    }
}
