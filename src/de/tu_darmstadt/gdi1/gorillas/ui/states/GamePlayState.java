package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.*;
import de.tu_darmstadt.gdi1.gorillas.entities.factories.*;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.utils.SqlGorillas;
import eea.engine.entity.*;
import eea.engine.entity.Entity;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
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
    private Entity  banana;
    private CompoundDestructibleEntity skyline;
    private Entity[] gorillas;
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

    public CompoundDestructibleEntity getSkyline(){
        return skyline;
    }

    public Entity getGorilla(int num){
        if (gorillas != null && num < gorillas.length) { return gorillas[num]; }
        else { throw new IllegalArgumentException(String.format("There is no Gorrilla at Index: %d currently there are %d gorillas", num, gorillas.length)); }
    }

    public Entity getSun(){
        return sun;
    }

    public void setGravity(final float g){
        gravity = g;
    }

    public void createSkyline() {
        // TODO: Switch over to Building Entity
        skyline = BuildingFactory.createCompoundSkyline(6);
        entityManager.addEntity(getID(), skyline);
    }

    /** Make sure to have a skyline before call this */
    public void createGorrillas() {
        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        // TODO: Placeholder, Refactor
        // int buildingWidth = Gorillas.FRAME_WIDTH / 6;
        int buildingWidth = skyline.getWidth(0);
        int xx = x1 * (buildingWidth) + (buildingWidth / 2);
        int yy = x2 * (buildingWidth) + (buildingWidth / 2);

        // TODO: Refactor Player Handling
        gorillas = new Entity[]{
                PlayerFactory.createGorilla(new Vector2f(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1)), Game.getInstance().getPlayer(0)),
                PlayerFactory.createGorilla(new Vector2f(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2)), Game.getInstance().getPlayer(1))
        };

        entityManager.addEntity(getID(), getGorilla(0));
        entityManager.addEntity(getID(), getGorilla(1));
    }

    public void startGame() {
        entityManager.clearEntitiesFromState(getID());
        destroyBanana(); // In case their ist still one remaining

        // TODO: Refactor Player Handling
        createSkyline();
        createGorrillas();

        sun = SunFactory.createSun(new Vector2f(400, 60));
        entityManager.addEntity(getID(), sun);

        windSpeed = wind ? (int) ((Math.random() * 30) - 15) : 0;
        cloud = CloudFactory.createCloud(new Vector2f(0, 60), windSpeed);
        entityManager.addEntity(getID(), cloud);

        setActivePlayer(Game.getInstance().getPlayer(0));
        state = STATES.INPUT;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        // TODO: Init should really only be called once
        // for a temporary workaround you can clear all entities at the beginning of init
        // or you can use entityManager.setEntityListByState(getID(), initEntitiesList);
        // which will lead to overwritting the list, and then the old entities from last init will be gc'd
        background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);

        // Load All Static Content or Ressources (Background Images, Sounds etc)
        // Lazy Load the UI, this is better for the TestGameContainer
        // if (rp == null) {this.createRootPane()};
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        startGame();
    }

    void renderDebugShapes(GameContainer gc, StateBasedGame sb, Graphics g) {
        // Wrap this into a debug
        for (Entity e : entityManager.getEntitiesByState(getID())) {
            g.draw(e.getShape());
        }

        // Skyline
        for (Shape s : skyline.getShapes()) {
            g.draw(s);
        }
    }


    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        entityManager.renderEntities(gc, game, g); // We render after the Background Image, but before the text overlays
        renderDebugShapes(gc,game, g);


        // TODO: Check if this returns WorldSpace or LocalSpace
        if(banana != null && banana.getShape().getMaxY() < 0) {
            // TODO: Make the Arrow an Entity, that syncronises with the banana via an action,
            // then let the projectileFactory automaticly create that arrow while creating the banana
            g.drawImage(Assets.loadImage(Assets.Images.ARROW), banana.getPosition().x - 8, 0);
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
            // NOTE: Never modify the Vector that is retured from a .getPosition call
            Vector2f pos = getGorilla(i).getPosition();
            Color color = getActivePlayer() == Game.getInstance().getPlayer(i) ? Color.yellow : Color.white;
            drawTextWithDropShadow(g, new Vector2f(pos.x, pos.y - 64), Game.getInstance().getPlayer(i).getName(), color);
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

        if(admin) {
            /* DEBUG: Reroll the LevelGeneration */
            if (input.isKeyPressed(Input.KEY_Q)) { startGame(); }

            // Win the Game
            if (input.isKeyPressed(Input.KEY_V) ) {
                getActivePlayer().setWin();
                getActivePlayer().setWin();
                getActivePlayer().setWin();
                state = STATES.VICTORY;
                System.out.println("V Cheat");
            }
        }

        /* Auf [ESC] muss unabhängig vom state reagiert werden */
        if(input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_P)) game.enterState(Game.INGAMEPAUSE);
        if(input.isKeyPressed(Input.KEY_M)) toggleMute();

        switch (state) {
            case INPUT:
                throwNumber = "Throw Nr " + getActivePlayer().getThrow(); // FIXME: null pointer, if init is not called after creating new players
                toggleUI(true);
                updateThrowParameters(input, delta);
                break;
            case THROW:
                throwNumber = "Throw Nr " + getActivePlayer().getThrow();
                // During the flight disable inputs
                toggleUI(false);

                /* Banane verlässt das Spielfeld */
                // TODO: see if this returns WorldSpace or LocalSpace
                // if(banana.getShape().getMinX() > Gorillas.FRAME_WIDTH || banana.getShape().getMaxX() < 0 || banana.getShape().getMinY() > Gorillas.FRAME_HEIGHT) {
                if(banana.getPosition().x > Gorillas.FRAME_WIDTH || banana.getPosition().x < 0 || banana.getPosition().y > Gorillas.FRAME_HEIGHT) {
                    state = STATES.DAMAGE;
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(1) && getGorilla(0).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    System.out.println("Hit Player 1");
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(0) && getGorilla(1).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    System.out.println("Hit Player 2");
                }

                if (banana.collides(skyline)) { state = STATES.DAMAGE; }

                break;
            case DAMAGE:
                System.out.println("Throw " + getActivePlayer().getName() + " Nr" + getActivePlayer().getThrow());
                throwNumber = "Throw Nr " + getActivePlayer().getThrow(); // Ueberfluessig

                Game.getInstance().toogleNextPlayerActive();
                if_speed.setValue(getActivePlayer().getLastSpeed());
                if_angle.setValue(getActivePlayer().getLastAngle());

                //skyline.destroy((int)banana.getPosition().x, (int)banana.getPosition().y, 32);
                skyline.impactAt(banana.getPosition());
                if(!mute) { Assets.loadSound(Assets.Sounds.EXPLOSION).play(); }
                destroyBanana();

                // TODO: Calculate PlayerDamage
                // player1.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorilla));
                // player2.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorillb));

                state = STATES.INPUT;
                break;
            case ROUNDVICTORY:
                getActivePlayer().setWin();
                totalRoundCounter += 1;

                if(getActivePlayer().getWin() > 2)
                    state = STATES.VICTORY;
                else {
                    System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben die Runde gewonnen !");
                    System.out.println("Win Nr" + getActivePlayer().getWin());

                    roundWinMessage = "Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben die Runde gewonnen !\n" +
                                        "Sieg Nummer " + getActivePlayer().getWin() + ".\n"+
                                        "Sie benötigten " + getActivePlayer().getThrow() + " Würfe.";
                    // TODO: Save Win and Throw-Number
                    // Restart Game
                    for (Player player : Game.getInstance().getPlayers()) {
                        player.resetThrow();
                        player.setLastAngle(120);
                        player.setLastSpeed(80);
                    }

                    if_speed.setValue(getActivePlayer().getLastSpeed());
                    if_angle.setValue(getActivePlayer().getLastAngle());
                    // init(gc, game); // TODO: Why are we reiniting after round won?
                    startGame();
                }
                break;
            case VICTORY:
                // TODO: VICTORY
                System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben das Spiel gewonnen !");
                System.out.println("Win Nr" + getActivePlayer().getWin());
                game.enterState(Game.GAMEVICTORY);

                // Store Win to SQL-DB
                SqlGorillas db = new SqlGorillas("data_gorillas.hsc","Gorillas");
                db.insertHighScore(getActivePlayer().getName(), totalRoundCounter, getActivePlayer().getWin(), getActivePlayer().getTotalThrows());

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

        btnThrow.addCallback(GamePlayState.this::throwBanana);

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

    // TODO: do component based
    private void destroyBanana() {
        if (banana != null) { entityManager.removeEntity(getID(), banana);}
        banana = null;
    }

    /** Creates and assigns the Banana */
    private void createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windAcceleration) {
        // Cleanup any remaining, Bananas since at the moment we can only have a maximum of 1
        if (banana != null) {entityManager.removeEntity(getID(), banana);}
        banana = ProjectileFactory.createBanana(pos, throwAngle, throwSpeed, gravity, windAcceleration);
        entityManager.addEntity(getID(), banana);
    }

    /** Generates a Banana at the current Player */
    private void throwBanana() {
        // Save new throw
        getActivePlayer().setThrow();

        int speed = if_speed.getValue();
        int angle = if_angle.getValue();

        System.out.println("Throw Banana " + speed + " " + angle);

        getActivePlayer().setLastSpeed(speed);
        getActivePlayer().setLastAngle(angle);

        if (getActivePlayer() == Game.getInstance().getPlayer(0)) {
            Vector2f pos = getGorilla(0).getPosition();
            Vector2f size = getGorilla(0).getSize();
            createBanana(new Vector2f(pos.x, pos.y - size.y), angle - 90, speed, gravity, windSpeed);
        }
        else {
            Vector2f pos = getGorilla(1).getPosition();
            Vector2f size = getGorilla(1).getSize();
            createBanana(new Vector2f(pos.x, pos.y - size.y), 180 - angle + 90, speed, gravity, windSpeed);
        }

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
