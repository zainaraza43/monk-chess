package com.Main;

import Launcher.Launcher;

import javax.swing.*;
public class GameOver extends JPanel {
    public void endGame(){

        JFrame frame = new JFrame("GAME OVER");
        setLayout(null);
        frame.setResizable(false);
        frame.setSize(400,200);
        frame.setLocation(850,500);
        frame.add(this);

        JLabel label = new JLabel();
        label.setText("The game is now over the king is dead");
        label.setBounds(80, 25,400, 50);
        JButton exit = new JButton("Exit Game");

        exit.setBounds(125,100,150,40);

        add(exit);
        add(label);
        frame.getContentPane().add(this);
        frame.setVisible(true);

        exit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
    }
}
