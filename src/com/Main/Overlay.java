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
import com.Util.Sounds;
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
    private boolean isPlaying;
    private OverlayPanels[] panels;
    private Sounds sounds;
    private String currentSound;
    private int soundIndex;

    public Overlay(JFrame frame){
       this.frame = frame;
       buttons = new ArrayList<>();
       panels = new OverlayPanels[]{new OverlayPanels(), new OverlayPanels(), new OverlayPanels()};
       sounds = MONKEECHESS.chessBoard.sounds;
       currentSound = sounds.getSoundNames()[0];
       isPlaying = true;
       soundIndex = 0;
    }

    public void createPanels(){ // function that will make the panels
        Rectangle[] bounds = new Rectangle[3];
        bounds[0] = new Rectangle(70, 730, 860, 90);
        bounds[1] = new Rectangle(10, 10, 50, 800);
        bounds[2] = new Rectangle(945, 10, 50, 800);
        for(int i = 0; i < 3; i ++) {
            OverlayPanels panel = panels[i];
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

    public OverlayPanels getLeftPanel(){
        return this.panels[1];
    }

    public OverlayPanels getRightPanel(){
        return this.panels[2];
    }


    public void createLabel(JPanel panel, int k){
        JLabel label = new JLabel();
        label.setBounds(0,0, 50, 30);
        BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.black, Color.black, Color.black, Color.black);
        label.setBorder(border);
        label.setBackground(Color.black);
        label.setText(k == 1 ? "Black" : "White");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
    }

    public void makeBottomLabels(JPanel panel){
        String [] labels = {"View", "Sound", "Rotation"};

        String [][] buttonLabels = {{"Top", "Normal", "Opposite"}, {"Stop", "Play", "Change"}, {"Lock", "Unlock", "Reset"}};

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
                switch (jButton.getText()){
                    case "Reset":
                        ChessBoard.mouseRotation.resetRotation();
                        break;
                    case "Top":

                        MONKEECHESS.changeViewer(MONKEECHESS.su, new Point3d(0, 35, 0));
                        break;
                    case "Normal":
                        MONKEECHESS.resetViewer(MONKEECHESS.su, MONKEECHESS.position);
                        break;
                    case "Lock":
                        ChessBoard.mouseRotation.pauseRotation();
                        break;
                    case "Unlock":
                        ChessBoard.mouseRotation.resumeRotation();
                        break;
                    case "Opposite":
                        Point3d position = MONKEECHESS.position;
                        position.z = -position.z;
                        ChessBoard.isWhite = !ChessBoard.isWhite;
                        MONKEECHESS.resetViewer(MONKEECHESS.su, position);
                        break;
                    case "Stop":
                        if(isPlaying){
                            sounds.stopSound(currentSound);
                            isPlaying = false;
                        }
                        break;
                    case "Play":
                        if(!isPlaying){
                           sounds.playSound(currentSound);
                            isPlaying = true;
                        }
                        break;
                    case "Change":
                        if(soundIndex == 9){
                            soundIndex = 0;
                            sounds.soundJOAL.stop(currentSound);
                            currentSound = sounds.getSoundNames()[soundIndex];
                            sounds.playSound(currentSound);
                        }else{
                            soundIndex++;
                            sounds.soundJOAL.stop(currentSound);
                            currentSound = sounds.getSoundNames()[soundIndex];
                            sounds.playSound(currentSound);
                        }
                        break;
                }
            }
        }
    }
}
