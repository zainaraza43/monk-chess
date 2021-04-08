/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Collision.java
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

    public Piece(Obj3D piece, String name, String color, Vector3d position, float scale, double rotation, String texture) {
        piece.setPiece(this);
        this.name = name;
        this.color = color;
        this.texture = texture;
        this.highlight = null;
        this.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup positionTG = new TransformGroup();
        positionTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        positionTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        positionTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        TransformGroup scaledTG = new TransformGroup();
        scaledTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scaledTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

        Transform3D scalar = new Transform3D();
        Transform3D pos = new Transform3D();
        pos.setTranslation(position);
        scalar.rotY(rotation);
        scalar.setScale(scale);
        scaledTG.setTransform(scalar);

        positionTG.setTransform(pos);
        positionTG.addChild(scaledTG);
        piece.setName(name);
        piece.setUserData(0);
        scaledTG.addChild(piece);
        addChild(positionTG);

        setApp(texture);
        this.oldPosition = getPosition();
        sounds = MONKEECHESS.chessBoard.sounds;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isWhite() {
        return color.equals("White");
    }

    public TransformGroup getPositionTransform() {
        return (TransformGroup) this.getChild(0);
    }

    public Vector3d getPosition() {
        Vector3d vector3d = new Vector3d();
        Transform3D tmp = new Transform3D();
        getPositionTransform().getTransform(tmp);
        tmp.get(vector3d);
        return vector3d;
    }

    public void setPosition(Vector3d position) {
        Transform3D position3D = new Transform3D();
        getPositionTransform().getTransform(position3D);
        position3D.setTranslation(position);
        getPositionTransform().setTransform(position3D);
    }

    public Shape3D getPiece() {
        return (Shape3D) ((TransformGroup) getPositionTransform().getChild(0)).getChild(0);
    }

    public void oldPos() {
        oldPosition = getPosition();
    }

    public void resetPos() {
        setPosition(oldPosition);
    }

    public void moveYPos(double amount) {
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

    public void makePieceGreen() {
        setTexture(texture + "_green");
        changeHighlightColor(MONKEECHESS.Green);
    }

    public void makePieceRed() {
        setTexture(texture + "_red");
        changeHighlightColor(MONKEECHESS.Red);
    }

    public void makePieceNormal() {
        setTexture(texture);
        removeHighlight();
    }


    public void makeHighlight() {
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

    public void changeHighlightColor(Color3f color) {
        if (highlight != null) {
            QuadArray quadArray = (QuadArray) ((Shape3D)((TransformGroup) highlight.getChild(0)).getChild(0)).getGeometry();
            for (int i = 0; i < quadArray.getVertexCount(); i++) {
                quadArray.setColor(i, color);
            }
        }
    }

    public void removeHighlight() {
        getPositionTransform().removeChild(1);
        highlight = null;
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
