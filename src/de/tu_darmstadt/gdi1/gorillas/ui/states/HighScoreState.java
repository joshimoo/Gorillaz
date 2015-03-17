package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.KeyMap;
import de.tu_darmstadt.gdi1.gorillas.utils.SqlGorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class HighScoreState extends BasicTWLGameState {

    private Image background;
    private Button btnStart;

    private StateBasedGame game;
    String line = null;

    @Override
    public int getID() {
        return Game.HIGHSCORESTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        this.game = game;
        refreshScore();
        if (!Game.getInstance().isTestMode()) {
            background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        }
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();

        btnStart = new Button("Back");
        btnStart.addCallback(() -> game.enterState(Game.MAINMENUSTATE));

        rp.add(btnStart);
        return rp;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
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
        KeyMap.keyPressedStateChange(gameContainer.getInput(),stateBasedGame);
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
        // Center the Textfields on the screen.
        int x = (paneWidth - btnStart.getWidth()) / 2;

        btnStart.setPosition(x, 500);
    }

    /**
     * Refresh the high score
     */
    private void refreshScore(){
        String[][] highScore_list = Database.getInstance().getHighScore();

        for (int i = 0; i < highScore_list.length; i++)
        {
            if(i == 0) {
                line =
                        String.format("%-6s","Place") + "  " +
                        String.format("%-" + Game.MAX_NAMESIZE + "s", "Name") + "  " +
                        String.format("%8s","Rounds") + "  " +
                        String.format("%8s","Wins") + "  " +
                        String.format("%8s","WinRate") + "  " +
                        String.format("%8s","HitRate") + "\n";
            }
                line += String.format("%-6s", (i+1)) + "  ";
            for (int j = 0; j < 5; j++) {
                if(j==0)
                    line += String.format("%-" + Game.MAX_NAMESIZE + "s", highScore_list[i][j]) + "  ";
                else
                    line += String.format("%8s",highScore_list[i][j]) + "  ";
            }
            line += "\n";
        }
    }
}
