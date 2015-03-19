package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

public class Banana extends Entity {

    private float gravity   = 9.80665f;
    private float rotationSpeed, time;
    private float vx, vy, speed;
    private int windAcceleration, angle;

    private Vector2f pos0;

    public Banana(Vector2f pos, final int angle, final int speed, float gravity, int wind) {
        super("Banana");

        if (!Game.getInstance().isTestMode()) {
            // Rendering
            Image img;
            if(Math.random() < 0.2f){
               if(Math.random() < 0.25f)  img = Assets.loadImage(Assets.Images.SNICKERS);
               else if(Math.random() < 0.5f) img = Assets.loadImage(Assets.Images.PINEAPPLE);
               else img = Assets.loadImage(Assets.Images.COCONUT);
            }
            else img = Assets.loadImage(Assets.Images.BANANA);

            float scale = Game.BANANA_SIZE / img.getWidth();
            if(scale != 1f) img.getScaledCopy(scale);
            addComponent(new ImageRenderComponent(img));
        } else {
            // In Test Mode the banana should be 10x10 px
            setSize(new Vector2f(10, 10));
        }

        rotationSpeed = (speed * 0.02f) * 360f / 1000f;

        // Flight Params
        setPosition(pos);
        pos0 = new Vector2f(pos.x, pos.y);
        time = 0;
        this.gravity = gravity;
        windAcceleration = wind;
        this.speed = speed;
        this.angle = angle;

        if(angle > 90) rotationSpeed = -rotationSpeed;
    }

    @Override
    public Shape getShape() {
        if(Game.getInstance().isTestMode())
            return super.getShape();
        else
            return new Circle(getPosition().x, getPosition().y, getSize().y / 2);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);

        /* Let the Banana rotate over time */
        setRotation(getRotation() + rotationSpeed * delta);

        Vector2f pos = getPosition();
        /* Move the Banana */
        time = time + delta * Game.getInstance().getTimeScale();
        //Dozen
        if((pos.y + getSize().y / 2 >= Gorillas.FRAME_HEIGHT)& (vx > 5 | vx < -5)){
            if(vx > 5) angle = -angle;
            if(vx < 5) angle = 180 - angle;
            pos0 = new Vector2f(pos.x, gc.getHeight() - (getSize().y + 10) / 2);
            speed = vx;
            time = delta * Game.getInstance().getTimeScale();
        }

        vx = (float) Math.cos(Math.toRadians(angle)) * speed;
        vy = (float) Math.sin(Math.toRadians(angle)) * speed;
        pos.x = (pos0.x + (vx * time) + ( windAcceleration /2f * Game.getInstance().getWindScale() * time * time));
        pos.y = (pos0.y - (vy * time) + ( gravity /2f * time * time));
        setPosition(pos);
    }
}
