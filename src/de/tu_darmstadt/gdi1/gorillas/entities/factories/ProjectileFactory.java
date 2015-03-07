package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.actions.ThrowMovement;
import de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * Responsible for Creation of all Projectile Entities
 * For Example, Banana, Snickers, etc
 */
public class ProjectileFactory extends EntityFactory {

    // TODO: Refactor, rethink where todo the scaling and unit conversion
    // It might be better to have the caller pass converted values
    private static final float PROJECTILE_SCALE = 25;
    public static float getProjectileScale() { return PROJECTILE_SCALE; }

    private static final float ROTATION_DRAG = 0.02f;
    private static float getRotationFactor() { return ROTATION_DRAG; }

    public static Entity createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windAcceleration ) {
        Entity banana = createEntity(EntityType.PROJECTILE, pos);

        // Rendering
        Image image = Assets.loadImage(Assets.Images.BANANA); // unique not needed any more since we rotate the entity
        banana.addComponent( new ImageRenderComponent(image) );

        // Movement
        // NOTE: Events cycle through there actions linearly, so there is an implicit priority
        // TODO: Make sure that Movement comes before rotation or in reverse but be consistent
        // At the moment the order has no impact, since our movement is in world space instead of local space
        LoopEvent update = new LoopEvent();
        update.addAction(new ThrowMovement(pos.x, pos.y, throwAngle, throwSpeed * MS_TO_S * getProjectileScale(), gravity, windAcceleration));

        // Rotation
        float direction = throwAngle > 90 ? -1 : 1;
        update.addAction(new RotateRightAction(throwSpeed * direction * getRotationFactor() * 360 * MS_TO_S));
        banana.addComponent(update);

        // Collision
        CollisionEvent collision = new CollisionEvent();
        OREvent validTargets = new OREvent(
                new CollidedWithEvent(collision, EntityType.GORILLA.name()),
                new CollidedWithEvent(collision, EntityType.BUILDING.name()) // TODO: Refactor after Sky Scrapper/Skyline Decision
        );
        ANDEvent validCollision = new ANDEvent(collision, validTargets);
        validCollision.addAction(new DestroyEntityAction());
        banana.addComponent(validCollision);

        // TODO: Add Scoring Actions


        return banana;
    }
}
