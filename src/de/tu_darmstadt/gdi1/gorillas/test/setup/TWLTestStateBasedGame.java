package de.tu_darmstadt.gdi1.gorillas.test.setup;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

import java.net.URL;

/**
 * TWLTestStateBasedGame is used by TestGorillas.java. TWLStateBasedGame is used
 * by Gorillas.java
 * <p>
 * This class is used for testing a slick state based game, which uses TWL as
 * GUI library.
 * <p>
 * TWLTestStateBasedGame is an adaption of TWLStateBasedGame, it prevents all
 * the rendering. It is used if the StateBasedGame is tested.
 *
 * @author Peter
 */
public abstract class TWLTestStateBasedGame extends StateBasedGame {

    private GUI gui = null;

    public TWLTestStateBasedGame(String name) {
        super(name);

    }

    /**
     * Adds a new game state
     *
     * @param state the game state
     * @see StateBasedGame#addState(org.newdawn.slick.state.GameState)
     */
    public void addState(BasicTWLGameState state) {
        super.addState(state);
    }

    /**
     * Adds a new game state.
     * <p>
     * This method is overriden to ensure that only instances of
     * BasicTWLGameState are added.
     *
     * @param state the game state. Must be an instance of BasicTWLGameState
     * @see StateBasedGame#addState(org.newdawn.slick.state.GameState)
     */
    @Override
    public void addState(GameState state) {
        if (!(state instanceof BasicTWLGameState)) {
            throw new IllegalArgumentException("state must be a BasicTWLGameState");
        }
        super.addState(state);
    }

    /**
     * Implement this method and return the URL for the TWL theme.
     *
     * @return the URL for the TWL theme. Must not be null.
     */
    protected abstract URL getThemeURL();

    /**
     * Transits to a the specified game state. This method hides the UI of the
     * current state before starting the transition.
     *
     * @param id    The ID of the state to enter
     * @param leave The transition to use when leaving the current state
     * @param enter The transition to use when entering the new state
     * @see StateBasedGame#enterState(int,
     * org.newdawn.slick.state.transition.Transition,
     * org.newdawn.slick.state.transition.Transition)
     */
    @Override
    public void enterState(int id, Transition leave, Transition enter) {

        super.enterState(id, leave, enter);
    }

    public void setRootPane(RootPane rootPane) throws SlickException {

    }

    @Override
    protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
        if (gui != null) {
            gui.draw();
        }
    }

    @Override
    protected void postUpdateState(GameContainer container, int delta) throws SlickException {
        if (gui != null) {
            gui.setSize();
            gui.handleTooltips();
            gui.updateTimers();
            gui.invokeRunables();
            gui.validateLayout();
            gui.setCursor();
        }
    }
}
