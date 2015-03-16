package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Skyline extends Entity {

    public final int BUILD_COUNT;
    public final int BUILD_WIDTH;
    public final Skyscraper[] skyscrapers;

    public Skyline(final int n){
        super("Skyline");
        BUILD_COUNT = n;
        BUILD_WIDTH = 1024 / BUILD_COUNT;
        skyscrapers = new Skyscraper[BUILD_COUNT];
        for(int i = 0; i < BUILD_COUNT; i++)
            skyscrapers[i] = new Skyscraper(i, BUILD_WIDTH);
    }

    public void destroy(final int x, final int y, final int pow){
        int hi = x + pow;   /* Boundscheck so explosions carry over buildings */
        int lo = x - pow;
        for(int i = (lo / BUILD_WIDTH ) ; i <= (hi / BUILD_WIDTH); ++i)
            if(i >= 0 && i < BUILD_COUNT) skyscrapers[i].destroy(x, y, pow);
    }

    public int getHeight(final int n){
        return skyscrapers[n].getHeight();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics graph) {
        super.render(gc, sb, graph);
        for(Skyscraper s : skyscrapers)
            s.render(gc, sb, graph);
    }

    @Override
    public boolean collides(Entity otherEntity) {
        boolean a = false;
        for(Skyscraper s: skyscrapers)
            a |= s.collides(otherEntity);
        return a;
    }

    @Deprecated
    public boolean isCollidding(Banana b) {
        return collides(b);
    }
}
