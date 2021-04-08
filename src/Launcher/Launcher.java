/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Launcher.java
 */

package Launcher;

import com.Main.ChessPieces;
import com.Main.MONKEECHESS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Launcher extends JPanel implements ActionListener {
    private ImageIcon background;
    private JLabel mylabel, textlabel, backlabel, smalllabel;
    private Font font, fontOver, fontBack, button_font, fontsmall;
    private JPanel Panel;
    private Buttons jbuttons;
    private ArrayList<Buttons> buttons = new ArrayList<>();
    private JFrame jFrame;
    public static ChessPieces chessPieces;
    public static String black = "mahogany";
    public static String white = "gold";
    public static boolean isMultiplayer = false;

    public Launcher() {
        jFrame = new JFrame("MONKEECHESS");
        jFrame.setSize(1000, 600);
        ImageIcon imageIcon = new ImageIcon("Assets/Launcher/icon.png"); // setting the image icon
        jFrame.setIconImage(imageIcon.getImage());
        Frame(jFrame);
        chessPieces = new ChessPieces(black, white);
    }

    public void Frame(JFrame frame) {

        Panel = new JPanel();
        Panel.setBounds(0, 0, 1000, 600);
        LayoutManager overlay = new OverlayLayout(Panel);
        Panel.setLayout(overlay);

        makeButtons(frame);
        fontLoader(Panel);
        Panel.add(loadImage());
        frame.add(Panel);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    public Font ttfFontloader(float size) { // will load in the ttf font

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/RockFont.ttf")).deriveFont(size);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/RockFont.ttf")));
        } catch (IOException | FontFormatException e) {

        }
        return font;
    }

    public void fontLoader(JPanel jPanel) { // will load the fonts in
        fontOver = ttfFontloader(55f);
        fontBack = ttfFontloader(75f);
        fontsmall = ttfFontloader(25f);

//        smalllabel = new JLabel("FUCK JAVA3D EDITION", JLabel.CENTER); // make a label and center it also the top font
//        smalllabel.setFont(fontsmall);
//        smalllabel.setAlignmentX(0.09f); // align
//        smalllabel.setAlignmentY(0.2f);
//        smalllabel.setForeground(new Color(255, 255, 255)); // set the color

        textlabel = new JLabel("MONKEECHESS", JLabel.CENTER); // make a label and center it also the top font
        textlabel.setFont(fontOver);
        textlabel.setAlignmentX(0.2f); // align
        textlabel.setAlignmentY(1.0f);
        textlabel.setForeground(new Color(236, 202, 21)); // set the color

//        jPanel.add(smalllabel);
        jPanel.add(textlabel);

    }

    public JLabel loadImage() { // will load in the background image
        background = new ImageIcon("Assets/Launcher/Background1.jpg");// load background
        mylabel = new JLabel(background, JLabel.CENTER); // new label
        mylabel.setAlignmentX(0.55f);
        mylabel.setAlignmentY(0.25f);

        return mylabel;
    }

    public void makeButtons(JFrame jframe) {
        try {
            button_font = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/minecraft.ttf")).deriveFont(20f);
        } catch (IOException | FontFormatException e) {

        }
        String[] options = {"SINGLE PLAYER", "MULTIPLAYER", "OPTIONS", "EXIT"};
        for (int i = 0; i < 4; i++) {
            jbuttons = new Buttons(0, 250 + (i * 60), 260 + (2 * options[i].length()), 40, options[i], button_font);
            jbuttons.setX(690 - jbuttons.getWidth() / 2);
            jbuttons.makeButton(this); // make the button and add actionListener to each button
            buttons.add(jbuttons); // add the buton to ArrayList
            jframe.add(jbuttons.getButton()); // add the buttons to the frame
        }
    }

    public static void main(String[] args) {
        new Launcher();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Buttons buttons : buttons) {
            if (e.getSource() == buttons.getButton()) {

                if (buttons.getButton().getText().equals("SINGLE PLAYER")) { // if single player is pressed
                    isMultiplayer = false;
                    startGame();
                }
                if (buttons.getButton().getText().equals("MULTIPLAYER")) { // if multiplayer is pressed
                    isMultiplayer = true;
                    startGame();
                }
                if (buttons.getButton().getText().equals("OPTIONS")) { // if multiplayer is pressed
                    Options op = new Options(button_font);
                }
                if (buttons.getButton().getText().equals("EXIT")) { // if exit is pressed
                    System.exit(0); // exit
                }
            }
        }
    }

    private void startGame() {
        jFrame.dispose(); // dispose of old frame
        chessPieces.loadPieces();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MONKEECHESS.MyGUI("MONKEECHESS"); // call the game

            }
        });
    }
}
