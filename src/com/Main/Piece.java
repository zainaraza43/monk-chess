/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Piece.java
 */
package com.Main;
import com.Util.Sounds;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

public class Piece extends BranchGroup {
    public static final int RAISE_AMOUNT = 3;
    private String name, color, texture;
    private Vector3d oldPosition;
    private BranchGroup highlight;
    public Sounds sounds;
    private float scale;
    private double rotation;

    // will make a piece object based on given paramaters
    public Piece(Obj3D piece, String name, String color, Vector3d position, float scale, double rotation, String texture) {
        piece.setPiece(this); // set the piece
        this.name = name;
        this.color = color;
        this.texture = texture;
        this.highlight = null;
        this.scale = scale;
        this.rotation = rotation;
        this.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup positionTG = new TransformGroup(); // transformgroup used to position and move piece on the board
        positionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        positionTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        positionTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        TransformGroup scaledTG = new TransformGroup(); // transformGroup used to scale the piece so it's the right size
        scaledTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scaledTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

        Transform3D scalar = new Transform3D(); // transform3D to scale
        Transform3D pos = new Transform3D(); // transform3D to position
        pos.setTranslation(position); // set the initial position on the board
        scalar.rotY(rotation); // set the rotation if needed
        scalar.setScale(scale); // set the scale
        scaledTG.setTransform(scalar);

        positionTG.setTransform(pos);
        positionTG.addChild(scaledTG); // add the scaled to the position
        piece.setName(name); // set the name of the piece used in picking
        piece.setUserData(0); // set the userdata
        scaledTG.addChild(piece); // add the piece
        addChild(positionTG);
        setApp(texture); // set the appearance
        this.oldPosition = getPosition();
        sounds = MONKEECHESS.chessBoard.sounds;
    }

    public String getColor() { // get the color of a piece
        return color;
    }

    public float getScale() { // get the scale of a piece
        return scale;
    }

    public String getTexture() { // get the texture
        return texture;
    }

    @Override
    public String getName() { // get the name
        return name;
    }

    public boolean isWhite() { // check if a piece is white or not
        return color.equals("White");
    }

    public TransformGroup getPositionTransform() { // get the position transformGroup
        return (TransformGroup) this.getChild(0);
    }

    public Vector3d getPosition() { // get the current postion of a piece in the universe
        Vector3d vector3d = new Vector3d();
        Transform3D tmp = new Transform3D();
        getPositionTransform().getTransform(tmp);
        tmp.get(vector3d);
        return vector3d;
    }

    public void setPosition(Vector3d position) { // set the position of a piece in the universe
        Transform3D position3D = new Transform3D();
        getPositionTransform().getTransform(position3D);
        position3D.setTranslation(position);
        getPositionTransform().setTransform(position3D);
    }

    public Shape3D getPiece() { // get the shape3D
        return (Shape3D) ((TransformGroup) getPositionTransform().getChild(0)).getChild(0);
    }

    public double getRotation() { // get the rotation of a piece
        return rotation;
    }

    public void oldPos() { // get the old position prior to moving
        oldPosition = getPosition();
    }

    public void resetPos() { // reset a position to it's old position prior to moving
        setPosition(oldPosition);
    }

    public void moveYPos(double amount) { // move a pieces y position up or down
        Vector3d v = getPosition();
        v.y += amount;
        setPosition(v);
    }

    public void setApp(String texture) {
        Appearance app = getPiece().getAppearance();
        app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        app.setMaterial(setMaterial(MONKEECHESS.White));

        ColoringAttributes ca = new ColoringAttributes(MONKEECHESS.Black, ColoringAttributes.SHADE_GOURAUD);
        ca.setColor(0, 0, 0);
        app.setColoringAttributes(ca);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(ta);

        setTexture(texture);
        getPiece().setAppearance(app);
    }

    public void setTexture(String name) {
        Texture t = ChessPieces.textures.get(name);
        getPiece().getAppearance().setTexture(t);
    }

    public void makePieceGreen() { // make a piece green (apply texture)
        setTexture(texture + "_green");
        changeHighlightColor(MONKEECHESS.Green);
    }

    public void makePieceRed() { // make a piece red (apply texture)
        setTexture(texture + "_red");
        changeHighlightColor(MONKEECHESS.Red);
    }

    public void makePieceNormal() { // make a piece back to normal
        setTexture(texture);
    }

    public void makeHighlight() { // make the highlight of a piece
        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.NORMALS | QuadArray.COORDINATES);
        quadArray.setCapability(QuadArray.ALLOW_COLOR_WRITE);
        float[][] coords = {{-1, 0, -1}, {-1, 0, 1}, {1, 0, 1}, {1, 0, -1}};
        float[] normal = {0, 1, 0};
        for (int i = 0; i < 4; i++) {
            quadArray.setCoordinate(i, coords[i]);
            quadArray.setNormal(i, normal);
            quadArray.setColor(i, MONKEECHESS.Green);
        }
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D highlightTransform = new Transform3D();
        getPositionTransform().getTransform(highlightTransform);
        Vector3d highlightPosition = new Vector3d();
        highlightPosition.y = 0.01 - getPosition().y;
        highlightTransform.setTranslation(highlightPosition);
        tg.setTransform(highlightTransform);

        Shape3D shape3d = new Shape3D(quadArray);
        shape3d.setCollidable(false);
        shape3d.setPickable(false);
        shape3d.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE | Shape3D.ALLOW_APPEARANCE_READ);
        tg.addChild(shape3d);

        bg.addChild(tg);
        getPositionTransform().addChild(bg);
        highlight = bg;
    }

    public void changeHighlightColor(Color3f color) { // change the highlight color when needed (hovering over invalid piece)
        if (highlight != null) {
            QuadArray quadArray = (QuadArray) ((Shape3D) ((TransformGroup) highlight.getChild(0)).getChild(0)).getGeometry();
            for (int i = 0; i < quadArray.getVertexCount(); i++) {
                quadArray.setColor(i, color);
            }
        }
    }

    public void removeHighlight() { // remove a highlight
        getPositionTransform().removeChild(1);
        highlight = null;
    }

    public boolean isSameSpot(){ // check if a piece was moved to the same spot
        if(oldPosition.z == getPosition().z && oldPosition.x == getPosition().x){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public static Material setMaterial(Color3f clr) {
        int SH = 100;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0, 0, 0));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }
}
