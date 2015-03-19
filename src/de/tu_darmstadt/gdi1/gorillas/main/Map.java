package de.tu_darmstadt.gdi1.gorillas.main;

import org.newdawn.slick.geom.Vector2f;
import java.util.ArrayList;
import java.util.Random;


/**
 * Contains all the positional Data for our Map
 * TODO: normally you might consider moving the skyline and gorilla into here, but no time.
 */
public class Map {
    // The default gorrila size thing, is a little unclean but no time
    public static final int defaultGorillaWidth = 37;
    public static final int defaultGorillaHeight = 42;

    // public immutable fields, no boilerplate needed
    private final int width;
    private final int height;
    private final int gorillaWidth;
    private final int gorillaHeight;

    // If you want to guarantee immutability for reference types
    // add getters that return copies of these.
    private final ArrayList<Vector2f> buildings;
    private final Vector2f leftGorillaCoordinate;
    private final Vector2f rightGorillaCoordinate;


    private Map(int width, int height, int yOffsetCity, ArrayList<Vector2f> buildings, Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate) {
        // In Test Mode we have a fixed size for the gorillas, unless a specific size is provided.
        this(width, height, yOffsetCity, buildings, leftGorillaCoordinate, rightGorillaCoordinate, defaultGorillaWidth, defaultGorillaHeight);
    }

    private Map(int width, int height, int yOffsetCity, ArrayList<Vector2f> buildings, Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate, int gorillaWidth, int gorillaHeight) {
        this.width = width;
        this.height = height;
        this.buildings = buildings;
        this.leftGorillaCoordinate = leftGorillaCoordinate;
        this.rightGorillaCoordinate = rightGorillaCoordinate;
        this.gorillaWidth = gorillaWidth;
        this.gorillaHeight = gorillaHeight;
    }

    /**
     * This method should create a new RANDOM map. The map should consist of the
     * coordinates of the left and the right gorilla and the upper left edges of
     * the buildings of the skyline. The gorilla positions should mark the
     * center of each gorilla. The buildings of the skyline are rectangular, and
     * not higher than <code>frameHeight</code> minus 100.
     * <p>
     * Important: The y coordinate of the gorillas and the buildings should be
     * denoted with the y axis pointing downwards, as slick demands this.
     * <p>
     * The left gorilla has to be placed on the first, second or third building
     * from the left. Accordingly, the right gorilla has to be placed on the
     * first, second or third building from the right.
     * <p>
     * Gorillas should be placed in the middle of a building and stand on the
     * building.
     *
     * @param frameWidth  the width of the frame/screen of the gorillas game
     * @param frameHeight the height of the frame/screen of the gorillas game
     */
    public static Map createRandomMap(int frameWidth, int frameHeight, int gorillaWidth, int gorillaHeight) {

        // Random Values
        Random r = new Random();
        // TODO: I like random building count, what do you guys think?
        int BUILD_COUNT = 6 + r.nextInt(6); // min 6, default was 8
        // int BUILD_COUNT = 8; // min 6, default was 8
        // int BUILD_COUNT = Game.getInstance().isTestMode() ? 6 : 6 + r.nextInt(6);
        float BUILD_WIDTH = (float)frameWidth / BUILD_COUNT;
        ArrayList<Vector2f> buildingCoordinates = new ArrayList<Vector2f>(BUILD_COUNT);

        /** Generate Buildings */
        // The buildings of the skyline are rectangular, and
        // not higher than frameHeight - 100.
        for (int i = 0; i < BUILD_COUNT; i++) {
            buildingCoordinates.add(new Vector2f( i * BUILD_WIDTH, 100 + r.nextInt(frameHeight - 100) ));
        }

        /** Position Gorillas */
        // The left gorilla has to be placed on the first,
        // second or third building from the left.
        int buildingIndex = r.nextInt(3);
        Vector2f leftGorillaCoordinate = new Vector2f(
            buildingCoordinates.get(buildingIndex).x + BUILD_WIDTH * 0.5f,
            buildingCoordinates.get(buildingIndex).y - gorillaHeight * 0.5f
        );

        // The right gorilla has to be placed on the first,
        // second or third building from the right.
        buildingIndex = (BUILD_COUNT - 1) - r.nextInt(3);
        Vector2f rightGorillaCoordinate = new Vector2f(
            buildingCoordinates.get(buildingIndex).x + BUILD_WIDTH * 0.5f,
            buildingCoordinates.get(buildingIndex).y - gorillaHeight * 0.5f
        );

        return new Map(frameWidth, frameHeight, 0, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate, gorillaWidth, gorillaHeight);
    }

    /**
     * creates a map, which is NOT RANDOM based on the given parameters
     *
     * @param frameWidth             the width of the frame/window/pane of the game
     * @param frameHeight            the height of the frame/window/pane of the game
     * @param yOffsetCity            the top y offset of the city
     * @param buildingCoordinates    the building coordinates of the city skyline
     * @param leftGorillaCoordinate  the coordinate of the left gorilla
     * @param rightGorillaCoordinate the coordinate of the right gorilla
     */
    public static Map createMap(int frameWidth, int frameHeight, int yOffsetCity, ArrayList<Vector2f> buildingCoordinates, Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate) {
        return new Map(frameWidth, frameHeight, yOffsetCity, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
    }

    // Getters
    public int getMapFrameWidth() { return width; }
    public int getMapFrameHeight() { return height; }
    public int getGorillaHeight() { return gorillaHeight; }
    public int getGorillaWidth() { return gorillaWidth; }

    /**
     * @return all the upper left corner's coordinates of the buildings of the
     * current map, ordered from left to right
     */
    public ArrayList<Vector2f> getBuildings() {
        return buildings;
    }

    /** @return the center coordinate of the left gorilla */
    public Vector2f getLeftGorillaCoordinate() { return leftGorillaCoordinate; }

    /** @return the center coordinate of the right gorilla */
    public Vector2f getRightGorillaCoordinate() { return rightGorillaCoordinate; }
}
