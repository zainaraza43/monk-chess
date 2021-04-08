package Launcher;

import com.Main.ChessPieces;

import javax.swing.*;
import java.awt.*;

public class GameOver extends JPanel {
    String black;
    String white;

    public GameOver() {
        this.white = ChessPieces.textureNameWhite;
        this.black = ChessPieces.textureNameBlack;

    }

    public void EndGame(){
        JFrame frame = new JFrame("GAME OVER");
        setLayout(null);
        frame.setSize(400,200);
        frame.setLocation(850,500);
        frame.add(this);

        JLabel label = new JLabel();
        label.setText("The game is now over the king is dead");
        label.setBounds(80, 25,400, 50);
        JButton restart = new JButton("Restart");
        JButton exit = new JButton("Exit Game");

        restart.setSize(75,75);
        restart.setBounds(80,100,100,40);
        exit.setBounds(200,100,100,40);

        add(restart);
        add(exit);
        add(label);
        frame.getContentPane().add(this);
        frame.setVisible(true);

        restart.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChessPieces chessPieces = new ChessPieces(black,white);
                chessPieces.restart();
                System.out.println("Reset pieces and score");

            }
        });

        exit.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
    }


}
