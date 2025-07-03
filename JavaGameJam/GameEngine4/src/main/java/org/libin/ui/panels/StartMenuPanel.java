package org.libin.ui.panels;


import org.libin.game.audio.Sound;
import org.libin.ui.mainpanel.MainPanel;
import org.libin.ui.mainpanel.ScreenName;

import javax.swing.*;
import java.awt.*;


public class StartMenuPanel extends JPanel {

    private final MainPanel ParentMainPanel;

    private final String GameName = "Echo Of The Husk";
    private final String StartButtonName = "START";
    private final String ExitButtonName = "EXIT";
    private final Sound sound;

    // right now i'm using font from the Dark souls series, but we have plans to make our own font
    private final Font TitleFont = new Font("Trajan Pro 3",Font.PLAIN,40);
    private final Font ButtonFont = new Font("Trajan Pro 3",Font.PLAIN,20);

    public StartMenuPanel(MainPanel parent) {
        ParentMainPanel = parent;
        setBackground(Color.BLACK);

        sound = new Sound();

        // Title
        JLabel title = new JLabel(GameName);
        title.setFont(TitleFont);
        title.setForeground(Color.WHITE);
        title.setBounds(200, 100, 600, 100);
        add(title);

        // Start button
        JButton start = new JButton(StartButtonName);
        styleButton(start);
        start.setBounds(300, 300, 200, 50);
        start.addActionListener(e -> {
            sound.stopMusic();
            ParentMainPanel.showScreen(ScreenName.GAME_SCREEN);
            ParentMainPanel.getGameScreen().startNewLevel(1);
        });
        add(start);

        // Exit button
        JButton exit = new JButton(ExitButtonName);
        styleButton(exit);
        exit.setBounds(300, 350, 200, 50);
        exit.addActionListener(e ->
                System.exit(0));
        add(exit);
        setLayout(null);
    }

    public void playMusic(){
        sound.setMusic(3);
        sound.playMusicLoop();
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
