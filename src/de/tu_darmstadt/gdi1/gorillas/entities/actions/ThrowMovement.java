package de.tu_darmstadt.gdi1.gorillas.entities.actions;

import eea.engine.action.basicactions.Movement;
import org.newdawn.slick.geom.Vector2f;

/**
 * If you make all movement actions additive
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

    // TODO: Move out of here, let the Creator handle additional Scale Factors
    // TODO: the game should use m/s for all design parameters. Then the creator can translate from m/s to m/ms
    public static final float SPEED_MOD = 0.8f;
    private float gravity = 9.80665f;

    private float windSpeed;
    private float rotationSpeed, t;

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

        // TODO: remove unneeded params
        rotationSpeed = (speed * 0.02f) * 360f / 1000f;

        // We should only need single precision
        vx = (float) (Math.cos(Math.toRadians(angle)) * speed * SPEED_MOD);
        vy = (float) (Math.sin(Math.toRadians(angle)) * speed * SPEED_MOD);

        x0 = x;
        y0 = y;
        t  = 0;

        gravity = g;
        windSpeed = w;

        if(angle > 90) rotationSpeed = -rotationSpeed;
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

        t = t + delta / 400f; // TODO: Cleanup Factor
        // float x = x0 + (vx * t) + (Cloud.WSCALE /2 * windSpeed * t * t); // TODO: WSCALE should be taken care of by the CREATOR
        float x = x0 + (vx * t) + (windSpeed * t * t);
        float y = y0 - (vy * t) + (gravity/2 * t * t);

        return new Vector2f(x, y);
    }
}
