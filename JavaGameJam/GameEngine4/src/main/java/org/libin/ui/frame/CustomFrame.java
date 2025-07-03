package org.libin.ui.frame;

import org.libin.ui.mainpanel.MainPanel;

import javax.swing.*;
import java.awt.*;

public class CustomFrame extends JFrame {
    private final int contentWidth = 800;
    private final int contentHeight = 600;

    public CustomFrame() {
        setTitle("Echo Of The Husk demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel();
        mainPanel.setPreferredSize(new Dimension(contentWidth, contentHeight));

        setContentPane(mainPanel);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
    }
}