package de.tu_darmstadt.gdi1.gorillas.entities.actions;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.action.basicactions.Movement;
import org.newdawn.slick.geom.Vector2f;

/**
 * TODO: a great idea, is if you make all movement actions additive
 * that would allow one to have multiple movement actions (gravity, wind, userinput) affect the same entity
 *
 * NOTE: there is an implicit, priority based on the order in which you add components
 * So you can actually use that, todo additive movements, since after each action is done the enitity position will have been updated.
 *
 * I feel like what we really need is a physics component (rigidbody)
 * that deals with collision detection and physics based motion.
 * but that would be overkill for this project, so I am not going to bother.
 * add a wind action/component, then all entities that depend on the wind can use that.
 * add a gravity action/component, then all entities that depend on gravity can use that.
 */

/**
 * Implements a parabolic flight, that is impacted by gravity and static wind
 */
public class ThrowMovement extends Movement {
    private float gravity = 9.80665f;
    private float windAcceleration;
    private float t;

    // TODO: store as Vector2f
    private final float vx, vy, x0, y0;

    /**
     * Creates a new Movement with the concrete movement speed passed in.
     *
     * @param speed the current movement speed
     * @see eea.engine.interfaces.IMovement
     */
    public ThrowMovement(float x, float y, float angle, float speed, float g, float w ) {
        super(speed);

        // We should only need single precision
        vx = (float) (Math.cos(Math.toRadians(angle)) * speed);
        vy = (float) (Math.sin(Math.toRadians(angle)) * speed);

        x0 = x;
        y0 = y;
        t  = 0;

        gravity = g;
        windAcceleration = w;
    }


    /**
     * This method determines the new position of the object, based on its current
     * position, movement speed, orientation, and time passed.
     * <p>
     * The displacement is determined by multiplying the speed with the time
     * passed (delta). The the new (x, y) position is then determined based on the
     * orientation and the displacement.
     *
     * @param position the current position of the underlying object
     * @param speed    the current movement speed, as passed to the constructor
     * @param rotation the current orientation of the object, as measured in degrees
     * @param delta    the amount of time passed, used to determine how far the object
     *                 has to move; more precisely: speed (in "pixels per ns") multiplied
     */
    @Override
    public Vector2f getNextPosition(Vector2f position, float speed, float rotation, int delta) {
        // TODO: calculate position delta based on current position, not start position
        // That way we can modify params, in flight withhout breaking the calculation.
        // Right now there is a dependency on t0 and pos0

        // TODO: Add Different Throw Components (Gravity, Static Wind, Dynamic Wind, Bouncing (dotzen) )
        // TODO: Move This to ThrowStaticWindMovement
        t += delta * Game.getTimeScale();
        float x = x0 + (vx * t) + (0.5f * windAcceleration * t * t);
        float y = y0 - (vy * t) + (0.5f * gravity * t * t);

        return new Vector2f(x, y);
    }
}
