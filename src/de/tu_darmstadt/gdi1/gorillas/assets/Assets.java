package de.tu_darmstadt.gdi1.gorillas.assets;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import java.util.EnumMap;
import java.util.Map;

/**
 * The Asset Class is responsible for all Asset Loading/Unloading
 * It will Lazily load and Cache all requested Assets
 *
 * TODO: Do the same processing/handling for sounds
 */
public final class Assets {
    private static final String ASSETS   = "/assets/gorillas/";
    private static Image ERROR_IMAGE;

    // Asset Collections
    private static Map<Images, Image> images = new EnumMap<Images, Image>(Images.class);


    /**
     * This class should never be instantiated
     */
    private Assets() {}

    /**
     * Generate an Error Image in Memory, since this will always proceed.
     * @return the generated ERROR_IMAGE or the cached version
     */
    private static Image createErrorImage() {
        if (ERROR_IMAGE == null)
        {
            ImageBuffer buf = new ImageBuffer(32, 32);

            // Flood Fill Magenta
            for (int y = 0; y < buf.getHeight(); y++) {
                for (int x = 0; x < buf.getWidth(); x++) {
                    buf.setRGBA(x, y, 255, 0, 255, 255);
                }
            }

            // Draw an Black Outline 2 px wide
            for (int y = 0; y < buf.getHeight(); y++) {
                buf.setRGBA(0, y, 255, 255, 255, 255);
                buf.setRGBA(1, y, 255, 255, 255, 255);
                buf.setRGBA(buf.getWidth() - 1, y, 255, 255, 255, 255);
                buf.setRGBA(buf.getWidth() - 2, y, 255, 255, 255, 255);
            }

            // Cache the generated Image
            ERROR_IMAGE = buf.getImage();
        }

        return ERROR_IMAGE;
    }

    /**
     * Load the Image File from Disk
     * @param path path to the image
     * @return the image or default error image
     */
    private static Image loadImage(final String path){
        try {
            return new Image(path);
        } catch (SlickException e) {
            System.err.println("Missing Image: " + path);
            return createErrorImage();
        }
    }

    /**
     * Lazily Load and cache all images
     * Future calls will use the cached image.
     * The returned Image is immutable,
     * if you require a copy, use LoadUniqueImage
     *
     * @param id The image to load
     * @return The requested image or a default error image
     */
    public static Image loadImage(Images id) {
        if (!images.containsKey(id)) {
            images.put(id, loadImage(id.getPath()));
        }

        return images.get(id);
    }

    /**
     * Returns a copy of the requested image
     * @param id The image to load
     * @return a copy of the requested image or a copy of the default error image
     */
    public static Image loadUniqueImage(Images id) {
        return loadImage(id).copy();
    }


    /**
     * Contains all Image Assets that are used by the game.
     * Order is not important, but it looks nicer when related assets are together.
     */
    public static enum Images {
        // Menu
        MAINMENU_BACKGROUND(ASSETS + "background.png"),
        SETUPMENU_BACKGROUND(ASSETS + "menusetup.png"),
        GAMEPLAY_BACKGROUND(ASSETS + "background.png"),

        // Sun
        SUN_SMILING(ASSETS + "sun/sun_smiling.png"),
        SUN_ASTONISHED(ASSETS + "sun/sun_astonished.png"),

        // Gorillas
        GORRILA(ASSETS + "gorillas/gorilla.png"),
        GORRILA_LEFT(ASSETS + "gorillas/gorilla_left_up.png"),
        GORRILA_RIGHT(ASSETS + "gorillas/gorilla_right_up.png"),
        GORRILA_HIT(ASSETS + "gorillaHit.png"),

        // Projectiles
        BANANA(ASSETS + "banana.png"),
        SNICKERS(ASSETS + "snickers.png"),
        DESTRUCTION_OVERLAY(ASSETS + "destruction.png");


        private final String path;
        public String getPath() { return path; }
        Images(String path) { this.path = path; }
    }

}
