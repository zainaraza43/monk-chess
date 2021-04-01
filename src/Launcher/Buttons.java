package Launcher;

import keeptoo.KButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Buttons{
    private int x, y, width, height;
    private String label;
    private KButton button;
    private Font font;
    private Color color;
    private ImageIcon image;

    public Buttons(){
//        button = new keeptoo.KButton();
        button = new KButton();
        color = new Color(255, 255, 255);
        image = new ImageIcon("Assets/Launcher/button.jpg");

    }
    public Buttons(String label){
        this();
        this.label = label;
    }

    public Buttons(int x, int y, int width, int height, String label){
        this(label);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public Buttons(int x, int y, int width, int height, String label, Font font){
        this(x, y, width, height, label);
        this.font = font;
    }

    public void makeButton(ActionListener listener){ // will make a button
        //button.setBackground(color); // set the background color .... note not working
        button.setBounds(x, y, width, height); // set the button bounds
        button.setFont(font);
        button.setText(label);
        button.addActionListener(listener);
        button.setFocusable(false);
        button.setkBackGroundColor(new Color(0, 0, 0));
        button.setkBorderRadius(0);
        button.setkBackGroundColor(new Color(0,0,0));
        button.setkStartColor(new Color(75,75,75));
        button.setkEndColor(new Color(75,75,75));
        button.setkForeGround(new Color(255,255,255));
        button.setkHoverForeGround(new Color(255,255,255));
        button.setkHoverEndColor(new Color(75,75,75));
        button.setkHoverColor(new Color(75,75,75));
        button.setkHoverStartColor(new Color(75,75,75));
    }

    public void setBounds(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JButton getButton() {
        return this.button;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
