package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Alignment;
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
    private RootPane rp;

    private Image background;
    private Button btnStart;
    private EditField txtName1, txtName2;

    public GameSetupState() {
        entityManager = StateBasedEntityManager.getInstance();
    }

    @Override
    public int getID() {
       return Gorillas.GAMESETUPSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        rp = super.createRootPane();

        background = Assets.loadImage(Assets.Images.SETUPMENU_BACKGROUND);

        txtName1 = new EditField();
        txtName2 = new EditField();
        txtName1.setText(Gorillas.player1);
        txtName2.setText(Gorillas.player2);

        btnStart = new Button("! START GAME !");
        btnStart.addCallback(new Runnable() {
            public void run() {
                String n1 = txtName1.getText().trim();
                String n2 = txtName2.getText().trim();

                if(!(n1.isEmpty()) && !(n2.isEmpty()) && !(n1.equals(n2)) && (n1.length() < 13) && (n2.length() < 13)) {
                    // Only update player names, if we have valid inputs
                    Gorillas.player1 = n1;
                    Gorillas.player2 = n2;
                    game.enterState(Gorillas.GAMEPLAYSTATE);
                }
                else {
                    // TODO: User Feedback
                }
            }
        });

        rp.add(txtName1);
        rp.add(txtName2);
        rp.add(btnStart);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, 0, 0);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {}

    @Override
    protected RootPane createRootPane() {
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        final int x = (paneWidth - 256) / 2;

        txtName1.setSize(256, 48);
        txtName2.setSize(256, 48);
        btnStart.setSize(256, 48);

        btnStart.setAlignment(Alignment.CENTER); // Unfortunately you cant align EditFields :(

        txtName1.setPosition(x, 120);
        txtName2.setPosition(x, 240);
        btnStart.setPosition(x, paneHeight - 128);
    }

}
