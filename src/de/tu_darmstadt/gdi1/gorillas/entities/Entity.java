package de.tu_darmstadt.gdi1.gorillas.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public abstract class Entity {
    public float x;
    public float y;

    public abstract void render(Graphics graph);
    public abstract void update(int delta);
    public abstract boolean isCollidding(Banana b);

}
