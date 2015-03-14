package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.utils.SqlGorillas;
import de.tu_darmstadt.gdi1.gorillas.utils.Utils;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.RotateTransition;
import org.newdawn.slick.state.transition.Transition;

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
        return de.tu_darmstadt.gdi1.gorillas.main.Game.GAMESETUPSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        this.createRootPane();
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

        loadPlayerNames();

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
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        loadPlayerNames();
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
        txtName1.setSize(96, 32);
        txtName2.setSize(96, 32);
        btnStart.setSize(64, 32);
        lPlayer1Error.setSize(64, 32);
        lPlayer2Error.setSize(64, 32);

        // Center the Textfields on the screen.
        int x = (paneWidth - txtName1.getWidth()) / 2;

        txtName1.setPosition(x, 40);
        lPlayer1Error.setPosition(x + 96, 40);

        txtName2.setPosition(x, 80);
        lPlayer2Error.setPosition(x + 96, 80);

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
            Game.getInstance().createPlayer(n1);
            Game.getInstance().createPlayer(n2);
            storePlayerNamesToSql(n1, n2);
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
        setPlayer1Error(n1.isEmpty() ? ERROR_IS_EMPTY : n1.length() > Game.getMaxPlayerName() ? ERROR_TO_LONG : "");
        setPlayer2Error(n2.isEmpty() ? ERROR_IS_EMPTY : n2.length() > Game.getMaxPlayerName() ? ERROR_TO_LONG : "");

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

            // Removed the init call, refresh the state dependent variables in enter (gorilla creation)
            // TODO: experiment with transitions
            //game.enterState(Game.GAMEPLAYSTATE, new RotateTransition(Color.blue), new BlobbyTransition(Color.green));
            game.enterState(Game.GAMEPLAYSTATE);

        } else {
            lPlayer1Error.setVisible(!getPlayer1Error().isEmpty());
            lPlayer2Error.setVisible(!getPlayer2Error().isEmpty());
        }
    }

    private void storePlayerNamesToSql(String player1, String player2)
    {
        SqlGorillas sql = new SqlGorillas("player_gorillas.data","Players");
        sql.insertPlayerName(player1);
        sql.insertPlayerName(player2);
    }

    private void loadPlayerNames()
    {
        SqlGorillas sql = new SqlGorillas("player_gorillas.data","Players");
        String[] names = sql.getPlayerName();
        if(names.length < 2) {
            txtName1.setText(Utils.getRandomName());
            txtName2.setText(Utils.getRandomName());
        }
        else
        {
            txtName1.setText(names[0]);
            txtName2.setText(names[1]);
        }
    }
}
