package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class GameSetupState extends BasicTWLGameState {

    private StateBasedEntityManager entityManager;
    private RootPane rp;

    private Image background;
    private Button btnStart;
    private EditField txtName1, txtName2;
    private Label lError;

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

        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);

        txtName1 = new EditField();
        txtName2 = new EditField();
        lError = new Label("");
        //TODO: Hintergrund- und Textfarbe


        txtName1.setText(Gorillas.player1);
        txtName2.setText(Gorillas.player2);

        btnStart = new Button("GO");
        btnStart.addCallback(new Runnable() {
            public void run() {
                saveNamesAndStartGame(game);
            }
        });

        rp.add(txtName1);
        rp.add(txtName2);
        rp.add(btnStart);
        rp.add(lError);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input in_key = gameContainer.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN)) { saveNamesAndStartGame(stateBasedGame); }
        if (in_key.isKeyPressed(Input.KEY_TAB))
        {
            if (txtName1.hasKeyboardFocus()) {
                txtName2.requestKeyboardFocus();
            }
            else {
                txtName1.requestKeyboardFocus();
            }
        }
    }

    @Override
    protected RootPane createRootPane() {
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
        lError.setSize(256, 32);
        // Center the Textfields on the screen. Jetzt wird duch 2 geteilt :)
        int x = (paneWidth - txtName1.getWidth()) >> 1;

        txtName1.setPosition(x, 40);
        txtName2.setPosition(x, 80);
        btnStart.setPosition(x, 120);
        lError.setPosition(x, 160);

    }

    /**
     * Stores the playernames and changes to the GAMEPLAYSTATE
     *
     * @param game StateBasedGame
     */
    private void saveNamesAndStartGame(StateBasedGame game) {
        String n1 = txtName1.getText();
        String n2 = txtName2.getText();

        if (!(n1.isEmpty()) && !(n2.isEmpty()) && !(n1.equals(n2)) && (n1.length() < 13) && (n2.length() < 13)) {
            // Only update player names, if we have valid inputs
            Gorillas.player1 = n1;
            Gorillas.player2 = n2;
            game.enterState(Gorillas.GAMEPLAYSTATE);
        }
        else {
            // Detailed errors
            if (n1.equals(n2)) {
                lError.setText("Both names are equal. Please enter a different name.");
            }
            else if (n1.isEmpty() || n1.length() > 13) {
                lError.setText("Please enter a new name for Player 1.");
            }
            else {
                lError.setText("Please enter a new name for Player 2.");
            }
        }
    }
}
