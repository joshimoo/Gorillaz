package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.actions.ThrowMovement;
import de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
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

    public static Entity createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windAcceleration ) {
        Entity banana = createEntity(EntityType.PROJECTILE, pos);

        // Rendering
        Image image = Assets.loadImage(Assets.Images.BANANA); // unique not needed any more since we rotate the entity
        banana.addComponent( new ImageRenderComponent(image) );

        // Movement
        // NOTE: Events cycle through there actions linearly, so there is an implicit priority
        // TODO: Make sure that Movement comes before rotation or in reverse but be consistent
        // At the moment the order has no impact, since our movement is in world space instead of local space
        // FIXME: The way I am dealing with the unit conversion, needs to be fixed.
        // FIXME: Devise a better strategy, maybe applie the MS_TO_S + TIMESCALE only to delta Time before dealing with it.
        LoopEvent update = new LoopEvent();
        update.addAction(new ThrowMovement(pos.x, pos.y, throwAngle,
                throwSpeed,
                gravity,
                windAcceleration * Game.getWindScale())
        );

        // Rotation
        float direction = throwAngle > 90 ? -1 : 1;
        update.addAction(new RotateRightAction(throwSpeed * direction * Game.getRotationFactor() * 360 / 1000));
        banana.addComponent(update);

        // Collision
        CollisionEvent collision = new CollisionEvent();
        OREvent validTargets = new OREvent(
                new CollidedWithEvent(collision, EntityType.PLAYER.name()),
                new CollidedWithEvent(collision, EntityType.BUILDING.name()) // TODO: Refactor after Sky Scrapper/Skyline Decision
        );
        ANDEvent validCollision = new ANDEvent(collision, validTargets);
        validCollision.addAction(new DestroyEntityAction());
        // banana.addComponent(validCollision);

        // TODO: Add Scoring Actions


        return banana;
    }
}
