package com.Main;

import Launcher.Launcher;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.vecmath.Color3f;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class OverlayCanvas3D extends Canvas3D {
    private Font msgFont;
    private String status;
    private Color color;

    public OverlayCanvas3D(GraphicsConfiguration config) {
        super(config);
        setDoubleBufferEnable(true);
        status = "Welcome to MONKEECHESS";
        msgFont = Launcher.ttfFontloaderStatic(28);
        color = Color.CYAN;

    }
    @Override
    public void postRender(){
        Graphics2D  g2D = getGraphics2D();
        g2D.setColor(color);
        g2D.setFont(msgFont);

        if(status != null){
            int width = g2D.getFontMetrics().stringWidth(status);
            g2D.drawString(status, 850 / 2 - width / 2, 50);
            getGraphics2D().flush(false);
        }
    }

    public void setColor(Color color){
       this.color = color;
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
