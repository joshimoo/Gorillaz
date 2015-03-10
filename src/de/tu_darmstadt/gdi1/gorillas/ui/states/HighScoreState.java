package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.utils.SqlGorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class HighScoreState extends BasicTWLGameState {

    private Image background;
    private Button btnStart;

    private StateBasedGame game;
    private SqlGorillas db = new SqlGorillas("data_gorillas.hsc","Gorillas");
    String line = null;

    @Override
    public int getID() {
        return Game.HIGHSCORESTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        this.createRootPane();
        this.game = game;

        refreshScore();
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        btnStart = new Button("Back");

        btnStart.addCallback(new Runnable() {
            public void run() {
                game.enterState(de.tu_darmstadt.gdi1.gorillas.main.Game.MAINMENUSTATE);
            }
        });

        rp.add(btnStart);
        return rp;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
        graphics.setColor(new Color(50,50,50,150));
        graphics.fillRect(0, 0, Gorillas.FRAME_WIDTH, Gorillas.FRAME_HEIGHT);
        if(line != null) {
            graphics.setColor(Color.yellow);
            graphics.drawString(line, 100, 50);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input in_key = gameContainer.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN) || in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_S)) { game.enterState(Game.MAINMENUSTATE); }
    }

    /**
     * Installs the rootPane of this state as the active root pane.
     * Calls createRootPane() on first run.
     *
     * @param container the GameContainer instance
     * @param game      the StateBasedGame instance
     * @throws org.newdawn.slick.SlickException
     * @see #createRootPane()
     */
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        // Get current high score when entering the state
        refreshScore();
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        // Layout subject to change
        btnStart.setSize(256, 32);
        // Center the Textfields on the screen. Jetzt wird duch 2 geteilt :)
        int x = (paneWidth - btnStart.getWidth()) >> 1;

        btnStart.setPosition(x, 500);
    }

    /**
     * Refresh the high score
     */
    private void refreshScore(){
        String[][] highScore_list = db.getHighScore();

        for (int i = 0; i < highScore_list.length; i++)
        {
            if(i == 0)
                line = "Name        Rounds  Won Rounds  WinRate  HitRate\n";
            for (int j = 0; j < 5; j++) {
                line += highScore_list[i][j] + "       ";
            }
            line += "\n";
        }
    }
}
