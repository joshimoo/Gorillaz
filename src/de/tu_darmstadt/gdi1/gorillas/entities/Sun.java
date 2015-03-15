package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.render.AnimationRenderComponent;
import eea.engine.component.render.FrameRenderComponent;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

public class Sun extends Entity {
    private FrameRenderComponent animation;

    // TODO: we currently use topleft positions everywhere, change them all to be center positions
    public Sun(Vector2f pos) {
        super("Sun");
        setPosition(pos);

        // Rendering
        Image[] frames = new Image[] {
                Assets.loadImage(Assets.Images.SUN_SMILING),
                Assets.loadImage(Assets.Images.SUN_ASTONISHED)
        };
        animation = new FrameRenderComponent(frames, frames[0].getWidth(), frames[0].getHeight());
        addComponent(animation);
    }

    @Override
    public Shape getShape() {
        return new Circle(getPosition().x, getPosition().y, getSize().y / 2);
    }

    /**
     * We collide if there is an intersection
     * But also if the otherEntity is completely contained inside of us
     */
    @Override
    public boolean collides(Entity otherEntity) {
        boolean collides = super.collides(otherEntity) || getShape().contains(otherEntity.getShape());
        animation.switchToFrame(collides ? 1 : 0);
        return collides;
    }

    @Deprecated
    public boolean isCollidding(Banana b) {
        return collides(b);
    }
}
