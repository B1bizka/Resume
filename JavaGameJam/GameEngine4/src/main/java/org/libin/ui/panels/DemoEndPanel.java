package org.libin.ui.panels;

import org.libin.game.audio.Sound;
import org.libin.ui.mainpanel.MainPanel;
import org.libin.ui.mainpanel.ScreenName;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DemoEndPanel extends  JPanel {

    private final MainPanel ParentMainPanel;
    private final Font ButtonFont = new Font("Trajan Pro 3",Font.PLAIN,20);
    private final Image backgroundImage;
    private final Sound sound;

    public DemoEndPanel(MainPanel mainPanel){
        ParentMainPanel = mainPanel;

        sound = new Sound();



        URL url = getClass().getResource("/Interface/end.png");
        if (url == null) {
            throw new IllegalStateException("no /Interface/end.png");
        }
        backgroundImage = new ImageIcon(url).getImage();
        setLayout(null); // абсолютное позиционирование кнопки



        JButton exitToMenu = new JButton("Exit To Menu");
        styleButton(exitToMenu);
        exitToMenu.setBounds(300, 300, 200, 50);
        exitToMenu.addActionListener(e -> {
            sound.stopMusic();
            ParentMainPanel.showScreen(ScreenName.START_MENU);
        });
        add(exitToMenu);


    }

    public void playMusic(){
        sound.setMusic(2);
        sound.playMusicLoop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    private void styleButton(JButton b) {
        b.setFont(ButtonFont);
        b.setForeground(Color.WHITE);
        b.setBackground(Color.BLACK);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(true);
    }
}
