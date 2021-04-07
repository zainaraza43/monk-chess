/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Obj3D.java
 * User defined class used for picking
 */
package com.Main;

import org.jogamp.java3d.Shape3D;

public class Obj3D extends Shape3D {
    private Piece piece;

    public Obj3D(){
        this.piece = null;
    }


    public Obj3D(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
