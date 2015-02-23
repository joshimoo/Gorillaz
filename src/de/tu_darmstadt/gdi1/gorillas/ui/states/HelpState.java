package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class HelpState extends BasicTWLGameState {

    private Image background;
    private Button btnStart;
    private Label helpText;

    private StateBasedGame game;
    private GameContainer cont;


    @Override
    public int getID() {
        return Gorillas.HELPSTATE;
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
        helpText = new Label();
        btnStart = new Button("Back");

        btnStart.addCallback(new Runnable() {
            public void run() {
                game.enterState(Gorillas.MAINMENUSTATE);
            }
        });

        // TODO: Maybe Random name genertion?
        helpText.setEnabled(false);
        helpText.setText(
                "Mainmenue\n" +
                "\n" +
                "    Enter -> new Game\n" +
                "    ESC -> Exit\n" +
                "    m -> Mute\n" +
                "\n" +
                "Game-Setup\n" +
                "\n" +
                "    Enter -> GO\n" +
                "    Tab -> Txt1 - Txt2 - Go-Button (Space aktiviert Button)\n" +
                "\n" +
                "Game-State\n" +
                "ESC / P -> Pause\n" +
                "WASD und Pfeiltasten -> Speed und Winkel\n" +
                "\n" +
                "Pause\n" +
                "ESC / P -> Game"
        );

        rp.add(helpText);
        rp.add(btnStart);
        return rp;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input in_key = gameContainer.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN) || in_key.isKeyPressed(Input.KEY_H)) { game.enterState(Gorillas.MAINMENUSTATE); }
        if (in_key.isKeyPressed(Input.KEY_TAB))
        {
            if(helpText.hasKeyboardFocus()) btnStart.requestKeyboardFocus();
            else helpText.requestKeyboardFocus();
        }
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        // Layout subject to change
        helpText.setSize(256, 300);
        btnStart.setSize(256, 32);
        // Center the Textfields on the screen. Jetzt wird duch 2 geteilt :)
        int x = (paneWidth - helpText.getWidth()) >> 1;

        helpText.setPosition(x, 80);
        btnStart.setPosition(x, 450);
    }
}
