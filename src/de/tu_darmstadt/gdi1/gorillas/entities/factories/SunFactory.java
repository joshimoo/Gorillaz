package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.action.Action;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.NOTEvent;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Responsible for Creation of all Sun/Moon (Interactive Background) Entities
 * For Example: Sun, Moon
 */
public class SunFactory extends EntityFactory {

    public static Entity createSun(Vector2f pos) {
        Entity sun = createEntity(EntityType.SUN, pos);

        // Rendering
        Image image = Assets.loadImage(Assets.Images.SUN_SMILING);
        sun.addComponent( new ImageRenderComponent(image) );

        // Rotation
        float rotationSpeed = 25;
        LoopEvent update = new LoopEvent();
        update.addAction(new RotateRightAction( (rotationSpeed * Game.getRotationFactor()) * 360f / 1000f));
        sun.addComponent(update);

        // Collisions
        CollisionEvent collision = new CollisionEvent();
        ANDEvent validCollision = new ANDEvent(collision, new CollidedWithEvent(collision, EntityType.PROJECTILE.name()));
        validCollision.addAction(new Action() {
            @Override
            public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
                // TODO: Add dedicated AnimationSwitching Action
                // TODO: add reusable action classes for animation switching
                // TODO: ADD ANIMATION ACTIONS (manuall control, [hit, sad])
                // TODO: Switch to Assets.Images.SUN_ASTONISHED;
            }
        });

        // No Collision
        LoopEvent invalidTarget = new LoopEvent();
        invalidTarget.addAction(new Action() {
            @Override
            public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
                // TODO: add reusable action classes for animation switching
                // TODO: ADD ANIMATION ACTIONS (manuall control, [hit, sad])
                // TODO: Switch back to Assets.Images.SUN_SMILING;
            }
        });

        // TODO: Next Evalation will be Component Driven
        // The OREvent makes sure that there is only one possible action executed
        // Since OR only evaluates it's events till one of them is true
        //sun.addComponent(new OREvent(validCollision, invalidTarget));

        // Return
        return sun;
    }
}
