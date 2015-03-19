package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Skyline extends Entity {

    public final int BUILD_COUNT;
    public final int BUILD_WIDTH;
    public final Skyscraper[] skyscrapers;

    /** Build a Skyline with the given coords and the mapSize */
    public Skyline(ArrayList<Vector2f> buildingCoords, int mapWidth, int mapHeight) {
        super("Skyline");
        BUILD_COUNT = buildingCoords.size();
        BUILD_WIDTH = mapWidth / BUILD_COUNT;
        skyscrapers = new Skyscraper[buildingCoords.size()];
        for (int i = 0; i < buildingCoords.size(); i++) {
            skyscrapers[i] = new Skyscraper(buildingCoords.get(i), BUILD_WIDTH, mapWidth, mapHeight);
        }
    }

    /** Procedurally destroy parts of the skyline
     * @param x, y - center of the destruction circle
     * @param radius - radius of the destruction circle
     */
    public void destroy(final int x, final int y, final int radius){
        int hi = x + radius;   /* Boundscheck so explosions carry over buildings */
        int lo = x - radius;
        for(int i = (lo / BUILD_WIDTH ) ; i <= (hi / BUILD_WIDTH); ++i)
            if(i >= 0 && i < BUILD_COUNT) skyscrapers[i].destroy(x, y, radius);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics graph) {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
        super.render(gc, sb, graph);
        for(Skyscraper s : skyscrapers) { s.render(gc, sb, graph); }
    }

    @Override
    public boolean collides(Entity otherEntity) {
        // Exit early if we have a collision
        for(Skyscraper s: skyscrapers) { if(s.collides(otherEntity)) { return true; } }
        return false;
    }

}
