package de.tu_darmstadt.gdi1.gorillas.main;

import de.tu_darmstadt.gdi1.gorillas.utils.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Class, that contains all globals.
 * The reason for using a Singleton instead of a static class, are possible serialization
 * The reason for using this vs, Gorrillas is the different GameContainers
 * (special TestGameContainer)
 */
public class Game {
    /** State Definitions */
    public static final int MAINMENUSTATE   = 0;
    public static final int GAMESETUPSTATE  = 1;
    public static final int GAMEPLAYSTATE   = 2;
    public static final int HIGHSCORESTATE  = 3;
    public static final int OPTIONSTATE     = 4;
    public static final int TUTORIALSTATE   = 5;
    public static final int INGAMEPAUSE     = 6;
    public static final int HELPSTATE       = 7;
    public static final int GAMEVICTORY     = 8;

    // Constants
    public static       float SUN_FROM_TOP = 60;

    public static final float GRAVITY_MIN = 0;
    public static final float GRAVITY_MAX = 24.79f;
    public static final float GRAVITY_DEFAULT = 9.80665f;
    public static final int   MAX_NAMESIZE = 12;
    public static final float SOUND_VOLUME_MIN = 0;
    public static final float SOUND_VOLUME_MAX = 1f;
    public static final float SOUND_VOLUME_DEFAULT = 0.2f;
    public static final int   ANGLE_MIN = 0;
    public static final int   ANGLE_MAX = 360;
    public static       int   ANGLE_DEFAULT = 60;
    public static final int   SPEED_MIN = 0;
    public static final int   SPEED_MAX = 200;
    public static       int   SPEED_DEFAULT = 80;

    // Switches
    private boolean testmode = true;   // Disables graphically output for test cases
    private boolean debug = false;       // Debug outputs
    private boolean developer = true;   // Cheating options
    private boolean inverseControlKeys = false; // Possible, candidate for an internal Option Class
    private boolean storePlayerNames = true; // Possible candidate for an internal Option Class
    private boolean mute = false;
    private boolean wind = false;

    // Scaling Factors
    private float WIND_SCALE = 0.6f;
    private float TIME_SCALE = 1 / 300f;
    private float ROTATION_DRAG = 0.02f;
    private int EXPLOSION_RADIUS = 32;
    public static float BACKGROUND_SCALE = 1f;
    public static float CANVAS_SCALE = 2f;

    // Gameplay Variables
    private float gravity = GRAVITY_DEFAULT;
    private float soundVolume = SOUND_VOLUME_DEFAULT;

    // Settings
    private String databaseFile = "data_gorillas.hsc";


    public float getGravity() { return gravity; }
    public String getDatabaseFile() { return databaseFile; }
    public float getSoundVolume() { return soundVolume; }

    public void setGravity(float value) { this.gravity = value > GRAVITY_MAX ? GRAVITY_MAX : value < GRAVITY_MIN ? GRAVITY_MIN : value; }
    public void setDatabaseFile(String value) { this.databaseFile = value; }
    public void setSoundVolume(float value) { this.soundVolume = value > SOUND_VOLUME_MAX ? SOUND_VOLUME_MAX : value < SOUND_VOLUME_MIN ? SOUND_VOLUME_MIN : value; }

    /** Singleton Pattern */
    private Game() { players = new ArrayList<Player>(MAX_PLAYER_COUNT); }
    private static Game game;
    public static Game getInstance() {
        if (game == null) { game = new Game(); }
        return game;
    }

    public boolean isMute() { return mute; }
    public void setMute(boolean value) { mute = value; }
    public void toggleMute() {
        mute = !mute;
        if( getDebug() ) System.out.println("Mute: " + mute);
    }

    public boolean getInverseControlKeys() { return inverseControlKeys; }
    public void setInverseControlKeys(boolean enable) { inverseControlKeys = enable; }
    public void toggleInverseControlKeys() { inverseControlKeys = !inverseControlKeys; }

    public boolean getWind(){ return wind; }
    public void setWind(boolean enable) { wind = enable; }
    public void toggleWind() { wind = !wind; }

    public boolean getDebug() { return debug; }
    public void setDebug(boolean enable) { debug = enable; }

    public boolean isTestMode() { return testmode; }
    public void enableTestMode(boolean enable) {
        testmode = enable;
        if(testmode)
        {
            // Set Test-Defaults
            if(isTestMode())
            {
                ANGLE_DEFAULT = 0;
                SPEED_DEFAULT = 0;
                gravity = 10;
                SUN_FROM_TOP = 5;
            }
        }
    }

    public boolean getStorePlayerNames() { return storePlayerNames; }
    public void setStorePlayerNames(boolean enable){ storePlayerNames = enable; }
    public void toggleStorePlayerNames() { storePlayerNames = !storePlayerNames; }

    public boolean isDeveloperMode() { return developer; }
    public void setDeveloperMode(boolean enable){ developer = enable; }

    /** Time Constants */
    public float getTimeScale() { return !isTestMode() ? TIME_SCALE : 1/100f; }
    public float getWindScale() { return !isTestMode() ? WIND_SCALE : 1/5f; }
    public float getRotationFactor() { return ROTATION_DRAG; }
    public int getExplosionRadius() {return EXPLOSION_RADIUS; }

    /** We are using a RingBuffer for Player handling
     * One of the reasons we are doing things this way,
     * is in case we do want to try todo network play
     **/
    public final int MAX_PLAYER_COUNT = 2;
    private List<Player> players = new ArrayList<>(MAX_PLAYER_COUNT);

    private int activePlayer = 0;
    public Player getActivePlayer() { return players.get(activePlayer); }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = players.indexOf(activePlayer); }
    public Player toggleNextPlayerActive() {
        activePlayer = ++activePlayer % MAX_PLAYER_COUNT;
        return players.get(activePlayer);
    }

    public List<Player> getPlayers() {return players; }
    //public  ListIterator<Player> getPlayers() { return players.listIterator(); } // Iterator is the safe way, but it's just to much boilerplate even for me !_!

    public Player getPlayer(int num){
        if (players != null && num < players.size()) { return players.get(num); }
        else { throw new IllegalArgumentException(String.format("There is no Player at Index: %d currently there are %d players", num, players.size())); }
    }

    private void setPlayer(int num, Player player) {
        if (players != null && num < MAX_PLAYER_COUNT) {
            // size is zero for no players, player one has num zero.
            if(players.size()-1 < num)
                players.add(num, player);
            else
                players.set(num, player);
        }
        else { throw new IllegalArgumentException(String.format("There is no Player at Index: %d currently there are %d players", num, players.size())); }
    }

    private int lastAddedPlayer = -1;
    public void createPlayer(String name) {
        setPlayer(++lastAddedPlayer % MAX_PLAYER_COUNT, new Player(name));
    }

    /**
     * Call exit game, to close the game
     * This will cleanup all loaded assets
     * as well as save all unsaved data
     */
    public void exitGame() {
        cleanupOnExit();
        System.exit(0);
    }

    /**
     * Call this on exit to cleanup all loaded assets
     * as well as save all unsaved data
     */
    public static void cleanupOnExit() {
        // Store PlayerNames to the SQL-Database
        Database.getInstance().writeToFile();
    }
}
