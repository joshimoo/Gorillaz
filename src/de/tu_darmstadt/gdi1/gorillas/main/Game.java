package de.tu_darmstadt.gdi1.gorillas.main;

import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import de.tu_darmstadt.gdi1.gorillas.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/** Singleton Class, that contains all globals.
 * The reason for using a Singleton instead of a static class, are possible serialization
 * The reason for using this vs, Gorillas is the different GameContainers
 * (special TestGameContainer)
 */
public class Game {

    /* State Definitions */
    public static final int MAINMENUSTATE   = 0;
    public static final int GAMESETUPSTATE  = 1;
    public static final int GAMEPLAYSTATE   = 2;
    public static final int HIGHSCORESTATE  = 3;
    public static final int OPTIONSTATE     = 4;
    public static final int INGAMEPAUSE     = 5;
    public static final int HELPSTATE       = 6;
    public static final int GAMEVICTORY     = 7;

    /* Constants */
    public static       float SUN_FROM_TOP = 60;
    public static final float GRAVITY_MIN = 0;
    public static final float GRAVITY_MAX = 24.79f;
    public static final float GRAVITY_DEFAULT = 9.80665f;
    public static final int   MAX_NAMELENGTH = 12;
    public static final float SOUND_VOLUME_MIN = 0;
    public static final float SOUND_VOLUME_MAX = 1f;
    public static final float SOUND_VOLUME_DEFAULT = 0.2f;
    public static final int   ANGLE_MIN = 0;
    public static final int   ANGLE_MAX = 360;
    public static       int   ANGLE_DEFAULT = 60;
    public static final int   SPEED_MIN = 0;
    public static final int   SPEED_MAX = 200;
    public static       int   SPEED_DEFAULT = 80;

    /* Switches */
    /** Disables graphically output for test cases */
    private boolean testmode = true;
    private boolean debug = false;       // Debug outputs
    private boolean developer = true;   // Cheating options
    private boolean inverseControlKeys = false; // Possible, candidate for an internal Option Class
    private boolean storePlayerNames = true; // Possible candidate for an internal Option Class
    private boolean mute = false;
    private boolean wind = false;

    /* Scaling Factors */
    private float WIND_SCALE = 0.6f;
    private float TIME_SCALE = 1/300f;
    private float ROTATION_DRAG = 0.02f;
    private int   EXPLOSION_RADIUS = 32;
    public static float BACKGROUND_SCALE = 1f;
    public static float CANVAS_SCALE = 2f;

    /* Gameplay Variables */
    private float gravity = GRAVITY_DEFAULT;
    private float soundVolume = SOUND_VOLUME_DEFAULT;

    /* Settings */
    private final String databaseFile = "data_gorillas.hsc";

    /** @return the current gravity */
    public float getGravity() { return gravity; }

    /** @return path to the sqlite database */
    public String getDatabaseFile() { return databaseFile; }

    /** @return the current volume */
    public float getSoundVolume() { return soundVolume; }

    /** Sets the current gravity to 'value' in respect to GRAVITY_MIN and GRAVITY_MAX*/
    public void setGravity(final float value) {
        this.gravity = Utils.clamp(GRAVITY_MIN, value, GRAVITY_MAX);
    }

    /** Sets the current sound volume to 'value' in respect to GRAVITY_MIN and GRAVITY_MAX*/
    public void setSoundVolume(final float value) {
        this.soundVolume = Utils.clamp(SOUND_VOLUME_MIN, value, SOUND_VOLUME_MAX);
    }

    /** Singleton Pattern */
    public static Game getInstance() {
        if (game == null) { game = new Game(); }
        return game;
    }
    private Game() { players = new ArrayList<Player>(MAX_PLAYER_COUNT); }
    private static Game game;

    /** @return true if the sound is muted */
    public boolean isMute() { return mute; }

    /** Set mute to the given value */
    public void setMute(boolean value) { mute = value; }

    /** Toggles the mute value */
    public void toggleMute() {
        mute = !mute;
    }

    public boolean getInverseControlKeys() { return inverseControlKeys; }
    public void setInverseControlKeys(boolean enable) { inverseControlKeys = enable; }
    public void toggleInverseControlKeys() { inverseControlKeys = !inverseControlKeys; }

    /** @return true if wind is active */
    public boolean isWindActive(){ return wind; }

    /** Enable/diable the wind */
    public void setWind(boolean enable) { wind = enable; }

    /** Toggle the wind  */
    public void toggleWind() { wind = !wind; }

    /** @return true if we are in debug mode */
    public boolean isDebugMode() { return debug; }

    /** Activate/Deactivate debug mode */
    public void setDebug(boolean enable) { debug = enable; }

    /** @return true, if we are in test mode */
    public boolean isTestMode() { return testmode; }

    /** Enables/disables test mode */
    public void enableTestMode(boolean enable) {
        testmode = enable;
        if (enable) { /* These values are needed in Unit-testing */
            ANGLE_DEFAULT = 0;
            SPEED_DEFAULT = 0;
            SUN_FROM_TOP  = 5;
            setGravity(10f);
        }
    }

    /** @return true if player names are stored */
    public boolean isStorePlayerNames() { return storePlayerNames; }

    /** Enables/disables Storing of player names */
    public void setStorePlayerNames(boolean enable){ storePlayerNames = enable; }

    /** Toggles Storing of player names */
    public void toggleStorePlayerNames() { storePlayerNames = !storePlayerNames; }

    /** @return true if wa are in developer mode */
    public boolean isDeveloperMode() { return developer; }

    /** Enables/Disables developer mode */
    public void setDeveloperMode(boolean enable){ developer = enable; }

    /** @return Time Constant if we are not in test mode */
    public float getTimeScale() { return isTestMode() ?  1/100f : TIME_SCALE ;}

    /** @return Wind Constant if we are not in test mode */
    public float getWindScale() { return isTestMode() ?   1/5f : WIND_SCALE; }

    public int getExplosionRadius() {return EXPLOSION_RADIUS; }

    /** We are using a RingBuffer for Player handling
     * One of the reasons we are doing things this way,
     * is in case we do want to try todo network play
     **/
    public final int MAX_PLAYER_COUNT = 2;
    private List<Player> players = new ArrayList<>(MAX_PLAYER_COUNT);

    private int activePlayerIndex = 0;

    /** @return currently active player*/
    public Player getActivePlayer() { return players.get(activePlayerIndex); }

    /** Sets the currently active player */
    public void setActivePlayer(Player activePlayer) { this.activePlayerIndex = players.indexOf(activePlayer); }

    /** Toggles currently active player */
    public Player toggleNextPlayerActive() {
        activePlayerIndex = ++activePlayerIndex % MAX_PLAYER_COUNT;
        return players.get(activePlayerIndex);
    }

    /** @return List of all players */
    public List<Player> getPlayers() {return players; }

    /** @return player at given index */
    public Player getPlayer(int num){
        if (players != null && num < players.size()) { return players.get(num); }
        else { throw new IllegalArgumentException(String.format("There is no Player at Index: %d currently there are %d players", num, players.size())); }
    }

    /** Sets player at given index */
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

    /** Creates Player with given name */
    public void createPlayer(String name) {
        setPlayer(++lastAddedPlayer % MAX_PLAYER_COUNT, new Player(name));
    }

    /** Call exit game, to close the game. This will cleanup all loaded assets
     * as well as save all unsaved data
     */
    public void exitGame() {
        cleanupOnExit();
        System.exit(0);
    }

    /** Call this on exit to cleanup all loaded assets as well as save all unsaved data */
    public static void cleanupOnExit() {
        // Store PlayerNames to the SQL-Database
        Database.getInstance().writeToFile();
    }

}
