package Launcher;

import com.Main.ChessPieces;

import javax.swing.*;
import java.awt.*;

public class GameEnd extends JPanel {
    String black;
    String white;

    public GameEnd() {
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
                System.out.println("This is the reset button it should reset all the pieces and the position of those" +
                        "pieces but as you can see am a fat fuck who is sleep deprived and can barely keep his eyes open." +
                        "Is someone calling me ? or is it just the voices in my head? Anyways like i was saying we need to " +
                        "reset the pieces and the position of those pieces when the reset button is hit.You might ask, why didn't you " +
                        "do that? That's a great question. NOW WHY DON'T YOU SHUT THE FUCK UP AND START WORKING ON THIS SHITTY ASS" +
                        "FUCKING USELESS JANK FUCKING CODE THAT WOULD LITERALLY GET US ALL FLAMED BY THE PROGRAMMING COMMUNITY" +
                        "THESE GUYS ARE MOURNING FOR US ON FUCKING REDDIT , THESE MF OLDER THAN MY DAD AND THEY ARE BASHING JAVA3D. THAT'S HOW BAD THIS SHIT IS. Anyways i got that off my chest now so i should be fine"+
                        "no wait... WHO TF PUSHED ARE YOU FUCKING KIDDING ME , WTF YOU GUYS PUSHING BRO?? I SWEAR TO GOD YOU GUYS ADD A PERIOD AND ARE LIKE" +
                        " 'well this seems good enough to push , send that shit.' FOR THE LOVE OF GOD I HATE THIS PROJECT AND I HOPE NO ONE HAS TO DO JAVA3D" +
                        "EVER AGAIN.)");
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
