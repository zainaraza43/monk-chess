package com.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameOver implements KeyListener {

    public GameOver(){

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Q){
            System.out.println("QuitGame");
        }
        if(e.getKeyCode() == KeyEvent.VK_R){
            System.out.println("Restart");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
