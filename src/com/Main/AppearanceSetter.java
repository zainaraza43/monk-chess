/*
 * Java Class to set the Appearance, Texture, and the material of an object or shape
 */

package com.Main;

import com.jogamp.opengl.util.texture.TextureCoords;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.vecmath.Vector4f;

public class AppearanceSetter{

    public static Texture setTexture(String name){
        TextureLoader loader = new TextureLoader("Assets/" + name + ".jpg", null); // load in the image
        ImageComponent2D imageComponent2D = loader.getImage(); //get image
        if(imageComponent2D == null){ // if image is not found
            System.out.println("Error opening image");
        }

        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D); //set the image on the texture
        return texture2D; // return the texture with the image
    }

    public static Appearance texturedApp(String name){
        Appearance appearance = new Appearance();
        appearance.setTexture(setTexture(name));

        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
        appearance.setPolygonAttributes(polygonAttributes);

        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.REPLACE);
        appearance.setTextureAttributes(ta);

        int angle = 0;
        float scale = 4f;
        Transform3D td = new Transform3D();
        td.rotZ((angle / 90.0f) * Math.PI / 2);
        td.setScale(scale);
        ta.setTextureTransform(td);
        return appearance;
    }

    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f*clr.x, 0.6f*clr.y, 0.6f*clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0, 0, 0));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    public static TransparencyAttributes setTransparency(int mode, float value){
       TransparencyAttributes transparencyAttributes = new TransparencyAttributes(mode, value);
       return transparencyAttributes;
    }
}
