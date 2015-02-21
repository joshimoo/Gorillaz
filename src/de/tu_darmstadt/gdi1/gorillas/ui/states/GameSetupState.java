package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class GameSetupState extends BasicTWLGameState {

    private StateBasedEntityManager entityManager;

    private Image background;
    private Button btnStart;
    private EditField txtName1, txtName2;
    private StateBasedGame game;

    public GameSetupState() {
        entityManager = StateBasedEntityManager.getInstance();
    }

    @Override
    public int getID() {
       return Gorillas.GAMESETUPSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        this.createRootPane();
        this.game=game;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {}

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        txtName1 = new EditField();
        txtName2 = new EditField();
        btnStart = new Button("GO");

        btnStart.addCallback(new Runnable() {
            public void run() {
                String n1 = txtName1.getText();
                String n2 = txtName2.getText();

                if(!(n1.isEmpty()) && !(n2.isEmpty()) && !(n1.equals(n2)) && (n1.length() < 13) && (n2.length() < 13)) {
                    // Only update player names, if we have valid inputs
                    Gorillas.player1 = n1;
                    Gorillas.player2 = n2;
                    game.enterState(Gorillas.GAMEPLAYSTATE);
                }
                else {
                    btnStart.setText("Error");
                }
            }
        });

        txtName1.setText(Gorillas.player1);
        txtName2.setText(Gorillas.player2);

        rp.add(txtName1);
        rp.add(txtName2);
        rp.add(btnStart);
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        // Layout subject to change
        txtName1.setSize(256, 32);
        txtName2.setSize(256, 32);
        btnStart.setSize(256, 32);

        // Center the Textfields on the screen. Jetzt wird duch 2 geteilt :)
        int x = (paneWidth - txtName1.getWidth()) >> 1;

        txtName1.setPosition(x,  40);
        txtName2.setPosition(x,  80);
        btnStart.setPosition(x, 120);

    }

}
