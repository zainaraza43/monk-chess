package Launcher;


import com.company.Craft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Launcher extends JPanel implements ActionListener {
    private ImageIcon background;
    private JLabel mylabel, textlabel, backlabel;
    private Font font, fontOver, fontBack;
    private JPanel Panel;
    private Buttons jbuttons;
    private ArrayList<Buttons> buttons = new ArrayList<>();
    private JFrame jFrame;


    public Launcher(){
        jFrame = new JFrame("MonkeCraft");
        jFrame.setSize(1000, 600);
        ImageIcon imageIcon = new ImageIcon("Assets/Launcher/icon.png"); // setting the image icon
        jFrame.setIconImage(imageIcon.getImage());
        Frame(jFrame);
    }
    public void Frame(JFrame frame){
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

    public Font ttfFontloader(float size){ // will load in the ttf font

        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")).deriveFont(size);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")));
        }
        catch (IOException | FontFormatException e){

        }
        return font;
    }

    public void fontLoader(JPanel jPanel){ // will load the fonts in
        fontOver = ttfFontloader(70f);
        fontBack = ttfFontloader(70f);

        textlabel = new JLabel("MONKECRAFT", JLabel.CENTER); // make a label and center it also the top font
        textlabel.setFont(fontOver);
        textlabel.setAlignmentX(0.5f); // align
        textlabel.setForeground(new Color(127, 129, 133)); // set the color

        backlabel = new JLabel("MONKECRAFT", JLabel.CENTER); // make back font and center it
        backlabel.setFont(fontBack);
        backlabel.setAlignmentX(0.49f); // align it so that a shadow effect shows
        backlabel.setAlignmentY(0.45f);
        backlabel.setForeground(new Color(0, 0, 0)); // set the back color to black

        jPanel.add(textlabel);
        jPanel.add(backlabel);
    }

    public JLabel loadImage(){ // will load in the background image
        background = new ImageIcon("Assets/Launcher/Background.jpg");// load background
        mylabel  = new JLabel(background, JLabel.CENTER); // new label
        mylabel.setAlignmentX(0.5f);
        mylabel.setAlignmentY(0.2f);

        return mylabel;
    }

    public void makeButtons(JFrame jframe){
        String [] options = {"SINGLE PLAYER", "MULTIPLAYER", "EXIT"};
        for(int i = 0; i < 3; i ++){
            jbuttons = new Buttons(0, 250 + (i * 60), 260 + (2 * options[i].length()), 40, options[i], ttfFontloader(20f));
            jbuttons.setX(540 - jbuttons.getWidth() / 2);
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
        for(Buttons buttons : buttons){
            if(e.getSource() == buttons.getButton()){
                if(buttons.getButton().getText().equals("SINGLE PLAYER")){ // if single player is pressed
                    jFrame.dispose(); // dispose of old frame
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new Craft.MyGUI("CRAFT"); // call the game
                        }
                    });
                }
                if(buttons.getButton().getText().equals("MULTIPLAYER")){ // if multiplayer is pressed
                    System.out.println("hello");
                }
                if(buttons.getButton().getText().equals("EXIT")){ // if exit is pressed
                    System.exit(0); // exit
                }
            }
        }
    }
}
