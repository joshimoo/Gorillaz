package de.tu_darmstadt.gdi1.gorillas.entities.events;

import eea.engine.entity.Entity;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Use this together with an AndEvent, and a CollisionEvent
 * The Collision Event needs to be evaluated before this event
 * @author Joshua Moody (joshua.moody@stud.tu-darmstadt.de)
 */
public class CollidedWithEvent extends Event {
    // Only trigger for Collision with this type
    String type;
    CollisionEvent collision;

    /**
     * Can be used to detect whether the last collided entity is of the requested type
     * Use this in combination with a ANDEvent
     * Like this new ANDEvent(collision, new CollidedWithEvent(collision, "EntityType"));
     * @param collision the actual event that is responsible for collision detection
     * @param type the entity type we are interested in
     */
    public CollidedWithEvent(CollisionEvent collision, String type) {
        super("CollidedWithEvent");
        this.collision = collision;
        this.type = type;
    }

    @Override
    protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
        if (collision != null) {
            Entity other = collision.getCollidedEntity();
            return other != null && other.getID().equalsIgnoreCase(type);
        }

        return false;
    }
}
