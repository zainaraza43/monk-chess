package Launcher;

import com.Main.ChessPieces;
import com.Main.MONKEECHESS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Graphics;


import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.Component.*;


public class ImagePanel extends JPanel implements MouseListener {
    private ArrayList<ImageIcon> images = new ArrayList<>();
    private ArrayList<ImageIcon> pieces = new ArrayList<>();
    private ArrayList<JLabel> label = new ArrayList<>();
    private ArrayList<JLabel> label1 = new ArrayList<>();

    public ImagePanel() {
        // images.add(new ImageIcon("Assets/images/"+"chess" + 0+ ".jpg"));
        check();
    }

    @Override
    protected void paintComponent(Graphics g) {

        Toolkit t = Toolkit.getDefaultToolkit();

        for (int i = 0; i <= 3; i++) {
            pieces.add(new ImageIcon("Assets/images/"+ "piece"+i+".jpg"));
            images.add(new ImageIcon("Assets/images/" + "chess" + i + ".jpg"));
            g.drawImage(images.get(i).getImage(), 120, 80 + (130 * i), 80, 80, this);
            images.get(i).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);

            g.drawImage(pieces.get(i).getImage(), 360, 80 + (130 * i), 80, 80, this);
            pieces.get(i).getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        }

    }

    public void check() {

        for(int i=0;i<=3;i++) {

            JLabel c1 = new JLabel("     ");
            label1.add(c1);
            label1.get(i).setBounds(360,80+(130*i),80,80);
            //cl.setLocation(120,80 + (130 * i));
            add(label1.get(i));

        }
        label1.get(0).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Launcher.black="mahogany";
                Launcher.white="gold";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });

        label1.get(1).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ChessPieces.textureNameWhite = "piece0";
                ChessPieces.textureNameBlack = "piece";
                System.out.println("SHUT RAT");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });

        label1.get(2).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ChessPieces.textureNameWhite = "piece1";
                ChessPieces.textureNameBlack = "piece2";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });

        label1.get(3).addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                MONKEECHESS.board="chess";
                System.out.println("black");
            }
            public void mouseReleased(MouseEvent e) {

            }
        });


    }


    @Override
    public void mouseClicked(MouseEvent e) {

        int ClickedCount = e.getClickCount();
        if (ClickedCount > 0) {
            System.out.println("THE RAT");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}