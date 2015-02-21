package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicTWLGameState {

    private StateBasedEntityManager entityManager;
    private Image background;
    private Button btnNewGame;
    private Button btnExit;
    private Button btnMute;
    private RootPane rp;

    public MainMenuState() {
        entityManager = StateBasedEntityManager.getInstance();
    }

    @Override
    public int getID() {
        return Gorillas.MAINMENUSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);

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
                // TODO: Mute me :)
            }
        });

        rp.add(btnNewGame);
        rp.add(btnExit);
        rp.add(btnMute);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        entityManager.updateEntities(container, game, delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -10, -20);
        entityManager.renderEntities(container, game, g);
    }

    @Override
    protected RootPane createRootPane() {
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        btnNewGame.setSize(128, 32);
        btnNewGame.setPosition(20, 20);

        btnExit.setSize(128, 32);
        btnExit.setPosition(20, 60);

        btnMute.setSize(64, 64);
        btnMute.setPosition(0, paneHeight - btnMute.getHeight());
    }

}
