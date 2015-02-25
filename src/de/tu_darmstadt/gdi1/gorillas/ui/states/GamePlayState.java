package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.*;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.entities.Banana;
import de.tu_darmstadt.gdi1.gorillas.entities.Skyline;
import de.tu_darmstadt.gdi1.gorillas.entities.Sun;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import static de.tu_darmstadt.gdi1.gorillas.main.Gorillas.player1;
import static de.tu_darmstadt.gdi1.gorillas.main.Gorillas.player2;

public class GamePlayState extends BasicTWLGameState {

    private static Skyline skyline;
    private static Gorilla gorilla, gorillb;
    private static Sun sun;
    private Image background;

    private ValueAdjusterInt if_speed;
    private ValueAdjusterInt if_angle;
    private Button btnThrow;

    private static Player activePlayer;
    private Banana banana;
    private STATES state;
    private RootPane rp;

    private boolean inverseControlKeys = false;
    private int keyPressDelay = 0;
    private String throwNumber = null;

    /** Die FSM für das spiel ist eigentlich recht simple:
     *      Im INPUT state werden die Eingaben des aktiven Spieles verarbeitet. Wenn einen
     *  Banane geworfen wird, wechseln wir nach THROW. Hier wird die Banane nach den Physikalichen
     *  vorgaben bewegt und am auf Kollisionen geprüft. Beim Auftreten der Kollision wird nach
     *  DMAMGE gewechselt, der schaden berechnet, auf Sieg geprüft und  zu INPUT oder VICTORY
     *  gewechselt.
     */
    private static enum STATES{ INPUT, THROW, DAMAGE, ROUNDVICTORY, VICTORY }

    @Override
    public int getID() {
        return Gorillas.GAMEPLAYSTATE;
    }

    public static Skyline getSkyline(){
        return skyline;
    }

    public static Gorilla getGorilla(int num){
        if(num == 0)
            return gorilla;
        else
            return gorillb;
    }

    public static Sun getSun(){
        return sun;
    }

    public static Player getActivePlayer() { return activePlayer; }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);
        skyline = new Skyline(6);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        int xx = x1 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);
        int yy = x2 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);

        gorilla = new Gorilla(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1));
        gorillb = new Gorilla(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2));

        sun = new Sun(400, 60);

        banana = null;
        if (rp == null)
            this.createRootPane();

        activePlayer = player1;
        state = STATES.INPUT;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        sun.render(g);
        skyline.render(g);
        gorilla.render(g);
        gorillb.render(g);

        if(banana != null) banana.render(g);

        g.setColor(Color.black);    /* Dropshadow TODO: maybe translucent background */
        g.drawString(player2.getName(), gorillb.x - g.getFont().getWidth(player2.getName()) / 2 + 1, gorillb.y - 63);
        g.drawString(player1.getName(), gorilla.x - g.getFont().getWidth(player1.getName()) / 2 + 1, gorilla.y - 63);

        /* We could possibly change the name-color of the active player as an indication */
        if(activePlayer == player1)
            g.setColor(Color.white);
        else
            g.setColor(Color.yellow);
        g.drawString(player2.getName(), gorillb.x - g.getFont().getWidth(player2.getName()) / 2, gorillb.y - 64);
        if(activePlayer == player1)
            g.setColor(Color.yellow);
        else
            g.setColor(Color.white);
        g.drawString(player1.getName(), gorilla.x - g.getFont().getWidth(player1.getName()) / 2, gorilla.y - 64);

        if(state != STATES.THROW) {
            g.setColor(Color.red);
            // Description for the buttons
            g.drawString("Speed", 20, 10);
            g.drawString("Angle ", 20, 50);
        }
        if(throwNumber != null)
        {
            g.setColor(Color.white);
            g.drawString(throwNumber,this.getRootPane().getWidth()-110,20);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input input = gc.getInput();

        gorilla.update(delta);
        gorillb.update(delta);

        /* DEBUG: Reroll the LevelGeneration */
        if(input.isKeyPressed(Input.KEY_Q))
            init(gc, game);

        /* Auf [ESC] muss unabhängig vom state reagiert werden */
        if(input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_P))
            game.enterState(Gorillas.INGAMEPAUSE);

        // Cheat for Admins ;-)
        if (input.isKeyPressed(Input.KEY_V) ) {activePlayer.setWin(3);
            state = STATES.VICTORY;
            System.out.println("V Cheat"); }

        switch (state) {
            case INPUT:
                throwNumber = "Throw Nr " + activePlayer.getThrow();
                btnThrow.setVisible(true);
                if_speed.setEnabled(true);
                if_angle.setEnabled(true);
                if_speed.setVisible(true);
                if_angle.setVisible(true);

                if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_SPACE))
                    throwBanana();


                if(keyPressDelay == 0) {

                    if (inverseControlKeys) {
                        if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_angle.setValue(if_angle.getValue() + 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){ if_angle.setValue(if_angle.getValue() - 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){ if_speed.setValue(if_speed.getValue() + 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)){ if_speed.setValue(if_speed.getValue() - 5); keyPressDelay = 50; }
                    }
                    else {
                        if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_speed.setValue(if_speed.getValue() + 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){ if_speed.setValue(if_speed.getValue() - 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){ if_angle.setValue(if_angle.getValue() + 5); keyPressDelay = 50; }
                        if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)){ if_angle.setValue(if_angle.getValue() - 5); keyPressDelay = 50; }
                    }
                    /*
                    if (inverseControlKeys) {
                        if (input.isKeyPressed(Input.KEY_RIGHT) || input.isKeyPressed(Input.KEY_D)) if_angle.setValue(if_angle.getValue() + 5);
                        if (input.isKeyPressed(Input.KEY_LEFT) || input.isKeyPressed(Input.KEY_A)) if_angle.setValue(if_angle.getValue() - 5);
                        if (input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W)) if_speed.setValue(if_speed.getValue() + 5);
                        if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) if_speed.setValue(if_speed.getValue() - 5);
                    }
                    else {
                        if (input.isKeyPressed(Input.KEY_RIGHT) || input.isKeyPressed(Input.KEY_D)) if_speed.setValue(if_speed.getValue() + 5);
                        if (input.isKeyPressed(Input.KEY_LEFT) || input.isKeyPressed(Input.KEY_A)) if_speed.setValue(if_speed.getValue() - 5);
                        if (input.isKeyPressed(Input.KEY_UP) || input.isKeyPressed(Input.KEY_W)) if_angle.setValue(if_angle.getValue() + 5);
                        if (input.isKeyPressed(Input.KEY_DOWN) || input.isKeyPressed(Input.KEY_S)) if_angle.setValue(if_angle.getValue() - 5);
                    }
                    */
                }
                else
                    keyPressDelay -= 1;
                break;
            case THROW:
                throwNumber = "Throw Nr " + (activePlayer.getThrow() + 1 );
                // During the flight disable inputs
                btnThrow.setVisible(false);
                if_speed.setEnabled(false);
                if_angle.setEnabled(false);
                if_speed.setVisible(false);
                if_angle.setVisible(false);

                banana.update(delta);
                sun.isCollidding(banana);

                /* Banane verlässt das Spielfeld */
                if(banana.getColMask().getMinX() > Gorillas.FRAME_WIDTH
                    || banana.getColMask().getMaxX() < 0
                    || banana.getColMask().getMinY() > Gorillas.FRAME_HEIGHT)
                    state = STATES.DAMAGE;

                if(activePlayer == player2 && gorilla.isCollidding(banana)) {
                    state = STATES.ROUNDVICTORY;
                    System.out.println("Hit Player 1");
                }

                if(activePlayer == player1 && gorillb.isCollidding(banana)) {
                    state = STATES.ROUNDVICTORY;
                    System.out.println("Hit Player 2");
                }

                if(skyline.isCollidding(banana))
                    state = STATES.DAMAGE;

                break;
            case DAMAGE:
                activePlayer.setThrow();
                System.out.println("Throw " + activePlayer.getName() + " Nr" + activePlayer.getThrow());
                throwNumber = "Throw Nr " + activePlayer.getThrow(); // Ueberfluessig
                if(activePlayer == player1) {
                    activePlayer = player2;
                }
                else {
                    activePlayer = player1;
                }

                if_speed.setValue(activePlayer.getLastSpeed());
                if_angle.setValue(activePlayer.getLastAngle());

                skyline.destroy((int)banana.getCenterX(), (int)banana.getCenterY(), 32);
                banana = null;

                // TODO: Claculate PlayerDamage
                // player1.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorilla));
                // player2.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorillb));

                state = STATES.INPUT;
                break;
            case ROUNDVICTORY:
                activePlayer.setWin(1);

                if(activePlayer.getWin() > 2)
                    state = STATES.VICTORY;
                else {
                    System.out.println("Herzlichen Glückwunsch " + activePlayer.getName() + "\nSie haben die Runde gewonnen !");
                    System.out.println("Win Nr" +activePlayer.getWin());
                    // Restart Game
                    init(gc, game);
                }
                break;
            case VICTORY:
                // TODO: VICTORY
                System.out.println("Herzlichen Glückwunsch " + activePlayer.getName() + "\nSie haben das Spiel gewonnen !");
                System.out.println("Win Nr" +activePlayer.getWin());
                game.enterState(Gorillas.INGAMEWIN);
                break;
        }
    }

    @Override
    protected RootPane createRootPane() {
        // Needed for adding the new Input-Elements
        rp = super.createRootPane();

        if_speed= new ValueAdjusterInt();
        if_angle = new ValueAdjusterInt();
        btnThrow = new Button("Throw");

        if_speed.setMinMaxValue(0,200);
        if_speed.setValue(80);

        if_angle.setMinMaxValue(0,360);
        if_angle.setValue(60);

        // Wirkungslos
        btnThrow.setAlignment(Alignment.CENTER);

        btnThrow.addCallback(new Runnable() {
            public void run() {
               throwBanana();
            }
        });

        // Set Size and Position of the Input-Elements
        int basic_x=20;
        int basic_y=10;
        int basic_x_c=35;

        // Labels next to the inputs because of place-conflict the the skyscraper
        int pos=0;
        if_speed.setSize(100, 25);
        if_speed.setPosition(basic_x+60, basic_y+basic_x_c*pos);

        pos=1;
        if_angle.setSize(100, 25);
        if_angle.setPosition(basic_x+60, basic_y+basic_x_c*pos);

        pos=2;
        // Button kleiner und verschoben
        btnThrow.setSize(50, 25);
        btnThrow.setPosition(basic_x+60+20, basic_y+basic_x_c*pos);

        // Add the Input-Elements to the RootPane
        rp.add(if_speed);
        rp.add(if_angle);
        rp.add(btnThrow);
        return rp;
    }

    /** Generates a Banana at the current Player */
    private void throwBanana() {
        System.out.println("Throw Banana " + if_speed.getValue() + " " + if_angle.getValue());

        activePlayer.setLastSpeed(if_speed.getValue());
        activePlayer.setLastAngle(if_angle.getValue());

        if (activePlayer == player1)
            banana = new Banana(gorilla.x, gorilla.y - gorilla.getHeight(), if_angle.getValue(), if_speed.getValue());
        else
            banana = new Banana(gorillb.x, gorillb.y - gorillb.getHeight(), 180 - if_angle.getValue(), if_speed.getValue());

        state = STATES.THROW;
    }


}
