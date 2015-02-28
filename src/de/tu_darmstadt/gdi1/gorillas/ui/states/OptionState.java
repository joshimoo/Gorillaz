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
    private Button btnInvertKeyControl;

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
        btnInvertKeyControl = new Button("");

        btnOK = new Button("OK");
        lError = new Label("");

        if(GamePlayState.getInverseControlKeys())
            btnInvertKeyControl.setText("UP-Down: Angle - Left-Right: Speed");
        else
            btnInvertKeyControl.setText("UP-Down: Speed - Left-Right: Angle");

        btnOK.addCallback(new Runnable() {
            public void run() {
                GamePlayState.setGravity(valueGravity.getValue());
                game.enterState(Gorillas.MAINMENUSTATE);
            }
        });

        btnInvertKeyControl.addCallback(new Runnable() {
            public void run() {
                if(GamePlayState.getInverseControlKeys())
                {
                    btnInvertKeyControl.setText("UP-Down: Speed - Left-Right: Angle");
                    GamePlayState.setInverseControlKeys(false);
                }
                else
                {
                    btnInvertKeyControl.setText("UP-Down: Angle - Left-Right: Speed");
                    GamePlayState.setInverseControlKeys(true);
                }
            }
        });

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setMinMaxValue(0.0f, 24.79f);
        valueGravity.setValue(9.80665f);

        rp.add(valueGravity);
        rp.add(btnInvertKeyControl);
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

        lError.setSize(128, 32);
        lError.setPosition(20, 100);

        btnOK.setSize(128, 32);
        btnOK.setPosition(20, 140);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        background.draw(-20, -10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        Input in_key = container.getInput();
        if (in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_O)) {
            GamePlayState.setGravity(valueGravity.getValue());
            game.enterState(Gorillas.MAINMENUSTATE);
        }
        if (in_key.isKeyPressed(Input.KEY_M)) GamePlayState.toggleMute();
    }
}
