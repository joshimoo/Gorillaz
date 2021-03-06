package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.main.Map;
import de.tu_darmstadt.gdi1.gorillas.main.Player;
import de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjusterInt;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.KeyMap;
import de.tu_darmstadt.gdi1.gorillas.utils.Utils;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class GamePlayState extends BasicTWLGameState {

    // Key Handling
    private float keyPressDelay = 0;
    private final float keyPressWaitTime = 0.1f; // wait 100 ms

    // DEBUG
    private String throwNumber = null;
    private String roundWinMessage = null;
    List<Circle> debugCollisions = new ArrayList<>(32);

    // UI
    private RootPane rp;
    private AdvancedValueAdjusterInt if_speed; // We are using the advanced with the edit callback for tests
    private AdvancedValueAdjusterInt if_angle; // We are using the advanced with the edit callback for tests
    private Button btnThrow;

    // GameState
    private STATES  state;

    private int     windSpeed;
    private Image   background;
    private Image   arrow;
    private Sound   explosionSound;
    private String  comment = "";
    private String  score = "Score: 0:0";
    private float   slowmoScale;

    // Entities
    private StateBasedEntityManager entityManager;
    private Banana  banana;
    private Skyline skyline;
    private Gorilla gorilla;
    private Gorilla gorillb;
    private Sun     sun;
    private Cloud   cloud;
    private Vector2f SCREEN;

    // Counter
    private static int totalRoundCounter = 0;
    private Image buffer;
    private int count;
    private Animation aniExplode;

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

    public Sun getSun(){
        return sun;
    }

    public GamePlayState() { entityManager = StateBasedEntityManager.getInstance(); }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        // Load All Static Content or Ressources (Background Images, Sounds etc)
        // Lazy Load the UI, this is better for the TestGameContainer
        if (!Game.getInstance().isTestMode()) {
            background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);

            float scaleFactor = (float) Gorillas.CANVAS_WIDTH / background.getWidth();
            Game.CANVAS_SCALE = scaleFactor;

            if(Game.CANVAS_SCALE != 1) background = background.getScaledCopy(Game.CANVAS_SCALE);
            arrow = Assets.loadImage(Assets.Images.ARROW);
            explosionSound = Assets.loadSound(Assets.Sounds.EXPLOSION);
            buffer = new Image(Gorillas.CANVAS_WIDTH, Gorillas.CANVAS_HEIGHT);
        }
        SCREEN = new Vector2f(gc.getWidth(), gc.getHeight());
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        // Only restart game when setup again
        if(game.getLastStateID() == Game.GAMESETUPSTATE)
            startGame();
    }

    public void startGame() {
        // Create a random Map;
        loadMap(Map.createRandomMap(Gorillas.CANVAS_WIDTH, Gorillas.FRAME_HEIGHT, Map.defaultGorillaWidth, Map.defaultGorillaHeight));
    }

    private void debugGorillaHit() {
        if_angle.setValue(45);
        if_speed.setValue(95);
        throwBanana();
        banana.setSize(new Vector2f(10, 10));
        gorilla.setSize(new Vector2f(37, 42));
        gorillb.setSize(new Vector2f(37, 42));
    }

    private void createDebugFlatMap() {

        // create a map, with a flat city.
        // just one building -> flat city
        ArrayList<Vector2f> buildingCoordinates = new ArrayList<Vector2f>();
        buildingCoordinates.add(new Vector2f(0, 570));

        // Gorillas should have a width of 37 and a height of 42 in testing
        // mode. (This is the size of the given gorilla image.)
        // That is why the gorilla y coordinate in this case has to be 549.
        Vector2f leftGorillaCoordinate = new Vector2f(50, 549);
        Vector2f rightGorillaCoordinate = new Vector2f(950, 549);

        loadMap(Map.createMap(1000, 600, 0, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate));
    }

    public void loadMap(Map map) {
        // Make sure to clean up all entities in here, otherwise gc will not be able to cleanup
        // Normally, I would refactor this, but no time.
        entityManager.clearEntitiesFromState(getID());
        debugCollisions.clear();

        int mapWidth = map.getMapFrameWidth();
        int mapHeight = map.getMapFrameHeight();

        skyline = new Skyline(map.getBuildings(), mapWidth, mapHeight);

        // We could translate back into feet position, since we could be using a different sized Gorilla.
        // So the Gorilla Class, can calculate the center position based on it's size.
        float x = map.getLeftGorillaCoordinate().x;
        float y = map.getLeftGorillaCoordinate().y;
        gorilla = new Gorilla(new Vector2f(x, y));
        gorilla.setVisible(true);

        x = map.getRightGorillaCoordinate().x;
        y = map.getRightGorillaCoordinate().y;
        gorillb = new Gorilla(new Vector2f(x, y));
        gorillb.setVisible(true);

        sun = new Sun(new Vector2f(Gorillas.CANVAS_WIDTH / 2, Game.SUN_FROM_TOP));

        windSpeed = Game.getInstance().isWindActive() ? calculateWind() : 0;
        cloud = new Cloud(new Vector2f(0, 60), windSpeed);

        // Clear the previous state, particular for debug loading
        destroyBanana();
        state = STATES.INPUT;

        // Reset the ugly input stuff for tests
        validAngle = false;
        validVelocity = false;
    }

    void renderDebugShapes(GameContainer gc, StateBasedGame game, Graphics g) {
        Color old = g.getColor();
        g.setColor(Color.yellow);
        g.draw(sun.getShape());
        g.draw(skyline.getShape());
        g.draw(gorilla.getShape());
        g.draw(gorillb.getShape());
        g.draw(cloud.getShape());
        if (banana != null) g.draw(banana.getShape());

        for (Skyscraper s : skyline.skyscrapers) {
            g.draw(s.getShape());
            Vector2f textPos = new Vector2f(s.getPosition().x, s.getPosition().y - s.getSize().y / 2);
            drawTextWithDropShadow(g, textPos, String.format("(%d , %d)", (int)textPos.x, (int)textPos.y), Color.pink);
        }

        // Draw historical collisions
        debugCollisions.forEach(g::draw);

        // Reset Color back to the old when done
        g.setColor(old);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics gr) throws SlickException {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
        Graphics g = buffer.getGraphics();
        g.clear();
        g.drawImage(background, 0, 0);

        drawTextWithDropShadow(g, sun.getPosition().copy().add(new Vector2f(0, sun.getSize().y / 2)), comment, Color.yellow);
        drawTextWithDropShadow(g, sun.getPosition().copy().sub(new Vector2f(0, sun.getSize().y)), score, Color.yellow);
        entityManager.renderEntities(gc, game, g);
        skyline.render(gc, game, g);
        gorilla.render(gc, game, g);
        gorillb.render(gc, game, g);
        cloud.render(gc, game, g);
        sun.render(gc, game, g);
        drawPlayerNames(g);

        if (Game.getInstance().isDebugMode()) { renderDebugShapes(gc, game, g); }

        if(banana != null) {
            banana.render(gc, game, g);
            if(banana.getShape().getMaxY() < 0) {
                g.drawImage(arrow, banana.getPosition().x - 8, 0);
            }
            if(aniExplode != null)
                g.drawAnimation(aniExplode, banana.getPosition().x - 32, banana.getPosition().y - 32);
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

        g.resetTransform();
        gr.resetTransform();

        Vector2f target;

        if (banana != null) {
            float zoom = 1f / (float) Math.sqrt(slowmoScale);

            target = getOffsetToCenter(SCREEN, banana.getPosition(), zoom);
            gr.drawImage(buffer.getScaledCopy(zoom), -target.x, -target.y);
        }
        else {
            Gorilla gor = (Game.getInstance().getActivePlayer() == Game.getInstance().getPlayer(0)) ? gorilla : gorillb;
            target = getOffsetToCenter(SCREEN, gor.getPosition());
            gr.drawImage(buffer, -target.x, -target.y);
        }

        if(state == STATES.INPUT) {
            gr.setColor(Color.blue);
            // Description for the buttons
            gr.drawString("Speed", 20, 10);
            gr.drawString("Angle ", 20, 50);
        }
    }

    private Vector2f getOffsetToCenter(Vector2f screen, Vector2f center){
        return getOffsetToCenter(screen, center, 1f);
    }

    private Vector2f getOffsetToCenter(Vector2f screen, Vector2f center, float scale){
        float a = screen.x / scale;
        float b = screen.y / scale;
        float x = Utils.clamp(0, center.x - (a / 2), 1024 - a);
        float y = Utils.clamp(0, center.y - (b / 2), screen.y - b);
        return new Vector2f(x * scale, y * scale);
    }

    /**
     * Draws text with a dropshadow
     * @param pos center position of the text
     */
    private void drawTextWithDropShadow(Graphics g, Vector2f pos, String text, Color color) {
        // Reset Color back to the old when done
        Color old = g.getColor();

        // Center Text
        float x = pos.x - g.getFont().getWidth(text) / 2;

        // TODO: maybe translucent background
        // Draw Dropshadow
        g.setColor(Color.black);
        g.drawString(text, x + 1, pos.y - 1);

        // Draw Text
        g.setColor(color);
        g.drawString(text, x, pos.y);
        g.setColor(old);
    }

    /** @return the distance off the banana to the given gorilla */
    private float getDistanceToBanana(final Gorilla g){
        if(banana == null)
            throw new RuntimeException("Keine banane");
        return banana.getPosition().distance(g.getPosition());
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

    private final float MS_TO_S = 1.0f / 1000;
    private void updateThrowParameters(Input input, int delta) {
        if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_SPACE)) { throwBanana(); }

        if(keyPressDelay > 0) { keyPressDelay -= delta * MS_TO_S; }
        else if (Game.getInstance().getInverseControlKeys()) {
            if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_angle.setValue(if_angle.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_LEFT)  || input.isKeyDown(Input.KEY_A)){ if_angle.setValue(if_angle.getValue() - 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_UP)    || input.isKeyDown(Input.KEY_W)){ if_speed.setValue(if_speed.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_DOWN)  || input.isKeyDown(Input.KEY_S)){ if_speed.setValue(if_speed.getValue() - 1); keyPressDelay = keyPressWaitTime; }
        }
        else {
            if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){ if_speed.setValue(if_speed.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_LEFT)  || input.isKeyDown(Input.KEY_A)){ if_speed.setValue(if_speed.getValue() - 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_UP)    || input.isKeyDown(Input.KEY_W)){ if_angle.setValue(if_angle.getValue() + 1); keyPressDelay = keyPressWaitTime; }
            if (input.isKeyDown(Input.KEY_DOWN)  || input.isKeyDown(Input.KEY_S)){ if_angle.setValue(if_angle.getValue() - 1); keyPressDelay = keyPressWaitTime; }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input input = gc.getInput();
        KeyMap.globalKeyPressedActions(input, game);

        if(Game.getInstance().isDeveloperMode()) {
            // Reroll the LevelGeneration
            if (input.isKeyPressed(Input.KEY_Q)) { startGame(); }
            if (input.isKeyPressed(Input.KEY_1)) { createDebugFlatMap(); }
            if (input.isKeyPressed(Input.KEY_2)) { debugGorillaHit(); }

            // Win the Game
            if (input.isKeyPressed(Input.KEY_V) ) {
                totalRoundCounter = 3;
                int throwfake = (int) (Math.random()*20);
                for (int i = 0; i < throwfake; i++) {
                    getActivePlayer().setThrow();
                }
                getActivePlayer().setWin();
                getActivePlayer().setWin();
                getActivePlayer().setWin();
                state = STATES.VICTORY;
            }
        }

        /* ActionCam slowmo :D */
        if (!Game.getInstance().isTestMode()) { delta = (int) Math.max(1, delta * slowmoScale * slowmoScale); }

        // Let the entities update their inputs first
        // Then process all remaining inputs
       // entityManager.updateEntities(gc, game, delta);
        gorilla.update(gc, game, delta);
        gorillb.update(gc, game, delta);
        cloud.update(gc, game, delta);

        switch (state) {
            case INPUT:
                slowmoScale = 1.0f;
                throwNumber = "Throw Nr " + getActivePlayer().getThrow();
                toggleUI(true);
                updateThrowParameters(input, delta);
                break;
            case THROW:
                flightTime += delta;
                throwNumber = "Throw Nr " + getActivePlayer().getThrow();
                // During the flight disable inputs
                toggleUI(false);
                comment = "";

                banana.update(gc, game, delta);
                sun.collides(banana);

                // Bounds Check
                if(outsidePlayingField(banana, gc.getWidth(), gc.getHeight())) {
                    state = STATES.DAMAGE;
                    if(Game.getInstance().isDebugMode()) System.out.printf("OutOfBounds: pos(%.0f, %.0f), world(%d, %d)",  banana.getPosition().x, banana.getPosition().y, gc.getWidth(), gc.getHeight() );
                    comment = "...";
                    Game.getInstance().toggleNextPlayerActive();
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(1) && getGorilla(0).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    if(Game.getInstance().isDebugMode()) System.out.println("Hit Player 1");
                    comment = "Treffer!";
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(0) && getGorilla(1).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    if(Game.getInstance().isDebugMode()) System.out.println("Hit Player 2");
                    comment = "Treffer!";
                }

                if(skyline.collides(banana)) {
                    state = STATES.DAMAGE;
                    debugCollisions.add(new Circle(banana.getPosition().x, banana.getPosition().y, Game.getInstance().getExplosionRadius()));
                    getThrowComments();
                    Game.getInstance().toggleNextPlayerActive();
                }

                // ACTIONSLOWMO
                Gorilla inactiv = (getActivePlayer() == Game.getInstance().getPlayer(0) ? gorillb:gorilla);
                float dist = Math.min(getDistanceToBanana(inactiv), 90 * 1.5f);
                slowmoScale = (float) Math.sin(Math.toRadians(dist / 1.5f));

                break;
            case DAMAGE:

                // Just Destroy the Skyline
                // and instanciate an explosion that is independent of game state
                // In Testmode, we return true, immediatly
                if(explodeAt(delta)) {
                    if_speed.setValue(getActivePlayer().getLastSpeed());
                    if_angle.setValue(getActivePlayer().getLastAngle());
                    skyline.destroy((int) banana.getPosition().x, (int) banana.getPosition().y, Game.getInstance().getExplosionRadius());
                    destroyBanana();
                    state = STATES.INPUT;
                }

                break;
            case ROUNDVICTORY:
                // In Testmode, we return true, immediatly
                if(explodeAt(delta)){
                getSun().resetAstonished(); // For tests, reset smiling on round end
                getActivePlayer().setWin();
                totalRoundCounter += 1;

                if(getActivePlayer().getWin() > 2){
                    state = STATES.VICTORY;
                    count = 0;
                }
                else {
                    if(Game.getInstance().isDebugMode()) System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben die Runde gewonnen !");
                    if(Game.getInstance().isDebugMode()) System.out.println("Win Nr" + getActivePlayer().getWin());

                    roundWinMessage = "Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben die Runde gewonnen !\n" +
                                        "Sieg Nummer " + getActivePlayer().getWin() + ".\n"+
                                        "Sie benötigten " + getActivePlayer().getThrow() + " Würfe.";
                    // TODO: Save Win and Throw-Number
                    // Restart Game
                    for (Player player : Game.getInstance().getPlayers()) {
                        player.resetThrow();
                        player.setLastAngle(Game.ANGLE_DEFAULT);
                        player.setLastSpeed(Game.SPEED_DEFAULT);
                    }
                        score = "Score: " + Game.getInstance().getPlayer(0).getWin() + ":" +
                                Game.getInstance().getPlayer(1).getWin();

                    if_speed.setValue(getActivePlayer().getLastSpeed());
                    if_angle.setValue(getActivePlayer().getLastAngle());

                    // Cycle activeplayer
                    Game.getInstance().toggleNextPlayerActive();
                    startGame();}
                }
                break;
            case VICTORY:
                destroyBanana();
                    if (Game.getInstance().isDebugMode()) System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben das Spiel gewonnen !");
                    if (Game.getInstance().isDebugMode()) System.out.println("Win Nr" + getActivePlayer().getWin());

                    for (Player p : Game.getInstance().getPlayers()) {
                        Database.getInstance().setHighScore(p.getName(), totalRoundCounter, p.getWin(), p.getTotalThrows());
                    }

                    // Reset Values
                    totalRoundCounter = 0;
                     Player winningPlayer = getActivePlayer();
                     if(winningPlayer == Game.getInstance().getPlayer(0)) gorillb.setGrave();
                     else gorilla.setGrave();
                     Game.getInstance().toggleNextPlayerActive();

                    game.enterState(Game.GAMEVICTORY);
                break;
        }
    }

    /** Plays the passed sound, unless the audio is muted */
    private void playSound(Sound sound) {
        if (!Game.getInstance().isMute() && sound != null) { sound.play(1f, Game.getInstance().getSoundVolume()); }
    }

    /**
     * Checks if the entity has left the playing field
     * Only checks Left/Right/Bottom
     */
    Boolean outsidePlayingField(Entity entity, int screenWidth, int screenHeight) {
        return entity.getPosition().x > 1024 || entity.getPosition().x < 0 || entity.getPosition().y > screenHeight;
    }

    @Override
    protected RootPane createRootPane() {
        // Needed for adding the new Input-Elements
        rp = super.createRootPane();

        if_speed= new AdvancedValueAdjusterInt();
        if_angle = new AdvancedValueAdjusterInt();
        btnThrow = new Button("Throw");

        if_speed.setMinMaxValue(Game.SPEED_MIN, Game.SPEED_MAX);
        if_speed.setValue(Game.SPEED_DEFAULT);
        // validVelocity = true;
        // NOTE: Don't set these here, since they are an ugly hack for testcases.

        if_angle.setMinMaxValue(Game.ANGLE_MIN, Game.ANGLE_MAX);
        if_angle.setValue(Game.ANGLE_DEFAULT);
        // validAngle = true;
        // NOTE: Don't set these here, since they are an ugly hack for testcases.
        // We do backend verification in our model instead of frontend verification.
        // Therefore we can only have valid inputs.

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
        btnThrow.setSize(60, 25);
        btnThrow.setPosition(basic_x + 60 + 20, basic_y + basic_x_c * pos);
    }

    private void toggleUI(Boolean enable) {
        btnThrow.setVisible(enable);
        if_speed.setEnabled(enable);
        if_angle.setEnabled(enable);
        if_speed.setVisible(enable);
        if_angle.setVisible(enable);
    }

    private void destroyBanana() {
        if (Game.getInstance().isDebugMode()) System.err.println("Flight Time: " + flightTime);
        if (banana != null) { entityManager.removeEntity(getID(), banana); }
        banana = null;
    }

    /** Creates and assigns the Banana */
    private void createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windAcceleration) {
        // Cleanup any remaining, Bananas since at the moment we can only have a maximum of 1
        if (banana != null) {entityManager.removeEntity(getID(), banana);}
        banana = new Banana(pos, (int)throwAngle, (int)throwSpeed, gravity, (int)windAcceleration);
        entityManager.addEntity(getID(), banana);
    }

    int flightTime = 0;
    /** Generates a Banana at the current Player */
    public void throwBanana() {
        // Save new throw
        getActivePlayer().setThrow();

        int speed = if_speed.getValue();
        int angle = if_angle.getValue();

        if(Game.getInstance().isDebugMode()) System.out.println("Throw Banana " + speed + " " + angle);

        getActivePlayer().setLastSpeed(speed);
        getActivePlayer().setLastAngle(angle);

        if (getActivePlayer() == Game.getInstance().getPlayer(0)) {
            Vector2f pos = getGorilla(0).getPosition();
            Vector2f size = getGorilla(0).getSize();
            createBanana(new Vector2f(pos.x - 30, pos.y - 38), angle, speed, Game.getInstance().getGravity(), windSpeed);
        } else {
            Vector2f pos = getGorilla(1).getPosition();
            Vector2f size = getGorilla(1).getSize();
            createBanana(new Vector2f(pos.x + 30, pos.y  - 38), 180 - angle, speed, Game.getInstance().getGravity(), windSpeed);
        }

        // Remove Win-Message
        roundWinMessage = null;
        state = STATES.THROW;
        flightTime = 0;
    }

    /** TESTS */
    // HACK: This is dirty !_!
    // We do backend verification in our model instead of frontend verification.
    // Therefore we can only have valid inputs, but tests require a -1 value for not set inputs.
    // while we set sensible defaults for our game, so this is the result of that testcase >/<
    private boolean validVelocity = false;
    private boolean validAngle = false;
    public void resetPlayerWidget() {
        if_speed.setValue(0);
        if_angle.setValue(0);
        validVelocity = false;
        validAngle = false;
    }

    public int getVelocity() { return validVelocity ? if_speed.getValue() : -1; }

    public void fillVelocityInput(char c) {
        if (verifyInput(if_speed.getValue(), if_speed.getMinValue(), if_speed.getMaxValue(), c)) {
            if_speed.setValue(if_speed.getValue() * 10 + Character.getNumericValue(c));
            validVelocity = true;
        }
    }

    public int getAngle() { return validAngle ? if_angle.getValue() : -1; }

    public void fillAngleInput(char c) {
        if (verifyInput(if_angle.getValue(), if_angle.getMinValue(), if_angle.getMaxValue(), c)) {
            if_angle.setValue(if_angle.getValue() * 10 + Character.getNumericValue(c));
            validAngle = true;
        }
    }

    /** This only works for positive numbers */
    public boolean verifyInput(int oldValue, int min, int max, char c) {
        if (Character.isDigit(c)) {
            int newValue = oldValue * 10 + Character.getNumericValue(c);
            if (newValue <= max && newValue >= min) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates wind speed
     * @return wind speed not 0
     */
    public int calculateWind()
    {
        /* wind speed 2 to 17 */
        int wind = (int) ((Math.random() * 15) + 2);

        /* wind direction */
        wind = Math.random() > 0.5f ? -wind : wind;

        if (Game.getInstance().isDebugMode()) { System.out.println("Wind-Speed : " + wind); }
        return wind;
    }


    private boolean explodeAt(int delta){
        if(Game.getInstance().isTestMode()) return true;
        if(aniExplode == null) {
            aniExplode = new Animation();
            aniExplode.setLooping(false);
            aniExplode.addFrame(Assets.loadImage(Assets.Images.EXPLOSIONS_SHEET).getSubImage(0, 0 * 64, 64, 64), 200);
            aniExplode.addFrame(Assets.loadImage(Assets.Images.EXPLOSIONS_SHEET).getSubImage(0, 1 * 64, 64, 64), 200);
            aniExplode.addFrame(Assets.loadImage(Assets.Images.EXPLOSIONS_SHEET).getSubImage(0, 3 * 64, 64, 64), 200);
            aniExplode.addFrame(Assets.loadImage(Assets.Images.EXPLOSIONS_SHEET).getSubImage(0, 4 * 64, 64, 64), 200);
            aniExplode.addFrame(Assets.loadImage(Assets.Images.EXPLOSIONS_SHEET).getSubImage(0, 5 * 64, 64, 64), 200);
            aniExplode.start();
            playSound(explosionSound);
            if(banana != null)  banana.setVisible(false);

        }
        if(aniExplode.isStopped()) {
            aniExplode = null;
            return true;
        } else {
            aniExplode.update(delta);
            return false;
        }
    }

    /**
     * comments on the hits
     */
    public void getThrowComments()
    {
        int invert = -1;
        Gorilla gorilla = this.gorilla;
        if(getActivePlayer() == Game.getInstance().getPlayer(1)) {
            invert = 1;
            gorilla = this.gorillb;
        }

        int distance = invert * 180;
        if (banana.getPosition().getX() < gorilla.getPosition().getX() + distance)      comment = "Knapp daneben ist leider auch vorbei!";
        else if(banana.getPosition().getX() > gorilla.getPosition().getX() + distance * 2)  comment = "Da wirft meine Oma ja weiter!";
        else if(banana.getPosition().getX() < gorilla.getPosition().getX() - distance * 2)  comment = "Da hinten steht doch niemand!";
        else if(banana.getPosition().getY() < gorilla.getPosition().getY() - distance * 2)  comment += " Hochmut kommt vor dem Fall!";

        if(banana.getPosition().getY() > gorilla.getPosition().getY() + distance * 2)       comment += " Mehr Höhe!";
        else if(banana.getPosition().getY() > gorilla.getPosition().getY() - distance * 2)       comment += " Zu Hoch!";
        // Default
        if(comment.isEmpty()) comment = "Beim nächsten mal vielleicht!";

        System.out.println("G=" + gorilla.getPosition().getX() +"|"+ gorilla.getPosition().getY() + " B= " + banana.getPosition().getX() +"|"+ banana.getPosition().getY() +" Dist=" + (gorilla.getPosition().getX() - banana.getPosition().getX()) + "|" + (gorilla.getPosition().getY() - banana.getPosition().getY()) + "|" +distance);
    }
}
