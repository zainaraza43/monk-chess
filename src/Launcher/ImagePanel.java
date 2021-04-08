package Launcher;

import com.Main.ChessBoard;
import com.Main.ChessPieces;
import com.Main.MONKEECHESS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.JPanel;
import java.awt.event.*;
import java.io.File;

public class ImagePanel extends JPanel{
    private ArrayList<ImageIcon> images = new ArrayList<>();
    private ArrayList<ImageIcon> pieces = new ArrayList<>();
    private ArrayList<ImageIcon> background = new ArrayList<>();
    private ArrayList<JLabel> label = new ArrayList<>();
    private ArrayList<JLabel> label1 = new ArrayList<>();
    private ArrayList<JLabel> label2 = new ArrayList<>();
    private ChessPieces chessPieces;
    AudioClip click;
    public ImagePanel() {
        chessPieces = new ChessPieces("mahogany","gold");
        check();
    }
    @Override
    protected void paintComponent(Graphics g) {
        for (int i = 0; i <= 3; i++) {
            background.add(new ImageIcon("Assets/Images/background"+i+".jpg"));
            pieces.add(new ImageIcon("Assets/Images/"+ "piece"+i+".jpg"));
            images.add(new ImageIcon("Assets/Images/" + "chess" + i + ".jpg"));

            g.drawImage(background.get(i).getImage(), 600, 80 + (130 * i), 100, 60, this);
            background.get(i).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);

            g.drawImage(images.get(i).getImage(), 120, 80 + (130 * i), 80, 80, this);
            images.get(i).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);

            g.drawImage(pieces.get(i).getImage(), 360, 80 + (130 * i), 80, 80, this);
            pieces.get(i).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        }

    }

    public void check() {
        for(int i=0;i<=3;i++) {
            JLabel c1 = new JLabel("     ");
            JLabel c2 = new JLabel("     ");
            JLabel c3 = new JLabel("     ");

            label.add(c1);
            label1.add(c2);
            label2.add(c3);
            label1.get(i).setBounds(360,80+(130*i),80,80);
            label.get(i).setBounds(120,80+(130*i),80,80);
            label2.get(i).setBounds(600,80+(130*i),100,60);

            add(label1.get(i));
            add(label.get(i));
            add(label2.get(i));
        }
        mouse();
    }

    public void mouse(){
        try{
            URL urlClick = new File("Assets/Sounds/SoundEffects/click.wav").toURI().toURL();
            click = Applet.newAudioClip(urlClick);
            //more code goes here
        }catch(java.net.MalformedURLException ex){
//do exception handling here
        }

        label.get(0).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.board = "chess0";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });
        label.get(1).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.board = "chess1";
                System.out.println("gold");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label.get(2).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.board = "chess3";
                System.out.println("gray");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });
        label.get(3).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.board="chess2";
                System.out.println("red");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });
        label1.get(0).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                chessPieces.textureNameWhite = "mahogany";
                chessPieces.textureNameBlack = "gold";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });
        label1.get(1).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                chessPieces.textureNameWhite = "piece0";
                chessPieces.textureNameBlack = "piece1";
                System.out.println("SHUT RAT");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });
        label1.get(2).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                ChessPieces.textureNameWhite = "piece1";
                ChessPieces.textureNameBlack = "piece";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label1.get(3).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                ChessPieces.textureNameWhite = "gold";
                ChessPieces.textureNameBlack = "piece";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label2.get(0).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.ground="background0";
                System.out.println("space");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label2.get(1).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.ground="background1";
                System.out.println("jungle");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label2.get(2).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.ground="background2";
                System.out.println("rainbow");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label2.get(3).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click.play();
                MONKEECHESS.ground="background3";
                System.out.println("rainbow");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

    }

}