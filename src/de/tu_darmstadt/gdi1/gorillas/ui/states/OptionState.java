package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class OptionState extends BasicTWLGameState {

    private Image background;
    private ValueAdjusterFloat valueGravity;
    private Button btnOK;
    private Label lError;
    private StateBasedGame game;
    private Button btnInvertKeyControl;
    private Button btnWind;

    @Override
    public int getID() {
        return Game.OPTIONSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        this.game = game;
        this.createRootPane();

    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        valueGravity = new ValueAdjusterFloat();
        btnInvertKeyControl = new Button("");
        btnWind = new Button("");

        btnOK = new Button("OK");
        lError = new Label("");

        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys()? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");

        btnOK.addCallback(this::returnToPrevScreen);
        btnInvertKeyControl.addCallback(this::toggleInverseControlKeys);
        btnWind.addCallback(this::toggleWind);

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setMinMaxValue(Game.GRAVITY_MIN, Game.GRAVITY_MAX);
        valueGravity.setValue(Game.GRAVITY_DEFAULT);

        rp.add(valueGravity);
        rp.add(btnInvertKeyControl);
        rp.add(btnWind);
        rp.add(btnOK);
        rp.add(lError);
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        valueGravity.setSize(128, 32);
        valueGravity.setPosition(20, 20);

        btnInvertKeyControl.setSize(255, 32);
        btnInvertKeyControl.setPosition(20, 60);

        btnWind.setSize(128, 32);
        btnWind.setPosition(20,100);

        lError.setSize(128, 32);
        lError.setPosition(20, 140);

        btnOK.setSize(128, 32);
        btnOK.setPosition(20, 180);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        Input in_key = container.getInput();
        if (in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_O)) { returnToPrevScreen();}
        if (in_key.isKeyPressed(Input.KEY_M)) { Game.getInstance().toggleMute(); }
        if (in_key.isKeyPressed(Input.KEY_UP)) { valueGravity.setValue(valueGravity.getValue() + 1); }
        if (in_key.isKeyPressed(Input.KEY_DOWN)) { valueGravity.setValue(valueGravity.getValue() - 1); }
        if (in_key.isKeyPressed(Input.KEY_C)) { toggleInverseControlKeys(); }
        if (in_key.isKeyPressed(Input.KEY_W)) { toggleWind(); }
    }

    private void returnToPrevScreen() {
        Game.getInstance().setGravity(valueGravity.getValue());
        game.enterState(Game.MAINMENUSTATE);
    }

    // TODO: Map Text Strings to Constants
    private void toggleWind() {
        Game.getInstance().toggleWind();
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");
    }

    private void toggleInverseControlKeys() {
        Game.getInstance().toggleInverseControlKeys();
        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys()? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
    }

}
