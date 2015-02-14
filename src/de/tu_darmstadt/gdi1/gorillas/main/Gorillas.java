package de.tu_darmstadt.gdi1.gorillas.main;

import de.matthiasmann.twl.slick.TWLStateBasedGame;
import de.tu_darmstadt.gdi1.gorillas.ui.states.MainMenuState;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import java.net.URL;

/**
 * Main class of the gorilla game
 *
 * @author Peter Kloeckner, Sebastian Fach
 * @version 1.0
 */
public class Gorillas extends TWLStateBasedGame {

    // Each state is represented by an integer value
    public static final int MAINMENUSTATE = 0;
    public static final int GAMESETUPSTATE = 1;
    public static final int GAMEPLAYSTATE = 2;
    public static final int HIGHSCORESTATE = 3;
    public static final int OPTIONSTATE = 4;
    public static final int INSTRUCTIONSSTATE = 5;

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;

    public static final int TARGET_FRAME_RATE = 120;

    public static boolean debug = false;

    public Gorillas(boolean debug) {
        super("Gorillas");
        setDebug(debug);
    }

    public static void setDebug(boolean debuging) {
        debug = debuging;
    }

    public static void main(String[] args) throws SlickException {

        // Set the native library path (depending on the operating system)
        // @formatter:off
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/lwjgl-2.9.1/native/windows");
        }
        else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/lwjgl-2.9.1/native/macosx");
        }
        else {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/lwjgl-2.9.1/native/" + System.getProperty("os.name").toLowerCase());
        }

        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "false");
        System.err.println(System.getProperty("os.name") + ": " + System.getProperty("org.lwjgl.librarypath"));
        // @formatter:on

        // Insert this StateBasedGame into an AppContainer (a window)
        AppGameContainer app = new AppGameContainer(new Gorillas(false));

        // Set window properties and start it
        app.setShowFPS(false);
        app.setDisplayMode(FRAME_WIDTH, FRAME_HEIGHT, false);
        app.setTargetFrameRate(TARGET_FRAME_RATE);
        app.start();
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
