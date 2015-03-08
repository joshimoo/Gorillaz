package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public class Banana extends Entity {

    private static final float WSCALE = 0.6f;
    private float gravity   = 9.80665f;
    public static final float SPEED_MOD = 0.8f;
    private Image img;
    private float rotationSpeed, t;
    private double vx, vy, x0, y0, speed;
    private int windSpeed, angle;

    public Banana(final float x, final float y, final int angle, final int speed, float g, int w){
        img = Assets.loadUniqueImage(Assets.Images.BANANA);
        img.setCenterOfRotation(img.getHeight()/ 2, img.getWidth() / 2);
        rotationSpeed = (speed * 0.02f) * 360f / 1000f;
        x0 = x;
        y0 = y;
        t  = 0;
        this.x = (float) x0;
        this.y = (float) y0;
        gravity = g;
        this.windSpeed = w;
        this.speed = speed;
        this.angle = angle;

        if(angle > 90) rotationSpeed = -rotationSpeed;
    }

    public float getCenterX(){ return x + img.getCenterOfRotationX(); }
    public float getCenterY(){ return y + img.getCenterOfRotationY(); }

    public Shape getColMask(){ return new Circle(getCenterX(), getCenterY(), 10); }


    @Override
    public void render(Graphics graph) {
        graph.drawImage(img, x, y);
        if(Gorillas.debug) graph.draw(getColMask());
    }

    @Override
    public void update(int delta) {
        /* Let the Banana rotate over time */
        img.rotate(rotationSpeed * delta);
        /* Move the Banana */
        t = t + delta / 400f;
        if((y + img.getHeight() >= Gorillas.FRAME_HEIGHT)& (vx > 5 | vx < -5)){
            if(vx > 5) angle = -angle;
            if(vx < 5) angle = 180 - angle;
            y0 = Gorillas.FRAME_HEIGHT - (img.getHeight() + 10);
            x0 = x;
            speed = vx;
            t = delta/ 400f;
        }

        vx = Math.cos(Math.toRadians(angle)) * speed * SPEED_MOD;
        vy = Math.sin(Math.toRadians(angle)) * speed * SPEED_MOD;
        x = (float) (x0 + (vx * t) + (WSCALE /2 * windSpeed * t * t));
        y = (float) (y0 - (vy * t) + ( gravity /2 * t * t));
    }

    /* Inception sound Playing :D */  @Override public boolean isCollidding(Banana b){return false;}

}
