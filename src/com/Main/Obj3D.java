/*
 * User defined Shape3D file that holds the obj file data and is also used for pick behavior
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
