package Launcher;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Launcher extends JPanel {
    private ImageIcon background;
    private JLabel mylabel, textlabel;
    private Font font;
    private JPanel textPanel ,background_p;


    public Launcher(){
        JFrame jFrame = new JFrame("MonkeCraft");
        jFrame.setSize(1000, 600);
        Frame(jFrame);
    }
    public void Frame(JFrame frame){
        textPanel = new JPanel();
        textPanel.add(fontLoader(textPanel));
        textPanel.add(loadImage());

        frame.add(textPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public JLabel fontLoader(JPanel panel){
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")).deriveFont(70f);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/Minecrafter_3.ttf")));
        }
        catch (IOException | FontFormatException e){

        }

        textlabel = new JLabel("MONKECRAFT");// new label
        textlabel.setFont(font);
        textlabel.setForeground(new Color(102, 99, 99));
        textlabel.setLayout(null);
        textlabel.setBounds(0,0,1000,600);// set label bounds, position and width/height
        return textlabel;
    }

    public JLabel loadImage(){
        background = new ImageIcon("Assets/Launcher/Background.jpg");// load background
        mylabel  = new JLabel(background); // new label
        mylabel.setLayout(null);
        mylabel.setBounds(0,0,1000,600);// set label bounds, position and width/height
        return mylabel;
    }

    public static void main(String[] args) {
        new Launcher();
    }
}
