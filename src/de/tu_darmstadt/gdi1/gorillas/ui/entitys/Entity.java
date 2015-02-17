package de.tu_darmstadt.gdi1.gorillas.ui.entitys;

import org.newdawn.slick.Graphics;

public abstract class Entity {
    float x;
    float y;

    public abstract void render(Graphics graph);
    public abstract void update(int delta);
}
