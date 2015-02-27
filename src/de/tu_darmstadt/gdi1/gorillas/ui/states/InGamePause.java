package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class InGamePause extends BasicTWLGameState {
    private Image background;
    private Color color = new Color(50,50,50,150);
    private Button btnNewGame;
    private Button btnExit;
    private Button btnMute;
    private Button btnMainMenu;
    private RootPane rp;
    private StateBasedGame game;

    @Override
    public int getID() {
        return Gorillas.INGAMEPAUSE;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);

        this.createRootPane();
        this.game = game;

    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        background.draw(-20,-10);
        GamePlayState.getSkyline().render(g);
        GamePlayState.getGorilla(0).render(g);
        GamePlayState.getGorilla(1).render(g);
        GamePlayState.getSun().render(g);
        g.setColor(color);
        g.fillRect(0, 0, Gorillas.FRAME_WIDTH, Gorillas.FRAME_HEIGHT);


    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        if(container.getInput().isKeyPressed(Input.KEY_ESCAPE) || container.getInput().isKeyPressed(Input.KEY_P)){
            game.enterState(Gorillas.GAMEPLAYSTATE);
        }
        Input in_key = container.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN)) game.enterState(Gorillas.GAMESETUPSTATE);
        if (in_key.isKeyPressed(Input.KEY_M)) {/* TODO: Mute me :) */ System.out.println("Mute");}

    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        btnNewGame.setSize(128, 32);
        btnNewGame.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 150);

        btnExit.setSize(128, 32);
        btnExit.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 225);

        btnMute.setSize(64, 64);
        btnMute.setPosition(0, paneHeight - btnMute.getHeight());

        btnMainMenu.setSize(128,32);
        btnMainMenu.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 300);
    }

    @Override
    protected RootPane createRootPane() {
        rp = super.createRootPane();

        btnNewGame = new Button("New Game");
        btnNewGame.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.GAMESETUPSTATE);
            }
        });

        btnExit = new Button("Exit Game");
        btnExit.addCallback(new Runnable() {
            public void run() {
                /// FIXME: ist system.exit ok?
                System.exit(0);
            }
        });

        btnMute = new Button("Mute");
        btnMute.addCallback(new Runnable() {
            public void run() {
                GamePlayState.setMute();
                System.out.println("Mute");
            }
        });

        btnMainMenu = new Button("Main Menu");
        btnMainMenu.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.MAINMENUSTATE);
            }
        });

        rp.add(btnNewGame);
        rp.add(btnExit);
        rp.add(btnMute);
        rp.add(btnMainMenu);

        return rp;
    }
}