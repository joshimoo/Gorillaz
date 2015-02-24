package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Gorilla extends Entity {

    /* How fast should the Animation be */
    public static final float ANIMATION_SPEED = 1.0f;
    /* How long a single frame should be displayed [ms] */
    public static final int   FRAME_LENGTH    = 1000;

    private Animation animation;
    private Shape collMask;

    public Gorilla(int x, int y) {
        this.x = x;
        this.y = y;
        animation = new Animation();
        animation.setLooping(true);
        animation.setSpeed(ANIMATION_SPEED);
        animation.addFrame(Assets.loadImage(Assets.Images.GORRILA),       FRAME_LENGTH);
        animation.addFrame(Assets.loadImage(Assets.Images.GORRILA_LEFT),  FRAME_LENGTH);
        animation.addFrame(Assets.loadImage(Assets.Images.GORRILA_RIGHT), FRAME_LENGTH);
        collMask = new Rectangle(x - getWidth()/2, y-getHeight(), getWidth(), getHeight());
    }

    @Override
    public boolean isCollidding(Banana b) {
        return collMask.intersects(b.getColMask());
    }

    @Override
    public void render(Graphics graph) {
        graph.drawImage(animation.getCurrentFrame(), x - getHeight() / 2, y - getHeight());
        graph.draw(collMask);
    }

    @Override
    public void update(int delta) {
        animation.update(delta);
    }

    public Image getImage() {
        return animation.getCurrentFrame();
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

}
