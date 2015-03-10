package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.actions.ThrowMovement;
import de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent;
import eea.engine.action.Action;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.OREvent;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This is an example on how to use the EEA Framework,
 * Together with a FactoryDesign pattern
 * The Code is not supposed to be run, only to be read for understanding :)
 * @author Joshua Moody (joshua.moody@stud.tu-darmstadt.de)
 */
public class ExampleFactory extends EntityFactory{
    public static Entity createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windSpeed ) {
        Entity banana = createEntity(EntityType.PROJECTILE, pos);
        float rotationSpeed = (throwSpeed * 0.02f) * 360f / 1000f; // TODO: Refactor params

        // Rendering
        Image image = Assets.loadImage(Assets.Images.BANANA);
        banana.addComponent( new ImageRenderComponent(image) );

        // Movement
        // NOTE: Events cycle through there actions linearly, so there is an implicit priority
        // Make sure that Movement comes before rotation
        LoopEvent update = new LoopEvent();
        update.addAction(new ThrowMovement(pos.x, pos.y, throwAngle, throwSpeed, gravity, windSpeed)); // CUSTOM Multi Use Action
        update.addAction(new RotateRightAction(rotationSpeed));
        banana.addComponent(update);

        /**
         * Here are some ways to use the CollidedWithEvent.
         * CollidedWithEvent is a custom Event that I wrote.
         * @see de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent
         */
        CollisionEvent collision = new CollisionEvent();


        /** Specific Action for specific Collision Type */
        ANDEvent gorillaCollision = new ANDEvent(collision, new CollidedWithEvent(collision, EntityType.PLAYER.name()));
        gorillaCollision.addAction(new DestroyEntityAction()); // EEA provided Multi Use Action
        gorillaCollision.addAction((gc, sb, delta, event) -> System.out.println("Single Use Action, implemented as Lambda Single Expression"));
        gorillaCollision.addAction(new Action() {
            // Because Lambdas only work, since Java 8
            // you can use this instead to be compatible with JAVA_VERSIONS < 8

            @Override
            public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
                System.out.println("Single Use Action, implemented as Anonymous Class");
            }
        });
        banana.addComponent(gorillaCollision);


        /** Same Action, for a set of targets */
        CollidedWithEvent[] targets = new CollidedWithEvent[] {
                new CollidedWithEvent(collision, EntityType.CLOUD.name()),
                new CollidedWithEvent(collision, EntityType.SUN.name()),
                new CollidedWithEvent(collision, EntityType.PLAYER.name()),
                new CollidedWithEvent(collision, "DynamicRuntimeEntity") // Don't do this for our gorillas game, use the EntityType
        };

        // Because of the boolean exit early logic, valid targets is only evaluated if there was a collision first
        // if you use this setup, collsion detection is also only evaluated once per frame, awesome :)
        OREvent validTargets = new OREvent(targets);
        ANDEvent groupCollision = new ANDEvent(collision, validTargets);
        groupCollision.addAction((gc, sb, delta, event) -> {
            System.out.println("Single Use Action, implemented as Lambda Block");
            System.out.println("There was a collision && it was a valid target");
        });
        banana.addComponent(groupCollision);


        /** Different Actions, for a set of targets */
        CollidedWithEvent target0 = new CollidedWithEvent(collision, EntityType.CLOUD.name());
        target0.addAction((gc, sb, delta, event) -> System.out.println("Do Target0 Action"));

        CollidedWithEvent target1 = new CollidedWithEvent(collision, "DynamicRuntimeEntity");
        target1.addAction((gc, sb, delta, event) -> System.out.println("Do Target1 Action"));

        OREvent validTargetsIndividualActions = new OREvent(target0, target1);
        validTargetsIndividualActions.addAction((gc, sb, delta, event) -> {
            System.out.println("We can have inidivual actions per target");
            System.out.println("but also general actions that executes as long as one target was valid");
            System.out.println("It all depends on how you combine the boolean logic");
            System.out.println("And where in the chain you add the action too");
        });
        banana.addComponent(new ANDEvent(collision, validTargetsIndividualActions));


        /** Either Collision or other action */
        // Collisions
        ANDEvent validCollision = new ANDEvent(collision, new CollidedWithEvent(collision, EntityType.PROJECTILE.name()));
        validCollision.addAction((gc, sb, delta, event) -> System.out.println("while we are colliding, change image to XXX"));

        // No Collision
        LoopEvent invalidTarget = new LoopEvent();
        invalidTarget.addAction((gc, sb, delta, event) -> System.out.println("We are no longer colliding, change image back to YYY"));

        // The OREvent makes sure that there is only one possible action executed
        // Since OR only evaluates it's events till one of them is true
        banana.addComponent(new OREvent(validCollision, invalidTarget));


        /**
         * A completely different approach is to create a new Collision Event
         * That gets passed a collisionMatrix in it's constructor
         * But generally this composition approach is more modular
         */

        return banana;
    }
}