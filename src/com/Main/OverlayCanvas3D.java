/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * OverlayCanvas3D.java
 */
package com.Main;

import Launcher.Launcher;
import org.jogamp.java3d.Canvas3D;
import java.awt.*;

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
    public void postRender(){ // will update the status
        Graphics2D  g2D = getGraphics2D();
        g2D.setColor(color);
        g2D.setFont(msgFont);

        if(status != null){
            int width = g2D.getFontMetrics().stringWidth(status);
            g2D.drawString(status, 850 / 2 - width / 2, 50);
            getGraphics2D().flush(false); // prevents flickering
        }
    }

    public void setColor(Color color){ // set the color of the text
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
    } // update the status
}
