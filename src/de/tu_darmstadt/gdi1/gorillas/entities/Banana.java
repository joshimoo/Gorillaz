package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

public class Banana extends Entity {

    private float gravity   = 9.80665f;
    public static final float SPEED_MOD = 0.8f;
    private float rotationSpeed, t;
    private float vx, vy, speed;
    private int windAcceleration, angle;

    private Vector2f pos0;

    public Banana(Vector2f pos, final int angle, final int speed, float g, int w) {
        super("Banana");

        // Rendering
        addComponent(new ImageRenderComponent(Assets.loadImage(Assets.Images.BANANA)));
        rotationSpeed = (speed * 0.02f) * 360f / 1000f;

        // Flight Params
        setPosition(pos);
        pos0 = new Vector2f(pos.x, pos.y);
        t  = 0;
        gravity = g;
        windAcceleration = w;
        this.speed = speed;
        this.angle = angle;

        if(angle > 90) rotationSpeed = -rotationSpeed;
    }

    @Deprecated
    public Banana(final float x, final float y, final int angle, final int speed, float g, int w){
        this(new Vector2f(x, y), angle, speed, g, w);
    }

    @Override
    public Shape getShape() {
        return new Circle(getPosition().x, getPosition().y, getSize().y / 2);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);

        /* Let the Banana rotate over time */
        setRotation(getRotation() + rotationSpeed * delta);

        Vector2f pos = getPosition();
        /* Move the Banana */
        t = t + delta * Game.getTimeScale();
        if((pos.y + getSize().y / 2 >= Gorillas.FRAME_HEIGHT)& (vx > 5 | vx < -5)){
            if(vx > 5) angle = -angle;
            if(vx < 5) angle = 180 - angle;

            pos0 = new Vector2f(pos.x, gc.getHeight() - (getSize().y + 10) / 2);
            speed = vx;
            t = delta * Game.getTimeScale();
        }

        vx = (float) Math.cos(Math.toRadians(angle)) * speed * SPEED_MOD;
        vy = (float) Math.sin(Math.toRadians(angle)) * speed * SPEED_MOD;
        pos.x = (float) (pos0.x + (vx * t) + ( windAcceleration /2 * Game.getWindScale() * t * t));
        pos.y = (float) (pos0.y - (vy * t) + ( gravity /2 * t * t));
        setPosition(pos);
    }
}
