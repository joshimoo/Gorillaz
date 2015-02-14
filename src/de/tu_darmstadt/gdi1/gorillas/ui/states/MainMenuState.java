package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicTWLGameState {

    private int stateID;
    private StateBasedEntityManager entityManager;

    private Button newGameButton;

    public MainMenuState(int sid) {
        stateID = sid;
        entityManager = StateBasedEntityManager.getInstance();
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        entityManager.updateEntities(container, game, delta);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

        entityManager.renderEntities(container, game, g);
    }

    @Override
    public int getID() {
        return stateID;
    }

    @Override
    protected RootPane createRootPane() {

        RootPane rp = super.createRootPane();

        newGameButton = new Button();
        newGameButton.addCallback(new Runnable() {
            public void run() {
                // TODO: Enter next state
            }
        });

        rp.add(newGameButton);
        return rp;
    }

    @Override
    protected void layoutRootPane() {

        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        newGameButton.adjustSize();
        newGameButton.setPosition(paneWidth / 2 - newGameButton.getWidth() / 2, paneHeight / 2 - newGameButton.getHeight() / 2);
    }
}
