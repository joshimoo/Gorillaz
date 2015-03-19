package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.component.render.AnimationRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Gorilla extends Entity {

    /** How fast should the Animation be */
    private static final float ANIMATION_SPEED = 1.0f;
    /** How long a single frame should be displayed [ms] */
    private static final int   FRAME_LENGTH    = 1000;

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
            addComponent(new AnimationRenderComponent(frames, ANIMATION_SPEED / FRAME_LENGTH, frames[0].getWidth(), frames[0].getHeight(), true));
        } else {
            // In Test Mode set the size explicitly since we don't have a renderer
            setSize(new Vector2f(25, 42));
        }

        setPosition(pos);
    }

}
