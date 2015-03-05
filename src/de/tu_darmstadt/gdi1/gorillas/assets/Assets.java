package de.tu_darmstadt.gdi1.gorillas.assets;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.EnumMap;
import java.util.Map;

/**
 * The Asset Class is responsible for all Asset Loading/Unloading
 * It will Lazily load and Cache all requested Assets
 */
public final class Assets {

    private static final String IMAGE_PATH   = "/assets/img/";
    private static final String SOUND_PATH   = "/assets/snd/";

    private static Image ERROR_IMAGE;
    private static Sound ERROR_SOUND;

    // Asset Collections, so beautiful *-*
    private static Map<Images, Image> images = new EnumMap<>(Images.class);
    private static Map<Sounds, Sound> sounds = new EnumMap<>(Sounds.class);

    /** This class should never be instantiated */ private Assets() {}

    /**
     * Generate an Error Image in Memory, since this will always proceed.
     * @return the generated ERROR_IMAGE or the cached version
     */
    private static Image createErrorImage() {
        if (ERROR_IMAGE == null){
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
    public static Image loadImage(final Images id) {
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
    public static Image loadUniqueImage(final Images id) {
        return loadImage(id).copy();
    }

    /** Generate an Sound in Memory, since this will always proceed.
     * @return the generated ERROR_SOUND or the cached version
     */
    private static Sound createErrorSound(){
        if(ERROR_SOUND == null){
            ERROR_SOUND = null; // FIXME: ;(
        }
        return ERROR_SOUND;
    }

    /** Load the Soundfile File from Disk
     * @param path path to the soundfile
     * @return the loaded Sound or a default error Sound
     */
    private static Sound loadSound(final String path){
        try {
            return new Sound(path);
        } catch (SlickException e) {
            System.err.println("Missing Sound: " + path);
            return createErrorSound();
        }
    }

    /** Lazily load and cache all sounds
     * Future calls will use the cached sound.
     *
     * @param id The Sound to load (Assets.Sounds)
     * @return The requested Sound or a default error Sound
     */
    public static Sound loadSound(final Sounds id){
        if (!sounds.containsKey(id)) {
            sounds.put(id, loadSound(id.getPath()));
        }
        return sounds.get(id);
    }

    /*********************************************************************************************/

    /**
     * Contains all Image Assets that are used by the game.
     * Order is not important, but it looks nicer when related assets are together.
     */
    public static enum Images {
        MAINMENU_BACKGROUND("background.png"),
        GAMEPLAY_BACKGROUND("background.png"),

        SUN_SMILING("sun/sun_smiling.png"),
        SUN_ASTONISHED("sun/sun_astonished.png"),

        GORRILA("gorillas/gorilla.png"),
        GORRILA_LEFT("gorillas/gorilla_left_up.png"),
        GORRILA_RIGHT("gorillas/gorilla_right_up.png"),
        GORRILA_HIT("gorillaHit.png"),

        BANANA("banana.png"),
        SNICKERS("snickers.png"),
        DESTRUCTION_OVERLAY("destruction.png"),
        ARROW("arrow.png"),
        CLOUD("cloud.png");

        private final String path;
        public String getPath() { return path; }
        Images(String path) { this.path = IMAGE_PATH + path; }
    }

    /** Contains all Soundfiles used in this Game */
    public static enum Sounds {
        EXPLOSION("explosion.ogg");

        private final String path;
        public String getPath() { return path; }
        Sounds(String path) { this.path = SOUND_PATH + path; }
    }

    /*********************************************************************************************/
}
