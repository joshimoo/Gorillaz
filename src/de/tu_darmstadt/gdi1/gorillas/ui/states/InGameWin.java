package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class InGameWin extends BasicTWLGameState {
    private Image background;
    private Color color = new Color(50,50,50,150);
    private Button btnNewGame;
    private Button btnMainMenu;
    private RootPane rp;
    private StateBasedGame game;
    private Label lWin;

    @Override
    public int getID() {
        return Gorillas.INGAMEWIN;
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
        Input in_key = container.getInput();
        if(in_key.isKeyPressed(Input.KEY_ESCAPE)){
            game.enterState(Gorillas.MAINMENUSTATE);
        }

        if (in_key.isKeyPressed(Input.KEY_RETURN)) game.enterState(Gorillas.GAMESETUPSTATE);
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        lWin.setSize(128, 32);
        lWin.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 150);

        btnNewGame.setSize(128, 32);
        btnNewGame.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 450);

        btnMainMenu.setSize(128,32);
        btnMainMenu.setPosition((Gorillas.FRAME_WIDTH >> 1) - 64, 500);
    }

    @Override
    protected RootPane createRootPane() {
        rp = super.createRootPane();

        lWin = new Label("");

        if(GamePlayState.getActivePlayer() != null) {
            Player player = GamePlayState.getActivePlayer();
            if(player != null)
            {
                lWin.setText("Herzlichen Glückwunsch " + player.getName() +"\nSie haben die Runde gewonnen !\nSie benötigten "+ player.getThrow() + " Würfe.");
            }
        }


        btnNewGame = new Button("New Game");
        btnNewGame.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.GAMESETUPSTATE);
            }
        });

        btnMainMenu = new Button("OK");
        btnMainMenu.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.MAINMENUSTATE);
            }
        });

        rp.add(btnNewGame);
        rp.add(btnMainMenu);
        rp.add(lWin);

        return rp;
    }
}