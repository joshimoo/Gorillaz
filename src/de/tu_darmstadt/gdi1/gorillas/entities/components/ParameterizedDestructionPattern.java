package de.tu_darmstadt.gdi1.gorillas.entities.components;

import eea.engine.interfaces.IDestructionPattern;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class provides a destruction pattern defined by alpha buffer,
 * during destruction only the alpha value of the target is changed.
 * @deprecated Use ShapeDestructionPattern instead
 * @author Joshua Moody (joshua.moody@stud.tu-darmstadt.de)
 */
@Deprecated
public class ParameterizedDestructionPattern implements IDestructionPattern {

    private int[][] buffer;
    private int width;
    private int height;

    /**
     * Creates a Destruction Pattern based on the passed color buffer
     * The underlying colors (int) need to be in ARGB format
     * @param colors needs to be [y][x] indexed and in ARGB format
     */
    @Deprecated
    protected ParameterizedDestructionPattern(int[][] colors) {
        // TODO: Think about whether we actually want color destruction
        // TODO: if we decide against color destruction change the buffer to only hold an alphamap
        // Maybe for toxic floors. then we can do damage calculation based on the color component
        // Oh the possibilities, but I will probably change this to only work with alphas :)
        this.buffer = colors;

        // Cache Params since the buffer is immutable
        height = buffer.length;
        width = buffer[0].length;
    }

    /**
     * Creates a Destruction Pattern based on the passed alpha buffer
     * This allows you to only change the alpha component of the target during a destruction
     * @param alphas needs to be [y][x] indexed
     */
    public ParameterizedDestructionPattern(byte[][] alphas) {
        this.buffer = new int[alphas.length][alphas[0].length];
        for (int y = 0; y < alphas.length; y++) {
            for (int x = 0; x < alphas[0].length; x++) {
                // since buffer is in ARGB format, shift the alpha component
                buffer[y][x] = alphas[y][x] << 24;
            }
        }

        // Cache Params since the buffer is immutable
        height = buffer.length;
        width = buffer[0].length;
    }

    /**
     * @return The width of the bounding box around the destruction.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the bounding box around the destruction.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @return The center of the destruction.
     */
    @Override
    public Vector2f getCenter() {
        return new Vector2f(width / 2, height / 2);
    }

    /**
     * Calculates the color of a pixel after the destruction.
     *
     * @param color The color before the destruction. Color format must be ARGB.
     * @param x     The x-coordinate of the pixel relative to the upper left
     *              corner of the bounding box around the pattern.
     * @param y     The y-coordinate of the pixel relative to the upper left
     *              corner of the bounding box around the pattern.
     * @return The color after the destruction. Color format: ARGB.
     */
    @Override
    public int getModifiedColor(int color, int x, int y) {
        try {
            int patternAlpha = buffer[y][x] >>> 24;
            int oldAlpha = (color & 0xFF000000) >>> 24; // extract alpha byte

            // TODO: Add color destruction
            int rgb = color & 0x00FFFFFF; // extract color
            int newAlpha = (oldAlpha * patternAlpha) / 255;
            return (newAlpha << 24) | rgb; // assemble new color

        } catch (Exception e) {
            return color; // in case of an error, modify nothing
        }
    }
}
