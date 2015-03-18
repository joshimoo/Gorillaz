package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.*;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Player;
import de.tu_darmstadt.gdi1.gorillas.ui.widgets.valueadjuster.AdvancedValueAdjusterInt;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.KeyMap;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class GamePlayState extends BasicTWLGameState {

    // Key Handling
    private float keyPressDelay = 0;
    private final float keyPressWaitTime = 0.1f; // wait 100 ms TODO: experiment with these

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
    private float   gravity = 9.80665f;
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
        if (!Game.getInstance().isTestMode()) { // Don't load anything in TestMode
            background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);
            float scaleFactor = (float) Gorillas.CANVAS_WIDTH / background.getWidth();
            Game.CANVAS_SCALE = scaleFactor;
            System.out.println(Game.CANVAS_SCALE);
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
        entityManager.clearEntitiesFromState(getID());
        debugCollisions.clear();
        skyline = new Skyline(8);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 5);

        int xx = x1 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);
        int yy = x2 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);

        gorilla = new Gorilla(new Vector2f(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1)));
        gorillb = new Gorilla(new Vector2f(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2)));

        sun = new Sun(new Vector2f(512, 60));

        windSpeed = Game.getInstance().getWind() ? calculateWind(0) : 0;
        cloud = new Cloud(new Vector2f(0, 60), windSpeed);

        destroyBanana();

        setActivePlayer(Game.getInstance().getPlayer(0));
        state = STATES.INPUT;
    }

    void renderDebugShapes(GameContainer gc, StateBasedGame game, Graphics g) {
        // TODO: instead of explicitly drawing individual entities, draw all statemanager registered entity
        //for (Entity e : entityManager.getEntitiesByState(getID())) {g.draw(e.getShape());}
        g.draw(sun.getShape());
        g.draw(skyline.getShape());
        g.draw(gorilla.getShape());
        g.draw(gorillb.getShape());
        g.draw(cloud.getShape());
        if (banana != null) g.draw(banana.getShape());

        // Draw historical collisions
        debugCollisions.forEach(g::draw);
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
        if (Game.getInstance().getDebug()) { renderDebugShapes(gc, game, g); }

        if(banana != null) {
            banana.render(gc, game, g);
            if(banana.getShape().getMaxY() < 0) {
                g.drawImage(arrow, banana.getPosition().x - 8, 0);
            }
        }

        drawPlayerNames(g);

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
        switch (state){
            case THROW:  // OSB
                if(banana != null) {
                    float zoom = 1f / (float) Math.sqrt(slowmoScale);

                    target = getOffsetToCenter(SCREEN, banana.getPosition(), zoom);
                    gr.drawImage(buffer.getScaledCopy(zoom), -target.x, -target.y);
                }
                break;
            default:
                Gorilla gor = (Game.getInstance().getActivePlayer() == Game.getInstance().getPlayer(0)) ? gorilla:gorillb;
                target = getOffsetToCenter(SCREEN, gor.getPosition());
                gr.drawImage(buffer, -target.x, -target.y);
                break;
        }

        if(state != STATES.THROW) {
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
        float x = clamp(0, center.x - (a / 2), 1024 - a);
        float y = clamp(0, center.y - (b / 2),screen.y - b);
        return new Vector2f(x * scale, y * scale);
    }

    private float clamp (float a, float x, float b){
        return Math.max(a, Math.min(x, b));
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

    // TODO: more refactoring
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
        delta *= slowmoScale * slowmoScale;

        // Let the entities update their inputs first
        // Then process all remaining inputs
        entityManager.updateEntities(gc, game, delta);
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
                throwNumber = "Throw Nr " + getActivePlayer().getThrow();
                // During the flight disable inputs
                toggleUI(false);
                comment = "";

                banana.update(gc, game, delta);
                sun.isCollidding(banana);

                // Bounds Check
                if(outsidePlayingField(banana, gc.getWidth(), gc.getHeight())) {
                    state = STATES.DAMAGE;
                    if(Game.getInstance().getDebug())
                        System.out.printf("OutOfBounds: pos(%.0f, %.0f), world(%d, %d)",  banana.getPosition().x, banana.getPosition().y, gc.getWidth(), gc.getHeight() );
                    comment = "...";
                    Game.getInstance().toggleNextPlayerActive();
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(1) && getGorilla(0).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    if(Game.getInstance().getDebug()) System.out.println("Hit Player 1");
                    comment = "Treffer!";
                }

                if(getActivePlayer() == Game.getInstance().getPlayer(0) && getGorilla(1).collides(banana)) {
                    state = STATES.ROUNDVICTORY;
                    if(Game.getInstance().getDebug()) System.out.println("Hit Player 2");
                    comment = "Treffer!";
                }

                if(skyline.isCollidding(banana)) {
                    state = STATES.DAMAGE;
                    debugCollisions.add(new Circle(banana.getPosition().x, banana.getPosition().y, Game.getInstance().getExplosionRadius()));
                   if(getActivePlayer() == Game.getInstance().getPlayer(1)){
                       if(banana.getPosition().getX() > gorilla.getPosition().getX() + 64)
                           comment = "Viel zu kurz!";
                       else if(banana.getPosition().getX() < gorilla.getPosition().getX() - 64)
                           comment = "Viel zu weit!";
                       if(banana.getPosition().getY() > gorilla.getPosition().getY() + 64)
                           comment += " Viel zu tief!";
                       else if(banana.getPosition().getY() < gorilla.getPosition().getY() - 64)
                           comment += " Viel zu hoch!";

                       if(comment == "") comment = "Fast getroffen!";
                   }
                    else{
                       if(banana.getPosition().getX() > gorillb.getPosition().getX() + 64)
                           comment = "Viel zu weit!";
                       else if(banana.getPosition().getX() < gorillb.getPosition().getX() - 64)
                           comment = "Viel zu kurz!";
                       if(banana.getPosition().getY() > gorillb.getPosition().getY() + 64)
                           comment += " Viel zu tief!";
                       else if(banana.getPosition().getY() < gorillb.getPosition().getY() - 64)
                           comment += " Viel zu hoch!";

                       if(comment == "") comment = "Fast getroffen!";
                   }
                    Game.getInstance().toggleNextPlayerActive();
                }

                // ACTIONSLOWMO
                Gorilla inactiv = (getActivePlayer() == Game.getInstance().getPlayer(0) ? gorillb:gorilla);
                float dist = Math.min(getDistanceToBanana(inactiv), 90 * 1.5f);
                slowmoScale = (float) Math.sin(Math.toRadians(dist / 1.5f));

                break;
            case DAMAGE:
                if(Game.getInstance().getDebug()) System.out.println("Throw " + getActivePlayer().getName() + " Nr" + getActivePlayer().getThrow());
                throwNumber = "Throw Nr " + getActivePlayer().getThrow(); // Ueberfluessig

                if_speed.setValue(getActivePlayer().getLastSpeed());
                if_angle.setValue(getActivePlayer().getLastAngle());

                skyline.destroy((int)banana.getPosition().x, (int)banana.getPosition().y, Game.getInstance().getExplosionRadius());
                playSound(explosionSound);
                destroyBanana();

                // TODO: Claculate PlayerDamage
                // player1.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorilla));
                // player2.damage(calcPlayerDamage(banana.getCenterX(), banana.getCenterY(), gorillb));

                state = STATES.INPUT;
                break;
            case ROUNDVICTORY:
                getSun().resetAstonished(); // For tests, reset smiling on round end
                getActivePlayer().setWin();
                totalRoundCounter += 1;

                if(getActivePlayer().getWin() > 2)
                    state = STATES.VICTORY;
                else {
                    if(Game.getInstance().getDebug()) System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben die Runde gewonnen !");
                    if(Game.getInstance().getDebug()) System.out.println("Win Nr" + getActivePlayer().getWin());

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
                    startGame();
                }
                break;
            case VICTORY:
                // TODO: VICTORY
                if(Game.getInstance().getDebug()) System.out.println("Herzlichen Glückwunsch " + getActivePlayer().getName() + "\nSie haben das Spiel gewonnen !");
                if(Game.getInstance().getDebug()) System.out.println("Win Nr" + getActivePlayer().getWin());
                game.enterState(Game.GAMEVICTORY);

                for(Player p : Game.getInstance().getPlayers())
                {
                    Database.getInstance().setHighScore(p.getName(), totalRoundCounter, p.getWin(), p.getTotalThrows());
                }

                // Reset Values
                totalRoundCounter = 0;
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
        validVelocity = true;

        if_angle.setMinMaxValue(Game.ANGLE_MIN, Game.ANGLE_MAX);
        if_angle.setValue(Game.ANGLE_DEFAULT);
        validAngle = true;

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
        btnThrow.setPosition(basic_x+60+20, basic_y+basic_x_c*pos);
    }

    private void toggleUI(Boolean enable) {
        btnThrow.setVisible(enable);
        if_speed.setEnabled(enable);
        if_angle.setEnabled(enable);
        if_speed.setVisible(enable);
        if_angle.setVisible(enable);
    }

    private void destroyBanana() {
        if (banana != null) {
            entityManager.removeEntity(getID(), banana);
        }
        banana = null;
    }

    /** Creates and assigns the Banana */
    private void createBanana(Vector2f pos, float throwAngle, float throwSpeed, float gravity, float windAcceleration) {
        // Cleanup any remaining, Bananas since at the moment we can only have a maximum of 1
        if (banana != null) {entityManager.removeEntity(getID(), banana);}
        banana = new Banana(pos, (int)throwAngle, (int)throwSpeed, gravity, (int)windAcceleration);
        entityManager.addEntity(getID(), banana);
    }

    /** Generates a Banana at the current Player */
    public void throwBanana() {
        // Save new throw
        getActivePlayer().setThrow();

        int speed = if_speed.getValue();
        int angle = if_angle.getValue();

        if(Game.getInstance().getDebug()) System.out.println("Throw Banana " + speed + " " + angle);

        getActivePlayer().setLastSpeed(speed);
        getActivePlayer().setLastAngle(angle);

        if (getActivePlayer() == Game.getInstance().getPlayer(0)) {
            Vector2f pos = getGorilla(0).getPosition();
            Vector2f size = getGorilla(0).getSize();
            createBanana(new Vector2f(pos.x, pos.y - size.y), angle, speed, Game.getInstance().getGravity(), windSpeed);
        } else {
            Vector2f pos = getGorilla(1).getPosition();
            Vector2f size = getGorilla(1).getSize();
            createBanana(new Vector2f(pos.x, pos.y - size.y), 180 - angle, speed, Game.getInstance().getGravity(), windSpeed);
        }

        // Remove Win-Message
        roundWinMessage = null;
        state = STATES.THROW;
    }

    // TESTS
    // HACK: This is dirty !_!
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
        if (Character.isDigit(c)){
            validVelocity = true;
            if(verifyInput(if_speed.getValue(), if_speed.getMinValue(), if_speed.getMaxValue(), c)) {
                if_speed.setValue(if_speed.getValue() * 10 + Character.getNumericValue(c));
            }
        }
        else validVelocity = false;
    }

    public int getAngle() { return validAngle ? if_angle.getValue() : -1; }
    public void fillAngleInput(char c) {
        if (Character.isDigit(c)) {
            validAngle = true;
            if(verifyInput(if_angle.getValue(), if_angle.getMinValue(), if_angle.getMaxValue(), c)) {
                if_angle.setValue(if_angle.getValue() * 10 + Character.getNumericValue(c));
            }
        }
        else validAngle = false;
    }

    /** This only works for positive numbers */
    public boolean verifyInput(int oldValue, int min, int max, char c) {
        int newValue = oldValue * 10 + Character.getNumericValue(c);
        if (newValue <= max && newValue >= min) {
            return true;
        }

        return false;
    }

    /**
     * Generates wind speed if "set" is "0"
     * @param set allows you to set your own wind speed
     * @return wind speed not 0
     */
    public int calculateWind(int set)
    {
        int wind = set;
        while(wind < 2)
            wind = (int) ((Math.random() * 30) - 15);
        if (Game.getInstance().getDebug()) { System.out.println("Wind-Speed : " + wind); }
        return wind;

    }
}
