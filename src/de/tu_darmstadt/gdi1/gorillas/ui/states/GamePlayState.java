package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.entitys.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.ui.entitys.Skyscraper;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Tamara on 17.02.2015.
 */
public class GamePlayState extends BasicTWLGameState {

    private int stateID;
    private StateBasedEntityManager entityManager;
    private RootPane rp;
    private Skyscraper[] skys;
    private Gorilla gorilla1;
    private Gorilla gorilla2;

    public GamePlayState(int sid) {
        stateID = sid;
        entityManager = StateBasedEntityManager.getInstance();
    }

    @Override
    public int getID() {
        return stateID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        skys = new Skyscraper[6];
        skys[0] = new Skyscraper();
        skys[1] = new Skyscraper();
        skys[2] = new Skyscraper();
        skys[3] = new Skyscraper();
        skys[4] = new Skyscraper();
        skys[5] = new Skyscraper();

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        gorilla1 = new Gorilla(x1 * (Gorillas.FRAME_WIDTH/6)) - Gorilla.WIDTH, skys[x1].getHeight);
        gorilla2 = new Gorilla(x2 * (Gorillas.FRAME_WIDTH/6)) - Gorilla.WIDTH, skys[x2].getHeight);

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
