package com.Main;

import Launcher.Launcher;
import org.jogamp.java3d.Canvas3D;

import java.awt.*;

public class OverlayCanvas3D extends Canvas3D {
    private Font msgFont;
    private String status;
    public static final int xPos = 15, yPos = 25;

    public OverlayCanvas3D(GraphicsConfiguration config) {
        super(config);
        setDoubleBufferEnable(true);
        status = "Welcome to MONKEECHESS";
        msgFont = Launcher.ttfFontloaderStatic(28);
    }
    @Override
    public void postRender(){
        Graphics2D  g2D = getGraphics2D();
        g2D.setColor(Color.cyan);
        g2D.setFont(msgFont);

        if(status != null){
            g2D.drawString(status, xPos, yPos);
            getGraphics2D().flush(false);
        }
    }
    @Override
    public void repaint() {
        Graphics2D g = getGraphics2D();
        paint(g);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
