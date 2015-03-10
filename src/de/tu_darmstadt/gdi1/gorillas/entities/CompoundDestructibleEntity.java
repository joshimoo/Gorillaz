package de.tu_darmstadt.gdi1.gorillas.entities;

import eea.engine.component.Component;
import eea.engine.component.render.DestructionRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * A compound DestructibleEntity, takes care of all it's children
 */
public class CompoundDestructibleEntity extends DestructibleImageEntity {

    private DestructibleImageEntity[] children;
    public CompoundDestructibleEntity(String entityID, DestructibleImageEntity[] children) {
        super(entityID, null);
        this.children = children;
    }

    public int getWidth(int index) {
        if (children != null && index < children.length) { return (int) children[index].getSize().x; }
        else {return 0;}
    }

    public int getHeight(int index) {
        if (children != null && index < children.length) { return (int) children[index].getSize().y; }
        else {return 0;}
    }

    // TODO: think about creating a compound shape (polygon), based on the points of the underlying shapes
    public Shape[] getShapes() {
        Shape[] shapes = new Shape[children.length];
        for (int i = 0; i < children.length; i++) {
            shapes[i] = children[i].getShape();
        }

        return shapes;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics graphicsContext) {
        super.render(gc,sb,graphicsContext);
        for (DestructibleImageEntity c : children) { c.render(gc, sb, graphicsContext); }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc,sb,delta);
        for (DestructibleImageEntity c : children) { c.update(gc,sb,delta); }
    }


    @Override
    public boolean collides(float x, float y) {
        Boolean collides = false;
        for (DestructibleImageEntity c : children) { collides |= c.collides(x,y); }
        return collides;
    }

    @Override
    public void impactAt(Vector2f position) {
        // Only call this on children that actually are colliding
        for (DestructibleImageEntity c : children) {
            if (c.collides(position.x, position.y)) {
                c.impactAt(position);
            }
        }
    }



}
