package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.component.Component;
import eea.engine.component.render.AnimationRenderComponent;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Gorilla extends Entity {

    /** How fast should the Animation be */
    private static final float ANIMATION_SPEED = 1.0f;
    /** How long a single frame should be displayed [ms] */
    private static final int   FRAME_LENGTH    = 1000;
    private Component component;

    /**
     * Create a new Gorilla
     * @param pos the gorillas center position
     */
    public Gorilla(Vector2f pos) {
        super("Gorilla");

        if (!Game.getInstance().isTestMode()) {
            // Rendering
            Image[] frames = new Image[] {
                    Assets.loadImage(Assets.Images.GORRILA_LEFT),
                    Assets.loadImage(Assets.Images.GORRILA),
                    Assets.loadImage(Assets.Images.GORRILA_RIGHT)
            };
            component = new AnimationRenderComponent(frames, ANIMATION_SPEED / FRAME_LENGTH, frames[0].getWidth(), frames[0].getHeight(), true);
            addComponent(component);
        } else {
            // In Test Mode set the size explicitly since we don't have a renderer
            setSize(new Vector2f(37, 42));
        }

        setPosition(pos);
    }

    public void setGrave(){
        removeComponent(component);
        component = new ImageRenderComponent(Assets.loadImage(Assets.Images.GRAVE));
        addComponent(component);
    }

}
