package de.tu_darmstadt.gdi1.gorillas.test.setup;

import de.tu_darmstadt.gdi1.gorillas.ui.states.MainMenuState;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import java.net.URL;

/**
 * This class mainly does the same as Gorillas does, but it extends
 * TWLTestStateBasedGame instead of TWLStateBasedGame. TestGorillas is used for
 * testing the Gorillas game; TWLTestStateBasedGame does not do any rendering in
 * contrast to TWLStateBasedGame.
 *
 * @author Peter Kloeckner, Sebastian Fach
 */
public class TestGorillas extends TWLTestStateBasedGame {

    // Each state is represented by an integer value
    public static final int MAINMENUSTATE = 0;
    public static final int GAMESETUPSTATE = 1;
    public static final int GAMEPLAYSTATE = 2;
    public static final int HIGHSCORESTATE = 3;
    public static final int OPTIONSTATE = 4;
    public static final int INSTRUCTIONSSTATE = 5;

    public static boolean debug = false;

    public TestGorillas(boolean debug) {
        super("Gorillas");
        setDebug(debug);
    }

    public static void setDebug(boolean debuging) {
        debug = debuging;
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        // Add states to the StateBasedGame
        this.addState(new MainMenuState(MAINMENUSTATE));
        // TODO: Add the other states...

        // Add states to the StateBasedEntityManager
        StateBasedEntityManager.getInstance().addState(MAINMENUSTATE);
        // TODO: Add the other states...
    }

    @Override
    protected URL getThemeURL() {
        return getClass().getResource("/theme.xml");
    }
}
