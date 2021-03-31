package com.Main;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

public class Overlay extends JPanel {
    private JFrame frame;

    public Overlay(JFrame frame){
       this.frame = frame;
    }

    public void createPanels(){
        Rectangle[] bounds = new Rectangle[3];
        bounds[0] = new Rectangle(70, 730, 860, 90);
        bounds[1] = new Rectangle(10, 10, 50, 800);
        bounds[2] = new Rectangle(945, 10, 50, 800);
        for(int i = 0; i < 3; i ++) {
            JPanel panel = new JPanel();
            panel.setBounds(bounds[i]);
            BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.black, Color.black, Color.black, Color.black);
            panel.setBorder(border);
            panel.setBackground(Color.gray);
            panel.setVisible(true);
            panel.setLayout(null);
            this.frame.add(panel);
        }
    }

}
