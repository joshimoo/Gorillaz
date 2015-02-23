package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Banana extends Entity {

    public static final float GRAVITY   = 9.80665f;
    public static final float SPEED_MOD = 0.8f;
    private Image img;
    private float rotationSpeed, t;
    private final double vx, vy, x0, y0;

    public Banana(final float x, final float y, final int angle, final int speed){
        img = Assets.loadUniqueImage(Assets.Images.BANANA);
        img.setCenterOfRotation(img.getHeight()/ 2, img.getWidth() / 2);
        rotationSpeed = (speed * 0.02f) * 360f / 1000f;
        vx = Math.cos(Math.toRadians(angle)) * speed * SPEED_MOD;
        vy = Math.sin(Math.toRadians(angle)) * speed * SPEED_MOD;
        x0 = x;
        y0 = y;
        t  = 0;
        this.x = (float) x0;
        this.y = (float) y0;
    }

    @Override
    public void render(Graphics graph) {
        graph.drawImage(img, x, y);
    }

    @Override
    public void update(int delta) {
        /* Let the Banana rotate over time */
        img.rotate(rotationSpeed * delta);
        /* Move the Banana */
        t = t + delta / 400f;
        x = (float) (x0 + (vx * t));
        y = (float) (y0 - (vy * t) + ( GRAVITY /2 * t * t) ) ;
    }

}
