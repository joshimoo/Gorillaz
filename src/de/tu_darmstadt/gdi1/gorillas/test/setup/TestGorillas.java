package de.tu_darmstadt.gdi1.gorillas.test.setup;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.ui.states.*;
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
    // Normally we could remove these and use Game.StateXXX
    // But we don't know if the private tests use these.
    // TODO: so make sure that these are the same as Game.StateXXX
    public static final int MAINMENUSTATE = 0;
    public static final int GAMESETUPSTATE = 1;
    public static final int GAMEPLAYSTATE = 2;
    public static final int HIGHSCORESTATE = 3;
    public static final int OPTIONSTATE = 4;
    public static final int INSTRUCTIONSSTATE = 5;

    public static boolean debug = false;

    public TestGorillas(boolean debug) {
        super("Gorillas");
        // Always activate, testmode in TestGorillas
        setDebug(debug);
        Game.getInstance().enableTestMode(true);
    }

    public static void setDebug(boolean debuging) {
        debug = debuging;
        Game.getInstance().setDebug(debuging);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        // Add states to the StateBasedGame
        this.addState(new MainMenuState());
        this.addState(new GameSetupState());
        this.addState(new GamePlayState());
        this.addState(new HighScoreState());
        this.addState(new InGamePause());
        this.addState(new HelpState());
        this.addState(new GameVictory());
        this.addState(new OptionState());
        // TODO: Add the other states...

        // Add states to the StateBasedEntityManager
        StateBasedEntityManager.getInstance().addState(Game.MAINMENUSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMESETUPSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMEPLAYSTATE);
        StateBasedEntityManager.getInstance().addState(Game.HIGHSCORESTATE);
        StateBasedEntityManager.getInstance().addState(Game.INGAMEPAUSE);
        StateBasedEntityManager.getInstance().addState(Game.HELPSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMEVICTORY);
        StateBasedEntityManager.getInstance().addState(Game.OPTIONSTATE);
        // TODO: Add the other states...
    }

    @Override
    protected URL getThemeURL() {
        return getClass().getResource("/theme.xml");
    }
}
