package de.tu_darmstadt.gdi1.gorillas.entities.components;

import eea.engine.interfaces.IDestructionPattern;
import org.newdawn.slick.geom.*;

/**
 * This class provides a destruction pattern defined by a Slick2D Shape,
 * during destruction only the alpha value of the target is changed.
 * @author Joshua Moody (joshua.moody@stud.tu-darmstadt.de)
 */
public class ShapeDestructionPattern implements IDestructionPattern {

    private int alpha = 0;
    private Shape shape;

    // Wrapper methods, for default shapes
    public static ShapeDestructionPattern createSquareDestructionPattern(float width, float height) {
        return new ShapeDestructionPattern(new Rectangle(0, 0, width, height));
    }
    public static ShapeDestructionPattern createCircleDestructionPattern(float radius) {
        return new ShapeDestructionPattern(new Circle(0, 0, radius ));
    }
    public static ShapeDestructionPattern createElipseDestructionPattern(float horizontalRadius, float verticalRadius) {
        return new ShapeDestructionPattern(new Ellipse(0, 0, horizontalRadius, verticalRadius));
    }
    public static ShapeDestructionPattern createPolygonDestructionPattern(Vector2f... points) {
        Polygon poly = new Polygon();
        for (Vector2f point : points) { poly.addPoint(point.x, point.y); }
        return new ShapeDestructionPattern(poly);
    }

    protected ShapeDestructionPattern(Shape shape) {
        this.shape = shape;
    }


    /**
     * @param alpha value in the range [0, 255]
     * where 0 fully transparent and 255 fully opaque
     */
    public void setAlpha(int alpha) { this.alpha = Math.max(0, Math.min(255, alpha)); }
    public int getAlpha() { return alpha; }


    /**
     * @return The width of the bounding box around the destruction.
     */
    @Override
    public int getWidth() {
        return (int) shape.getWidth();
    }

    /**
     * @return The height of the bounding box around the destruction.
     */
    @Override
    public int getHeight() {
        return (int) shape.getHeight();
    }

    /**
     * @return The center of the destruction. Note: The center of the
     * destruction is not necessarily the same as the center of the
     * bounding box around the pattern.
     */
    @Override
    public Vector2f getCenter() {
        return new Vector2f(shape.getCenterX(), shape.getCenterY());
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
        // TODO: consider alphablending, instead of just setting the alpha.
        return shape.contains(x, y)? (alpha << 24 | (color & 0x00FFFF)) : color;
    }
}
