/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Overlay.java
 */
package com.Main;

import Launcher.Buttons;
import org.jogamp.vecmath.Point3d;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Overlay extends JPanel implements ActionListener {
    private JFrame frame;
    private ArrayList<Buttons> buttons;
    private boolean spin = false;

    public Overlay(JFrame frame){
       this.frame = frame;
       buttons = new ArrayList<>();
    }

    public void createPanels(){ // function that will make the panels
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
            if(i == 0){
                makeBottomLabels(panel);
            }
            else{
                createLabel(panel, i);
            }
            this.frame.add(panel);
        }
    }

    public void createLabel(JPanel panel, int k){
        JLabel label = new JLabel();
        label.setBounds(0,0, 50, 30);
        BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.black, Color.black, Color.black, Color.black);
        label.setBorder(border);
        label.setBackground(Color.black);
        label.setText("P" + k);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
    }

    public void makeBottomLabels(JPanel panel){
        String [] labels = {"View", "Sound", "Rotation"};

        String [][] buttonLabels = {{"Top", "Normal", "Spin"}, {"Stop", "Play", "Change"}, {"Lock", "Unlock", "Reset"}};

        for(int i = 0; i < 3; i ++){
            JLabel label = new JLabel(labels[i]);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.setBounds(20 + i * 280, 10, 266, 70);
            BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.black, Color.black, Color.black, Color.black);
            label.setBorder(border);
            label.setBackground(Color.black);
            createButtons(panel, buttonLabels[i], 30 + i * 280);
            panel.add(label);
        }
    }
    public void createButtons(JPanel panel, String [] labels, int x){

        for(int i = 0; i < labels.length; i ++){
            Buttons jbuttons = new Buttons(labels[i]);
            jbuttons.setBounds(x + i * 80, 40, 80, 20);
            jbuttons.makeButton(this);
            buttons.add(jbuttons);
            panel.add(jbuttons.getButton());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Buttons button : buttons){
            JButton jButton = button.getButton();
            if (e.getSource() == jButton){
                System.out.println(button.getLabel());
                switch (jButton.getText()){
                    case "Reset":
                        System.out.println("Reset rotation");
                        ChessBoard.mouseRotation.resetRotation();
                        break;
                    case "Top":

                        MONKEECHESS.changeViewer(MONKEECHESS.su, new Point3d(0, 35, 0));
                        System.out.println("Top view now");
                        break;
                    case "Normal":
                        MONKEECHESS.resetViewer(MONKEECHESS.su, MONKEECHESS.position);
                        System.out.println("Normal view now");
                        break;
                    case "Lock":
                        ChessBoard.mouseRotation.pauseRotation();
                        System.out.println("Rotation paused");
                        break;
                    case "Unlock":
                        ChessBoard.mouseRotation.resumeRotation();
                        System.out.println("Rotation resumed");
                        break;
                }
            }
        }
    }
}
