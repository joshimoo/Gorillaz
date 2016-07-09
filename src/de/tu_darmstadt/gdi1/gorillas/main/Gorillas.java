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
    public static int FRAME_WIDTH   = Database.getInstance().getDisplayWidth();
    public static int FRAME_HEIGHT  = Database.getInstance().getDisplayHeight();
    public static int CANVAS_WIDTH  = Database.getInstance().getCanvasWidth();
    public static int CANVAS_HEIGHT = Database.getInstance().getCanvasHeight();
    public static int TARGET_FPS    = 120;

    public static final String THEME    = "/theme.xml";

    public Gorillas() {
        super("Gorillas");
        Game.getInstance().enableTestMode(false);
        Database.getInstance().readFromFile();
        Database.getInstance().restoreConfigFromFile();
    }

    public static void main(String[] args) throws SlickException {
        Utils.setNativePath();
        AppGameContainer app = new AppGameContainer(new Gorillas());
        app.setDisplayMode(FRAME_WIDTH, FRAME_HEIGHT, false);
        app.setTargetFrameRate(TARGET_FPS);
        app.setShowFPS(false);
        app.start();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new MainMenuState());
        this.addState(new GameSetupState());
        this.addState(new GamePlayState());
        this.addState(new HighScoreState());
        this.addState(new InGamePause());
        this.addState(new HelpState());
        this.addState(new GameVictory());
        this.addState(new OptionState());

        StateBasedEntityManager.getInstance().addState(Game.MAINMENUSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMESETUPSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMEPLAYSTATE);
        StateBasedEntityManager.getInstance().addState(Game.HIGHSCORESTATE);
        StateBasedEntityManager.getInstance().addState(Game.INGAMEPAUSE);
        StateBasedEntityManager.getInstance().addState(Game.HELPSTATE);
        StateBasedEntityManager.getInstance().addState(Game.GAMEVICTORY);
        StateBasedEntityManager.getInstance().addState(Game.OPTIONSTATE);
    }

    @Override
    protected URL getThemeURL() {
        return getClass().getResource(THEME);
    }

}
