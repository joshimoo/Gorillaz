package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.*;
import de.tu_darmstadt.gdi1.gorillas.entities.factories.CloudFactory;
import de.tu_darmstadt.gdi1.gorillas.entities.factories.PlayerFactory;
import de.tu_darmstadt.gdi1.gorillas.entities.factories.SunFactory;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.utils.SqlGorillas;
import eea.engine.entity.*;
import eea.engine.entity.Entity;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicTWLGameState {

    // Key Handling
    private float keyPressDelay = 0;
    private final float keyPressWaitTime = 0.1f; // wait 100 ms TODO: experiment with these

    // DEBUG
    private String throwNumber = null;
    private String roundWinMessage = null;

    // UI
    private RootPane rp;
    private ValueAdjusterInt if_speed;
    private ValueAdjusterInt if_angle;
    private Button btnThrow;

    // GameState
    private STATES  state;
    private int     windSpeed;
    private Image   background;
    private float   gravity = 9.80665f;

    // Entitys
    private StateBasedEntityManager entityManager;
    private Banana  banana;
    private Skyline skyline;
    private Gorilla gorilla;
    private Gorilla gorillb;
    private Entity sun;
    private Entity cloud;

    // Switchs
    private static boolean inverseControlKeys = false;
    private boolean admin = true;
    private static boolean mute = false;
    private static boolean wind = false;

    // Counter
    private static int totalRoundCounter = 0;


    public Player getActivePlayer() { return Game.getInstance().getActivePlayer(); }
    public void setActivePlayer(Player activePlayer) {
        Game.getInstance().setActivePlayer(activePlayer);
    }

    /** Die FSM für das spiel ist eigentlich recht simple:
     *      Im INPUT state werden die Eingaben des aktiven Spieles verarbeitet. Wenn einen
     *  Banane geworfen wird, wechseln wir nach THROW. Hier wird die Banane nach den Physikalichen
     *  vorgaben bewegt und am auf Kollisionen geprüft. Beim Auftreten der Kollision wird nach
     *  DMAMGE gewechselt, der schaden berechnet, auf Sieg geprüft und  zu INPUT oder VICTORY
     *  gewechselt.
     */
    private static enum STATES{ INPUT, THROW, DAMAGE, ROUNDVICTORY, VICTORY }

    public GamePlayState(){ entityManager = StateBasedEntityManager.getInstance(); }

    @Override
    public int getID() {
        return de.tu_darmstadt.gdi1.gorillas.main.Game.GAMEPLAYSTATE;
    }

    public Skyline getSkyline(){
        return skyline;
    }

    public Gorilla getGorilla(int num){
        return num == 0 ? gorilla : gorillb;
    }

    public Entity getSun(){
        return sun;
    }

    public void setGravity(final float g){
        gravity = g;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        // TODO: Init should really only be called once
        // for a temporary workaround you can clear all entities at the beginning of init
        // or you can use entityManager.setEntityListByState(getID(), initEntitiesList);
        // which will lead to overwritting the list, and then the old entities from last init will be gc'd
        entityManager.clearEntitiesFromState(getID());
        background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);

        // TODO: Switch over to Building Entity
        skyline = new Skyline(6);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        int xx = x1 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);
        int yy = x2 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);

        gorilla = new Gorilla(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1));
        gorillb = new Gorilla(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2));

        sun = SunFactory.createSun(new Vector2f(400, 60));
        entityManager.addEntity(getID(), sun);

        if(wind) windSpeed = (int) ((Math.random() * 30) - 15);
        else windSpeed = 0;
        cloud = CloudFactory.createCloud(new Vector2f(0, 60), windSpeed);
        entityManager.addEntity(getID(), cloud);

        banana = null;
        if (rp == null)
            this.createRootPane();

        setActivePlayer(Game.getInstance().getPlayer(0));
        state = STATES.INPUT;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        entityManager.renderEntities(gc, game, g); // We render after the Background Image, but before the text overlays
        skyline.render(g);
        gorilla.render(g);
        gorillb.render(g);

        if(banana != null) {
            banana.render(g);
            if(banana.getColMask().getMaxY() < 0)
                g.drawImage(Assets.loadImage(Assets.Images.ARROW), banana.x - 8, 0);
        }

        drawPlayerNames(g);

        if(state != STATES.THROW) {
            g.setColor(Color.blue);
            // Description for the buttons
            g.drawString("Speed", 20, 10);
            g.drawString("Angle ", 20, 50);
        }
        if(throwNumber != null)
        {
            g.setColor(Color.white);
            g.drawString(throwNumber,this.getRootPane().getWidth()-110,20);
        }
        if(roundWinMessage != null)
        {
            g.setColor(Color.red);
            g.drawString(roundWinMessage,this.getRootPane().getWidth()/2 - 150,100);
        }
    }

    /**
     * Draws text with a dropshadow
     * @param pos center position of the text
     */
    private void drawTextWithDropShadow(Graphics g, Vector2f pos, String text, Color color) {
        // Center Text
        float x = pos.x - g.getFont().getWidth(text) / 2;

        // TODO: maybe translucent background
        // Draw Dropshadow
        g.setColor(Color.black);
        g.drawString(text, x + 1, pos.y - 1);

        // Draw Text
        g.setColor(color);
        g.drawString(text, x, pos.y);
    }

    private void drawPlayerNames(Graphics g) {

        for (int i = 0; i < Game.getInstance().getPlayers().size(); i++) {
            // Offset the Text 64 pixels higher then the gorrila
            Vector2f pos = getGorilla(i).getPosition();
            pos.y -= 64;
            Color color = getActivePlayer() == Game.getInstance().getPlayer(i) ? Color.yellow : Color.white;
            drawTextWithDropShadow(g, pos, Game.getInstance().getPlayer(i).getName(), color);
            i++;
        }
    }

    // TODO: more refactoring
    private final float MS_TO_S = 1.0f / 1000;
    private void updateThrowParameters(Input input, int delta) {
        if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_SPACE)) { throwBanana(); }

        if(keyPressDelay > 0) { keyPressDelay -= delta * MS_TO_S; }
        else if (inverseControlKeys) {
            if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_angle.setValue(if_angle.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){ if_angle.setValue(if_angle.getValue() - 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){ if_speed.setValue(if_speed.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)){ if_speed.setValue(if_speed.getValue() - 1); keyPressDelay = keyPressWaitTime; }
        }
        else {
            if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_speed.setValue(if_speed.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){ if_speed.setValue(if_speed.getValue() - 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){ if_angle.setValue(if_angle.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)){ if_angle.setValue(if_angle.getValue() - 1); keyPressDelay = keyPressWaitTime; }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        // Let the entities update their inputs first
        // Then process all remaining inputs
        entityManager.updateEntities(gc, game, delta);
        Input input = gc.getInput();

        gorilla.update(delta);
        gorillb.update(delta);

        if(admin) {
            /* DEBUG: Reroll the LevelGeneration */
            if (input.isKeyPressed(Input.KEY_Q))
                init(gc, game);
            // Win the Game
            if (input.isKeyPressed(Input.KEY_V) ) {
                activePlayer.setWin();
                activePlayer.setWin();
                activePlayer.setWin();
                state = STATES.VICTORY;
                System.out.println("V Cheat");
            }
        }
        /* Auf [ESC] muss unabhängig vom state reagiert werden */
        if(input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_P)) game.enterState(Gorillas.INGAMEPAUSE);
        if(input.isKeyPressed(Input.KEY_M)) toggleMute();

        switch (state) {
            case INPUT:
                throwNumber = "Throw Nr " + activePlayer.getThrow(); // FIXME: null pointer, if init is not called after creating new players
                toggleUI(true);
                updateThrowParameters(input, delta);
                break;
            case THROW: // TODO: Refactor Collision Detection into it's own methods
                throwNumber = "Throw Nr " + activePlayer.getThrow();
                // During the flight disable inputs
                toggleUI(false);

                banana.update(delta);

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
                if(!mute)
                    Assets.loadSound(Assets.Sounds.EXPLOSION).play();
                banana = null;

                // TODO: Claculate PlayerDamage
                // player1.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorilla));
                // player2.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorillb));

                state = STATES.INPUT;
                break;
            case ROUNDVICTORY:
                activePlayer.setWin();
                totalRoundCounter += 1;

                if(activePlayer.getWin() > 2)
                    state = STATES.VICTORY;
                else {
                    System.out.println("Herzlichen Glückwunsch " + activePlayer.getName() + "\nSie haben die Runde gewonnen !");
                    System.out.println("Win Nr" +activePlayer.getWin());

                    roundWinMessage = "Herzlichen Glückwunsch " + activePlayer.getName() + "\nSie haben die Runde gewonnen !\n" +
                                        "Sieg Nummer " + activePlayer.getWin() + ".\n"+
                                        "Sie benötigten " + activePlayer.getThrow() + " Würfe.";
                    // TODO: Save Win and Throw-Number
                    // Restart Game
                    player1.resetThrow();
                    player2.resetThrow();
                    player1.setLastAngle(120);
                    player2.setLastAngle(120);
                    player1.setLastSpeed(80);
                    player2.setLastSpeed(80);
                    if_speed.setValue(activePlayer.getLastSpeed());
                    if_angle.setValue(activePlayer.getLastAngle());
                    init(gc, game);
                }
                break;
            case VICTORY:
                // TODO: VICTORY
                System.out.println("Herzlichen Glückwunsch " + activePlayer.getName() + "\nSie haben das Spiel gewonnen !");
                System.out.println("Win Nr" +activePlayer.getWin());
                game.enterState(Gorillas.INGAMEWIN);
                game.enterState(Game.INGAMEWIN);

                // Store Win to SQL-DB
                SqlGorillas db = new SqlGorillas("data_gorillas.hsc","Gorillas");
                db.insertHighScore(activePlayer.getName(), totalRoundCounter, activePlayer.getWin(), activePlayer.getTotalThrows());

                // Reset Values
                totalRoundCounter = 0;
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

        if_angle.setMinMaxValue(0,180);
        if_angle.setValue(120);

        // Wirkungslos
        btnThrow.setAlignment(Alignment.CENTER);

        btnThrow.addCallback(new Runnable() {
            public void run() {
               throwBanana();
            }
        });

        // Add the Input-Elements to the RootPane
        rp.add(if_speed);
        rp.add(if_angle);
        rp.add(btnThrow);
        return rp;
    }

    @Override
    protected void layoutRootPane() {
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
    }

    private void toggleUI(Boolean enable) {
        btnThrow.setVisible(enable);
        if_speed.setEnabled(enable);
        if_angle.setEnabled(enable);
        if_speed.setVisible(enable);
        if_angle.setVisible(enable);
    }

    /** Generates a Banana at the current Player */
    private void throwBanana() {
        // Save new throw
        activePlayer.setThrow();

        System.out.println("Throw Banana " + if_speed.getValue() + " " + if_angle.getValue());

        activePlayer.setLastSpeed(if_speed.getValue());
        activePlayer.setLastAngle(if_angle.getValue());

        if (activePlayer == player1)
            banana = new Banana(gorilla.x, gorilla.y - gorilla.getHeight(), if_angle.getValue() - 90, if_speed.getValue(), gravity, windSpeed);
        else
            banana = new Banana(gorillb.x, gorillb.y - gorillb.getHeight(), 180 - if_angle.getValue() + 90, if_speed.getValue(), gravity, windSpeed);

        // Remove Win-Message
        roundWinMessage = null;

        state = STATES.THROW;
    }

    // TODO: Move this out of this state, it does not belong here.
    public static void toggleMute() {
        mute = !mute;
        if(Game.getInstance().getDebug()) System.out.println("Mute: " + mute);
    }

    // TODO: Move this out of this state, it does not belong here.
    public static void setInverseControlKeys(boolean x) {
        inverseControlKeys = x;
    }

    public static boolean getInverseControlKeys() {
        return inverseControlKeys;
    }

    public static void setWind(boolean x){wind = x;}
    public static boolean getWind(){return wind;}
}
