package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.*;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjusterFloat;
import de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjusterInt;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.KeyMap;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.StateBasedGame;


public class OptionState extends BasicTWLGameState {

    private Image background;
    private AdvancedValueAdjusterFloat valueGravity;
    private AdvancedValueAdjusterInt valueSound;
    private Button btnOK;
    private Label lError;
    private StateBasedGame game;
    private Button btnInvertKeyControl;
    private Button btnWind;
    private Button btnStorePlayerNames;
    private Button btnMute;
    private Button btnSaveToFile;
    private EditField resolution;

    @Override
    public int getID() {
        return Game.OPTIONSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        this.game = game;
        if (!Game.getInstance().isTestMode()) { // Don't load anything in TestMode
            background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
            if(Game.BACKGROUND_SCALE != 1) background = background.getScaledCopy(Game.BACKGROUND_SCALE);
        }
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        valueGravity = new AdvancedValueAdjusterFloat();
        valueSound = new AdvancedValueAdjusterInt();
        btnInvertKeyControl = new Button("");
        btnStorePlayerNames = new Button("");
        btnWind = new Button("");
        btnMute = new Button("");
        btnSaveToFile = new Button("Save Configuration to File");
        btnOK = new Button("OK");
        lError = new Label("");
        resolution = new EditField();
        resolution.setMaxTextLength(9);
        // TODO: reactivate if all GUI can scale
        resolution.setVisible(false);
        resolution.setEnabled(false);

        resolution.setText(Database.getInstance().getDisplayWidth() + "x" + Database.getInstance().getDisplayHeight());
        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys() ? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");
        btnStorePlayerNames.setText(Game.getInstance().getStorePlayerNames() ? "Store PlayerNames" : "Random PlayerNames");
        btnMute.setText(Game.getInstance().isMute() ? "Sound off" : "Sound off");
        btnOK.addCallback(this::returnToPrevScreen);
        btnInvertKeyControl.addCallback(this::toggleInverseControlKeys);
        btnWind.addCallback(this::toggleWind);
        btnStorePlayerNames.addCallback(this::toggleStorePlayerNames);
        btnMute.addCallback(this::toggleMute);
        btnSaveToFile.addCallback(this::saveConfigToFile);

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setMinMaxValue(Game.GRAVITY_MIN, Game.GRAVITY_MAX);
        valueGravity.setValue(Game.getInstance().getGravity());

        valueSound.setMinMaxValue((int) (Game.SOUND_VOLUME_MIN * 100), (int) (Game.SOUND_VOLUME_MAX * 100));
        valueSound.setValue((int) (Game.getInstance().getSoundVolume() * 100));

        rp.add(valueGravity);
        rp.add(btnInvertKeyControl);
        rp.add(btnWind);
        rp.add(btnStorePlayerNames);
        rp.add(btnOK);
        rp.add(lError);
        rp.add(valueSound);
        rp.add(btnMute);
        rp.add(btnSaveToFile);
        rp.add(resolution);
        return rp;
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        valueGravity.setSize(128, 32);
        valueGravity.setPosition((Gorillas.FRAME_WIDTH - valueGravity.getWidth()) / 2, 20);

        btnInvertKeyControl.setSize(256, 32);
        btnInvertKeyControl.setPosition((Gorillas.FRAME_WIDTH - btnInvertKeyControl.getWidth()) / 2, 60);

        btnWind.setSize(128, 32);
        btnWind.setPosition((Gorillas.FRAME_WIDTH - btnWind.getWidth()) / 2,100);

        valueSound.setSize(128, 32);
        valueSound.setPosition((Gorillas.FRAME_WIDTH - valueSound.getWidth()) / 2, 140);

        btnStorePlayerNames.setSize(168, 32);
        btnStorePlayerNames.setPosition((Gorillas.FRAME_WIDTH - btnStorePlayerNames.getWidth()) / 2,180);

        btnMute.setSize(80, 32);
        btnMute.setPosition((Gorillas.FRAME_WIDTH - btnMute.getWidth()) / 2,220);

        resolution.setSize(100, 32);
        resolution.setPosition((Gorillas.FRAME_WIDTH - resolution.getWidth()) / 2,260);

        btnOK.setSize(128, 32);
        btnOK.setPosition((Gorillas.FRAME_WIDTH - btnOK.getWidth()) / 2, 300);

        lError.setSize(128, 64);
        lError.setPosition(Gorillas.FRAME_WIDTH / 2 - 250, 340);

        btnSaveToFile.setSize(190, 32);
        btnSaveToFile.setPosition((Gorillas.FRAME_WIDTH - btnSaveToFile.getWidth()) / 2, Gorillas.FRAME_HEIGHT - 100);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics gr) throws SlickException {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
        gr.drawImage(background, 0, 0);
        gr.setColor(Color.black);
        gr.drawString("Gravity", valueGravity.getX() - valueGravity.getWidth() / 1.8f, valueGravity.getY() + 5);
        gr.drawString("Volume", valueSound.getX() - valueGravity.getWidth() / 1.8f, valueSound.getY() + 5);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int i) throws SlickException {
        Input in_key = container.getInput();

        KeyMap.globalKeyPressedActions(in_key, game);

        if (in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_O) || in_key.isKeyPressed(Input.KEY_ENTER)) { returnToPrevScreen();}
        if (in_key.isKeyPressed(Input.KEY_UP)) { valueGravity.setValue(valueGravity.getValue() + 1); }
        if (in_key.isKeyPressed(Input.KEY_DOWN)) { valueGravity.setValue(valueGravity.getValue() - 1); }
        if (in_key.isKeyPressed(Input.KEY_C)) { toggleInverseControlKeys(); }
        if (in_key.isKeyPressed(Input.KEY_W)) { toggleWind(); }
        if (in_key.isKeyPressed(Input.KEY_P)) { toggleStorePlayerNames();}
        if (in_key.isKeyPressed(Input.KEY_M))  { toggleMute();}
    }

    private void returnToPrevScreen() {

        if(resolution.isVisible()) {
            String resolutionString = resolution.getText();
            int splitter = resolutionString.indexOf("x");
            int x = Integer.parseInt(resolutionString.substring(0, splitter));
            int y = Integer.parseInt(resolutionString.substring(splitter + 1, resolutionString.length()));

            if (checkResolution(x, y)) {
                Database db = Database.getInstance();
                db.setDisplayWidth(x);
                db.setDisplayHeight(y);

            // TODO: Canvas vs Frame
            float scale = 1024 / 800;
            db.setCanvasWidth(db.getDisplayWidth() * (int) scale);
            db.setCanvasHeight(db.getDisplayWidth() * (int) scale);
            }
            else {
                lError.setText("No valid resolution.\n" +
                        "Use 4/3 300x225 600x450 800x600 1024x768 1280x960 1600x1200 1920x1440\n" +
                        "or 16/10        600x375 800x500 1024x640 1280x800 1600x1000 1920x1200\n" +
                        "or 16/9                 800x450 1024x576 1280x720  1600x900 1920x1080\n");

                return;
            }
        }
        Game.getInstance().setGravity(valueGravity.getValue());
        Game.getInstance().setSoundVolume(valueSound.getValue() / 100f);
        game.enterLastState();
    }

    private boolean checkResolution(int x ,int y)
    {
        boolean result = y > 100 &&
                        ((x == 480) || (x == 480) || (x == 600) || (x == 800) || (x == 1024) || (x == 1280) || (x == 1600) || (x == 1920)) &&
                        ((x / y == 4/3) || (x / y == 10/9) || (x / y == 16/9));
        if (Game.getInstance().getDebug()) { System.out.println("Resolution valid: "+result + " x="+x + " y=" +y); }
        return result;
    }

    // TODO: Map Text Strings to Constants
    private void toggleWind() {
        Game.getInstance().toggleWind();
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");
    }

    private void toggleInverseControlKeys() {
        Game.getInstance().toggleInverseControlKeys();
        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys() ? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
    }

     private void toggleStorePlayerNames() {
         Game.getInstance().toggleStorePlayerNames();
         btnStorePlayerNames.setText(Game.getInstance().getStorePlayerNames() ? "Store PlayerNames" : "Random PlayerNames");
    }

    private void toggleMute() {
        Game.getInstance().toggleMute();
        btnMute.setText(Game.getInstance().isMute() ? "Sound off" : "Sound on");
    }

    private void refreshGUI()
    {
        /*
            Reset GUI
         */
        btnInvertKeyControl.setText(Game.getInstance().getInverseControlKeys() ? "UP-Down: Speed - Left-Right: Angle" : "UP-Down: Angle - Left-Right: Speed");
        btnWind.setText(Game.getInstance().getWind() ? "Wind" : "No wind");
        btnStorePlayerNames.setText(Game.getInstance().getStorePlayerNames() ? "Store PlayerNames" : "Random PlayerNames");
        btnMute.setText(Game.getInstance().isMute() ? "Sound off" : "Sound on");

        //Max ist Gravitationsbeschleunigung des Jupiters
        valueGravity.setValue(Game.getInstance().getGravity());
        valueSound.setValue((int) (Game.SOUND_VOLUME_DEFAULT * 100));
    }

    public void saveConfigToFile() {
        Game.getInstance().setGravity(valueGravity.getValue());
        Game.getInstance().setSoundVolume(valueSound.getValue() / 100f);
        Database.getInstance().saveConfigToFile();
    }
}
