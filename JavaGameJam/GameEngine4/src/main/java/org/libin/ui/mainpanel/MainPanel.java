package org.libin.ui.mainpanel;

import lombok.Getter;
import org.libin.ui.panels.DemoEndPanel;
import org.libin.ui.panels.GameScreenPanel;
import org.libin.ui.panels.StartMenuPanel;

import javax.swing.*;
import java.awt.*;

@Getter
public class MainPanel extends JPanel {

    private final CardLayout cardLayout = new CardLayout();
    private final StartMenuPanel startMenu;
    private final DemoEndPanel demoEndPanel;
    @Getter
    private final GameScreenPanel gameScreen;

    public MainPanel(){
        setBounds(0,0,800,600);
        setLayout(cardLayout);

        startMenu  = new StartMenuPanel(this);
        gameScreen = new GameScreenPanel(this);
        //Todo End panel
        demoEndPanel = new DemoEndPanel(this);


        add(startMenu,ScreenName.START_MENU.name());
        add(gameScreen,ScreenName.GAME_SCREEN.name());
        add(demoEndPanel,ScreenName.END_SCREEN_DEMO.name());


        showScreen(ScreenName.START_MENU);


    }

    public void showScreen(ScreenName screenName) {
        cardLayout.show(this, screenName.name());
        if (screenName == ScreenName.END_SCREEN_DEMO) {
            demoEndPanel.playMusic();
        }
        if (screenName == ScreenName.START_MENU) {
            startMenu.playMusic();
        }
    }
}
