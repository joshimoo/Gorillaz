package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class GameSetupState extends BasicTWLGameState {

    private Image background;
    private Button btnStart;
    private EditField txtName1, txtName2;

    private StateBasedGame game;
    private GameContainer cont;
    private Label lPlayer1Error;
    private Label lPlayer2Error;

    @Override
    public int getID() {
        return Gorillas.GAMESETUPSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        // this.createRootPane();
        this.game = game;
        this.cont = gameContainer;
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        txtName1 = new EditField();
        txtName2 = new EditField();
        btnStart = new Button("GO");
        lPlayer1Error = new Label("");
        lPlayer2Error = new Label("");

        btnStart.addCallback(new Runnable() {
            public void run() {
                saveNamesAndStartGame(game, cont);
            }
        });

        // TODO: Maybe Random name genertion?
        txtName1.setText("Player1");
        txtName2.setText("Player2");

        rp.add(txtName1);
        rp.add(txtName2);
        rp.add(btnStart);
        rp.add(lPlayer1Error);
        rp.add(lPlayer2Error);
        return rp;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input in_key = gameContainer.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN)) { saveNamesAndStartGame(stateBasedGame, cont); }
        if (in_key.isKeyPressed(Input.KEY_TAB))
        {
            if (txtName1.hasKeyboardFocus()) txtName2.requestKeyboardFocus();
            else if(txtName2.hasKeyboardFocus()) btnStart.requestKeyboardFocus();
            else txtName1.requestKeyboardFocus();
        }
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        // Layout subject to change
        txtName1.setSize(64, 32);
        txtName2.setSize(64, 32);
        btnStart.setSize(64, 32);
        lPlayer1Error.setSize(64, 32);
        lPlayer2Error.setSize(64, 32);

        // Center the Textfields on the screen.
        int x = (paneWidth - txtName1.getWidth()) / 2;

        txtName1.setPosition(x, 40);
        lPlayer1Error.setPosition(x + 64, 40);

        txtName2.setPosition(x, 80);
        lPlayer2Error.setPosition(x + 64, 80);

        btnStart.setPosition(x, 120);
    }

    // Placeholder Implementation TODO: Add Translator Support
    public static final String ERROR_DUPLICATE = "Names are equal, You must enter different names.";
    public static final String ERROR_IS_EMPTY = "Name must not be empty.";
    public static final String ERROR_TO_LONG = "Name is to long.";

    /**
     * Will only set the new player names if they are valid,
     * @see de.tu_darmstadt.gdi1.gorillas.ui.states.GameSetupState#checkValidPlayerNames(String, String)
     * @param n1 name of player 1
     * @param n2 name of player 2
     * @return true when both names are valid
     */
    public Boolean setPlayerNames(String n1, String n2) {
        if (checkValidPlayerNames(n1,n2)) {
            Gorillas.player1 = new Player(n1);
            Gorillas.player2 = new Player(n2);
            return true;
        }

        return false;
    }

    /**
     * Checks if the passed names are valid
     * Sets error messages, when inputs are not valid
     * @param n1 name of player 1
     * @param n2 name of player 2
     * @return true when both names are valid
     */
    private Boolean checkValidPlayerNames(String n1, String n2) {
        setPlayer1Error(n1.isEmpty() ? ERROR_IS_EMPTY : n1.length() > 12 ? ERROR_TO_LONG : "");
        setPlayer2Error(n2.isEmpty() ? ERROR_IS_EMPTY : n2.length() > 12 ? ERROR_TO_LONG : "");

        // Only check for duplicates if we have valid inputs
        if (!n1.isEmpty() && n1.equals(n2)) {
            setPlayer1Error(ERROR_DUPLICATE);
            setPlayer2Error(ERROR_DUPLICATE);
        }

        return getPlayer1Error().isEmpty() && getPlayer2Error().isEmpty();
    }

    // Wrap the internal label
    public void setPlayer1Error(String error) {lPlayer1Error.setText(error);}
    public void setPlayer2Error(String error) {lPlayer2Error.setText(error);}
    public String getPlayer1Error() { return lPlayer1Error.getText(); }
    public String getPlayer2Error() { return lPlayer2Error.getText(); }

    /**
     * Stores the playernames and changes to the GAMEPLAYSTATE
     *
     * @param game StateBasedGame
     */
    private void saveNamesAndStartGame(StateBasedGame game, GameContainer cont) {
        String n1 = txtName1.getText();
        String n2 = txtName2.getText();

        if (setPlayerNames(n1, n2)) {
            lPlayer1Error.setVisible(false);
            lPlayer2Error.setVisible(false);

            // TODO: should not be necessary since StateBasedGame should call all init methods for the each entry in the init states list
            try { game.getState(Gorillas.GAMEPLAYSTATE).init(cont, game); }
            catch (SlickException e) { e.printStackTrace(); }
            game.enterState(Gorillas.GAMEPLAYSTATE);

        } else {
            lPlayer1Error.setVisible(!getPlayer1Error().isEmpty());
            lPlayer2Error.setVisible(!getPlayer2Error().isEmpty());
        }
    }
}
