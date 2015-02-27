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
    private Button btnHelp;
    private Button btnOptions;
    private Button btnExit;
    private Button btnMute;
    private StateBasedGame game;

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

        this.createRootPane();
        this.game = game;
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();

        btnNewGame = new Button("New Game");
        btnNewGame.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.GAMESETUPSTATE);
            }
        });

        btnHelp = new Button("Help");
        btnHelp.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.HELPSTATE);
            }
        });

        btnOptions = new Button("Options");
        btnOptions.addCallback(new Runnable(){
            public void run(){ game.enterState(Gorillas.OPTIONSTATE);}
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
                System.out.println("Mute");
            }
        });

        rp.add(btnNewGame);
        rp.add(btnHelp);
        rp.add(btnOptions);
        rp.add(btnExit);
        rp.add(btnMute);
		return rp;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        entityManager.updateEntities(container, game, delta);
        Input in_key = container.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN)) game.enterState(Gorillas.GAMESETUPSTATE);
        if (in_key.isKeyPressed(Input.KEY_ESCAPE)) System.exit(0);
        if (in_key.isKeyPressed(Input.KEY_M)) {/* TODO: Mute me :) */ System.out.println("Mute");}
        if (in_key.isKeyPressed(Input.KEY_H)) game.enterState(Gorillas.HELPSTATE);
        if (in_key.isKeyPressed(Input.KEY_M)) game.enterState(Gorillas.OPTIONSTATE);
    }

	@Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -10, -20);
        entityManager.renderEntities(container, game, g);
    }
	
   
    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        btnNewGame.setSize(128, 32);
        btnNewGame.setPosition(20, 20);

        btnHelp.setSize(128, 32);
        btnHelp.setPosition(20, 60);

        btnOptions.setSize(128, 32);
        btnOptions.setPosition(20, 100);

        btnExit.setSize(128, 32);
        btnExit.setPosition(20, 140);

        btnMute.setSize(64, 64);
        btnMute.setPosition(0, paneHeight - btnMute.getHeight());
    }

}
