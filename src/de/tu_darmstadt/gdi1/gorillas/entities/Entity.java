package de.tu_darmstadt.gdi1.gorillas.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Entity {
    float x;
    float y;

    public abstract void render(Graphics graph);
    public abstract void update(int delta);
}