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
        if (collides) { triggerAstonished(false); }
        return collides;
    }

    @Deprecated
    public boolean isCollidding(Banana b) {
        return collides(b);
    }

    private boolean astonished;
    public boolean isAstonished() {
        return astonished;
    }
    public void resetAstonished() { triggerAstonished(true); }
    private void triggerAstonished(boolean reset) {
        // Tests are stupid, require the sun to be astonished for XXX Amount (whole round)
        astonished = !reset;
        if(!Game.getInstance().isTestMode()) {
            animation.switchToFrame(astonished ? 1 : 0);
        }
    }
}
