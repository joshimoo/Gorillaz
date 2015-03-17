package de.tu_darmstadt.gdi1.gorillas.main;

import de.matthiasmann.twl.slick.TWLStateBasedGame;
import de.tu_darmstadt.gdi1.gorillas.ui.states.*;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.Utils;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import java.net.URL;

public class Gorillas extends TWLStateBasedGame {

    public static Gorillas game;

    /* Global Parameters */
    public static final int FRAME_WIDTH     = 800;
    public static final int FRAME_HEIGHT    = 600;
    public static final int TARGET_FPS      = 120;

    public static final String THEME    = "/theme.xml";

    public Gorillas(boolean debug) {
        super("Gorillas");
        Game.getInstance().setDebug(debug);
        Database.getInstance().readFromFile();
        Database.getInstance().restoreConfigFromFile();
    }

    public static void main(String[] args) throws SlickException {
        Utils.setNativePath();
        AppGameContainer app = new AppGameContainer(new Gorillas(Game.getInstance().getDebug()));
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
        return getClass().getResource(THEME);
    }
}
