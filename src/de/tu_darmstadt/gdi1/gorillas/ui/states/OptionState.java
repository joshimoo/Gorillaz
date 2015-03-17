package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class OptionState extends BasicTWLGameState {

    private Image background;
    private ValueAdjusterFloat valueGravity;
    private ValueAdjusterInt valueSound;
    private Button btnOK;
    private Label lError;
    private StateBasedGame game;
    private Button btnInvertKeyControl;
    private Button btnWind;
    private Button btnStorePlayerNames;

    @Override
    public int getID() {
        return Game.OPTIONSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        this.game = game;
        if (!Game.getInstance().isTestMode()) { // Don't load anything in TestMode
            background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        }

    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        valueGravity = new ValueAdjusterFloat();
        btnInvertKeyControl = new Button("");
        btnWind = new Button("");
        btnStorePlayerNames = new Button("");
        valueSound = new ValueAdjusterInt();

        btnOK = new Button("OK");
        lError = new Label("");

        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys()? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");
        btnStorePlayerNames.setText(Game.getInstance().getStorePlayerNames()? "Random PlayerNames" : "Store PlayerNames");

        btnOK.addCallback(this::returnToPrevScreen);
        btnInvertKeyControl.addCallback(this::toggleInverseControlKeys);
        btnWind.addCallback(this::toggleWind);
        btnStorePlayerNames.addCallback(this::toggleStorePlayerNames);

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setMinMaxValue(Game.GRAVITY_MIN, Game.GRAVITY_MAX);
        valueGravity.setValue(Game.GRAVITY_DEFAULT);

        valueSound.setMinMaxValue((int) (Game.SOUND_VOLUME_MIN * 100), (int) (Game.SOUND_VOLUME_MAX * 100));
        valueSound.setValue((int) (Game.SOUND_VOLUME_DEFAULT * 100));

        rp.add(valueGravity);
        rp.add(btnInvertKeyControl);
        rp.add(btnWind);
        rp.add(btnStorePlayerNames);
        rp.add(btnOK);
        rp.add(lError);
        rp.add(valueSound);
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        valueGravity.setSize(128, 32);
        valueGravity.setPosition(Gorillas.FRAME_WIDTH / 2 - 64, 20);

        btnInvertKeyControl.setSize(256, 32);
        btnInvertKeyControl.setPosition(Gorillas.FRAME_WIDTH / 2 - 128, 60);

        btnWind.setSize(128, 32);
        btnWind.setPosition(Gorillas.FRAME_WIDTH / 2 - 64,100);

        valueSound.setSize(128, 32);
        valueSound.setPosition(Gorillas.FRAME_WIDTH / 2 - 64, 140);

        btnStorePlayerNames.setSize(168, 32);
        btnStorePlayerNames.setPosition(Gorillas.FRAME_WIDTH / 2 - 84,180);

        lError.setSize(128, 32);
        lError.setPosition(Gorillas.FRAME_WIDTH / 2 -64, 220);

        btnOK.setSize(128, 32);
        btnOK.setPosition(Gorillas.FRAME_WIDTH / 2 -64, 260);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics gr) throws SlickException {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
        gr.drawImage(background, -10, -20);
        gr.setColor(Color.black);
        gr.drawString("Gravity", valueGravity.getX() - valueGravity.getWidth() / 2, valueGravity.getY());
        gr.drawString("Volume", valueSound.getX() - valueGravity.getWidth() / 2, valueSound.getY());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        Input in_key = container.getInput();

        if (in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_O) || in_key.isKeyPressed(Input.KEY_ENTER)) { returnToPrevScreen();}
        if (in_key.isKeyPressed(Input.KEY_M)) { Game.getInstance().toggleMute(); }
        if (in_key.isKeyPressed(Input.KEY_UP)) { valueGravity.setValue(valueGravity.getValue() + 1); }
        if (in_key.isKeyPressed(Input.KEY_DOWN)) { valueGravity.setValue(valueGravity.getValue() - 1); }
        if (in_key.isKeyPressed(Input.KEY_C)) { toggleInverseControlKeys(); }
        if (in_key.isKeyPressed(Input.KEY_W)) { toggleWind(); }
    }

    private void returnToPrevScreen() {
        Game.getInstance().setGravity(valueGravity.getValue());
        Game.getInstance().setSoundVolume(valueSound.getValue() / 100f);
        game.enterState(game.getLastStateID());
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

     private void toggleStorePlayerNames() {
        Game.getInstance().toggleStorePlayerNames();
         btnStorePlayerNames.setText(Game.getInstance().getStorePlayerNames()? "Random PlayerNames" : "Store PlayerNames");
    }

    private void toggleMute() {
        Game.getInstance().toggleMute();
        btnInvertKeyControl.setText(Game.getInstance().isMute()? "Unmute" : "Mute");
    }


    /*
        Save Settings
        Game.getInstance().


        getWind()
        getInverseControlKeys
        getGravity
        getSoundVolume
        getStorePlayerNames

        isMute
        getDebug
        isTestMode

        isDeveloper




     */
}
