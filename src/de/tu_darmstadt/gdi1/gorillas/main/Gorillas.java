package de.tu_darmstadt.gdi1.gorillas.main;

import de.matthiasmann.twl.slick.TWLStateBasedGame;
import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;
import de.tu_darmstadt.gdi1.gorillas.ui.states.GameSetupState;
import de.tu_darmstadt.gdi1.gorillas.ui.states.MainMenuState;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import java.net.URL;

public class Gorillas extends TWLStateBasedGame {

    /* State Definitions */
    public static final int MAINMENUSTATE   = 0;
    public static final int GAMESETUPSTATE  = 1;
    public static final int GAMEPLAYSTATE   = 2;
    public static final int HIGHSCORESTATE  = 3;
    public static final int OPTIONSTATE     = 4;
    public static final int TUTORIALSTATE   = 5;

    /* Global Parameters */
    public static final int FRAME_WIDTH     = 800;
    public static final int FRAME_HEIGHT    = 600;
    public static final int TARGET_FPS      = 120;

    public static final String THEME    = "/theme.xml";
    public static final String NATIVE   = "/lib/lwjgl-2.9.1/native/";
    public static final String ASSETS   = "/assets/gorillas/";

    /* Global Variables */
    public static String player1 = "Player1";
    public static String player2 = "Player2";

    public static boolean debug = false;

    public Gorillas(boolean debug) {
        super("Gorillas");
        setDebug(debug);
    }

    public static void setDebug(boolean debuging) {
        debug = debuging;
    }

    public static void setNativePath(){
        // @formatter:off
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + NATIVE + "windows");
        }
        else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + NATIVE + "macosx");
        }
        else {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + NATIVE + System.getProperty("os.name").toLowerCase());
        }

        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "false");
        System.err.println(System.getProperty("os.name") + ": " + System.getProperty("org.lwjgl.librarypath"));
        // @formatter:on
    }

    public static void main(String[] args) throws SlickException {
        setNativePath();
        AppGameContainer app = new AppGameContainer(new Gorillas(false));
        app.setShowFPS(true);
        app.setDisplayMode(FRAME_WIDTH, FRAME_HEIGHT, false);
        app.setTargetFrameRate(TARGET_FPS);
        app.start();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        // Add states to the StateBasedGame
        this.addState(new MainMenuState());
        this.addState(new GameSetupState());
        this.addState(new GamePlayState());
        // TODO: Add the other states...

        // Add states to the StateBasedEntityManager
        StateBasedEntityManager.getInstance().addState(MAINMENUSTATE);
        StateBasedEntityManager.getInstance().addState(GAMESETUPSTATE);
        StateBasedEntityManager.getInstance().addState(GAMEPLAYSTATE);
        // TODO: Add the other states...
    }

    @Override
    protected URL getThemeURL() {
        return getClass().getResource(THEME);
    }
}
