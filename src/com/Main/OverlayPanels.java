package com.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OverlayPanels extends JPanel {
    private ArrayList<ImageIcon> icons;

    public OverlayPanels(){
        super();
        icons = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        Rectangle r = getBounds();
        int width = (int) r.getWidth();
        int height = (int) r.getHeight();
        g.fillRect(0 , 0, width, height);

        for(int i = 0; i < icons.size(); i ++){
            Image image = icons.get(i).getImage();
            g.drawImage(image, 10, 40 + 40 * i, 30, 30,null);
        }

    }

    public void addIcon(String name){
        icons.add(ChessPieces.icons.get(name));
        System.out.println(name);
    }
}
