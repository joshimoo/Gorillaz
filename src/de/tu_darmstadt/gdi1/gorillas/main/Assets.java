package de.tu_darmstadt.gdi1.gorillas.main;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Assets {

    /* Global Image Definitions */
    public static Image imgBackground   = loadImage(Gorillas.ASSETS + "background.png");
    public static Image imgBanana       = loadImage(Gorillas.ASSETS + "banana.png");
    public static Image imgSunSmile     = loadImage(Gorillas.ASSETS + "sun/sun_smiling.png");
    public static Image imgSumSuprise   = loadImage(Gorillas.ASSETS + "sun/sun_astonished.png");
    public static Image imgDestruction  = loadImage(Gorillas.ASSETS + "destruction.png");
    public static Image imgGorilla      = loadImage(Gorillas.ASSETS + "gorillas/gorilla.png");
    public static Image imgGorillaLeft  = loadImage(Gorillas.ASSETS + "gorillas/gorilla_left_up.png");
    public static Image imgGorillaRight = loadImage(Gorillas.ASSETS + "gorillas/gorilla_right_up.png");
    public static Image imgGorillaHit   = loadImage(Gorillas.ASSETS + "gorillaHit.png");



    public static Image loadImage(final String path){
        try {
            return new Image(path);
        } catch (SlickException e) {
            // TODO: Better handling here? Works for now.
            // Maybe fallback image, but we still need to load it.
            throw new RuntimeException(path + "not Found", e);
        }
    }

}
