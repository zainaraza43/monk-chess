package com.Main;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

public class Piece extends BranchGroup {
    private String name, color;
    private Vector3d oldPosition;

    public Piece(Obj3D piece, String name, String color, Vector3d position, float scale, double rotation, String texture) {
        piece.setPiece(this);
        this.name = name;
        this.color = color;
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
        setApp(piece, texture);
        piece.setName(name);
        piece.setUserData(0);
        scaledTG.addChild(piece);
        addChild(positionTG);
        this.oldPosition = getPosition();
    }

    public String getColor() {
        return color;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isWhite(){
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

    public void oldPos(){
        oldPosition = getPosition();
    }

    public Vector3d getOldPosition() {
        return oldPosition;
    }

    public void resetPos(){
        setPosition(oldPosition);
    }

    public static void setApp(Shape3D piece, String texture) {
        Appearance appearance = new Appearance();
        appearance.setMaterial(setMaterial(MONKEECHESS.White));

        ColoringAttributes ca = new ColoringAttributes(MONKEECHESS.Black, ColoringAttributes.SHADE_GOURAUD);
        ca.setColor(0, 0, 0);
        appearance.setColoringAttributes(ca);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        appearance.setTextureAttributes(ta);

        appearance.setTexture(setTexture(texture));
        piece.setAppearance(appearance);
    }

    public static Texture setTexture(String name) {
        TextureLoader loader = new TextureLoader("Assets/Textures/" + name + ".jpg", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if (imageComponent2D == null) { // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        texture2D.setEnable(true);
        return texture2D; // return the texture with the image
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
