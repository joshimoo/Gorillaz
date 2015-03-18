package de.tu_darmstadt.gdi1.gorillas.test.adapter;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.test.setup.TestGorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;
import org.newdawn.slick.geom.Vector2f;

public class GorillasTestAdapterExtended2 extends GorillasTestAdapterExtended1 {

    public GorillasTestAdapterExtended2() {
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
     * @param deltaTime       the time passed in ms
     * @param fromLeftToRight true if the shot was fired by the left player and thus moves
     *                        from left to right, otherwise false
     * @return the next position of the shot
     */
    public Vector2f getNextShotPosition(Vector2f startPosition, int angle, int speed, int wind, boolean fromLeftToRight, int deltaTime) {

        // TODO: Implement
        return null;
    }

    /**
     * Ensure that this method returns exactly the same the wind scaling factor,
     * which is used within the calculations of the parabolic flight to make the
     * influence of the wind look more realistic.
     *
     * @return the wind scaling factor for the parabolic flight calculation
     */
    public float getWindScalingFactor() {
        return Game.getInstance().getWindScale();
    }

    /**
     * checks whether the sun is astonished in the moment
     *
     * @return true if the game is in GameplayState and the sun is astonished,
     * otherwise false
     */
    public boolean isSunAstonished() {
        if(gorillas.getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            GamePlayState state = (GamePlayState) gorillas.getCurrentState();

            // Our sun is only Astonished, while being in contact with the projectile
            return state.getSun().isAstonished();
        }

        return false;
    }

}
