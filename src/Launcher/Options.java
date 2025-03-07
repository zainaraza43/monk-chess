/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Options.java
 */
package Launcher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Options extends JPanel implements ActionListener {
    JFrame frame;

    public Options(Font f) { // makes the options panel
        frame = new JFrame("MONKEECHESS");

        ImagePanel panel = new ImagePanel();

        Buttons exit = new Buttons(0, 250, 260, 40, "Go Back", f);
        exit.setX(350);
        exit.setY(0);
        exit.makeButton(this); // make the button and add actionListener to each button
        panel.add(exit.getButton());
        panel.setLayout(null);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(1000, 600);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
    }
}



