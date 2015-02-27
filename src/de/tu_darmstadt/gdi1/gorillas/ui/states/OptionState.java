package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class OptionState extends BasicTWLGameState {

    private Image background;
    private ValueAdjusterFloat valueGravity;
    private Button btnOK;
    private Label lError;
    private StateBasedGame game;

    @Override
    public int getID() {
        return Gorillas.OPTIONSTATE;
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

        btnOK = new Button("OK");
        lError = new Label("");

        btnOK.addCallback(new Runnable() {
            public void run() {

                GamePlayState.setGravity(valueGravity.getValue());
                game.enterState(Gorillas.MAINMENUSTATE);
            }
        });

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setMinMaxValue(0.0f, 24.79f);
        valueGravity.setValue(9.80665f);

        rp.add(valueGravity);
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

        lError.setSize(128, 32);
        lError.setPosition(20, 60);

        btnOK.setSize(128, 32);
        btnOK.setPosition(20, 100);

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        background.draw(-20, -10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        Input in = container.getInput();
        if (in.isKeyPressed(Input.KEY_ESCAPE)) {
            GamePlayState.setGravity(valueGravity.getValue());
            game.enterState(Gorillas.MAINMENUSTATE);
        }

    }
}
