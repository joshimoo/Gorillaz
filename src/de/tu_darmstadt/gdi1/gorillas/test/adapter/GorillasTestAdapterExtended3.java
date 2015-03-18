package de.tu_darmstadt.gdi1.gorillas.test.adapter;

import de.tu_darmstadt.gdi1.gorillas.entities.Banana;
import org.newdawn.slick.geom.Vector2f;

public class GorillasTestAdapterExtended3 extends GorillasTestAdapterExtended2 {

    protected int gravity;

    public GorillasTestAdapterExtended3() {
        super();
    }

    @Override
    public void rememberGameData() {
        super.rememberGameData();
    }

    @Override
    public void restoreGameData() {
        super.restoreGameData();
    }

    /**
     * Computes the next position of a throw. Your method has to evaluate the
     * parameters as defined by the task description. You can choose your own
     * wind and time scaling factors. The constraint is: The gameplay. This
     * means on one hand, that the user does not have to wait for minutes till
     * the shot either collides or leaves the screen (not too slow). On the
     * other hand, the user has to be able to follow the shot with the eyes (not
     * too fast). To ensure testability, please provide your wind and time
     * scaling factors via {@link #getWindScalingFactor()} respectively
     * {@link #getTimeScalingFactor()}.
     *
     * @param startPosition   the (x,y) start position of the shot. The upper left corner of
     *                        the game screen is (0,0). The lower right corner of the game
     *                        screen is (width of game screen, height of game screen).
     * @param angle           the starting angle in degree from 0 to 360
     * @param speed           the speed in a range from 0 to 200
     * @param wind            the wind speed on a scale from -15 to 15, whereas -15 means
     *                        strong wind from right to left and 15 means strong wind from
     *                        left to right
     * @param gravity         the gravity on a scale from 0 to 20
     * @param deltaTime       the time passed in ms
     * @param fromLeftToRight true if the shot was fired by the left player and thus moves
     *                        from left to right, otherwise false
     * @return the next position of the shot
     */
    public Vector2f getNextShotPosition(Vector2f startPosition, int angle, int speed, int wind, int gravity, boolean fromLeftToRight, int deltaTime) {
        angle = fromLeftToRight ? angle : 180 - angle;
        Banana banana = new Banana(startPosition, angle, speed, gravity, wind);
        banana.update(null, null, deltaTime);
        return banana.getPosition();
    }
}
