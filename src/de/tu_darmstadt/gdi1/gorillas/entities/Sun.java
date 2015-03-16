package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.component.render.FrameRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Sun extends Entity {
    private FrameRenderComponent animation;

    // TODO: we currently use topleft positions everywhere, change them all to be center positions
    public Sun(Vector2f pos) {
        super("Sun");
        setPosition(pos);

        if (!Game.getInstance().isTestMode()) {
            // Rendering
            Image[] frames = new Image[]{Assets.loadImage(Assets.Images.SUN_SMILING), Assets.loadImage(Assets.Images.SUN_ASTONISHED)};
            animation = new FrameRenderComponent(frames, frames[0].getWidth(), frames[0].getHeight());
            addComponent(animation);
        } else {
            // In Test Mode the sun should be 100x100 px
            setSize(new Vector2f(100, 100));
        }
    }

    /**
     * We collide if there is an intersection
     * But also if the otherEntity is completely contained inside of us
     */
    @Override
    public boolean collides(Entity otherEntity) {
        boolean collides = super.collides(otherEntity) || getShape().contains(otherEntity.getShape());
        setHit(collides);
        return collides;
    }

    @Deprecated
    public boolean isCollidding(Banana b) {
        return collides(b);
    }

    private boolean hit;
    public boolean isHit() {
        return hit;
    }
    private void setHit(boolean collides) {

        hit = collides;
        if(!Game.getInstance().isTestMode()) {
            animation.switchToFrame(collides ? 1 : 0);
        }
    }
}
