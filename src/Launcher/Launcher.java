package Launcher;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Launcher extends JPanel {
    private ImageIcon background;
    private JLabel mylabel, textlabel;
    private Font font;
    private JPanel textPanel;


    public Launcher(){


        JFrame jFrame = new JFrame("MonkeCraft");
        jFrame.setSize(1000, 600);

        JPanel panel = new JPanel();
        loadImage(panel);
        jFrame.add(panel);

        textPanel = new JPanel();
        fontLoader(textPanel);
        jFrame.add(textPanel);

        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    public void fontLoader(JPanel jpanel){
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")).deriveFont(100f);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")));
        }
        catch (IOException | FontFormatException e){

        }


        textlabel = new JLabel("MONKECRAFT");
        textlabel.setFont(font);
        textlabel.setForeground(new Color(102, 99, 99));
        jpanel.add(textlabel);
    }

    public void loadImage(JPanel panel){
        background = new ImageIcon("Assets/Launcher/Background.jpg");
        mylabel = new JLabel(background);
        panel.add(mylabel);
    }

    public static void main(String[] args) {
        new Launcher();
    }
}
