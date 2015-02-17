package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.entitys.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.ui.entitys.Skyscraper;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
    private Image background;

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
        background = new Image("assets/menu/background.png");
        skys = new Skyscraper[6];
        skys[0] = new Skyscraper(0);
        skys[1] = new Skyscraper(1);
        skys[2] = new Skyscraper(2);
        skys[3] = new Skyscraper(3);
        skys[4] = new Skyscraper(4);
        skys[5] = new Skyscraper(5);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        gorilla1 = new Gorilla(x1 * (Gorillas.FRAME_WIDTH/6) + Gorillas.FRAME_WIDTH/12, Gorillas.FRAME_HEIGHT - skys[x1].getHeigth());
        gorilla2 = new Gorilla(x2 * (Gorillas.FRAME_WIDTH/6) + Gorillas.FRAME_WIDTH/12, Gorillas.FRAME_HEIGHT - skys[x2].getHeigth());

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -20, -10);
        for(Skyscraper element:skys){
            element.render(graphics);
        }
        gorilla1.render(graphics);
        gorilla2.render(graphics);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}
