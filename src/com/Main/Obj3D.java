/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
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

    public Obj3D() {
        this.piece = null;
    }


    public Obj3D(Piece piece) {
        this.piece = piece;
    }


    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public static Piece getPiece(Shape3D shape) {
        return (Piece) shape.getParent().getParent().getParent();
    }
}
