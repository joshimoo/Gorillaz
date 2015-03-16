package de.tu_darmstadt.gdi1.gorillas.main;

import java.util.List;
import java.util.ArrayList;

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
    public static final float GRAVITY_MIN = 0;
    public static final float GRAVITY_MAX = 24.79f;
    public static final float GRAVITY_DEFAULT = 9.80665f;
    public static final int MAX_NAMESIZE = 12;
    public static final float SOUND_VOLUME_MIN = 0;
    public static final float SOUND_VOLUME_MAX = 1f;
    public static final float SOUND_VOLUME_DEFAULT = 0.2f;

    // Switches
    private boolean testmode = false;
    private boolean debug = true;
    private boolean developer = true;
    private boolean inverseControlKeys = false; // Possible, candidate for an internal Option Class
    private boolean storePlayerNames = true; // Possible candidate for an internal Option Class
    private boolean mute = false;
    private boolean wind = false;

    // Scaling Factors
    private float WIND_SCALE = 0.6f;
    private float TIME_SCALE = 1 / 400f;
    private float ROTATION_DRAG = 0.02f;
    private int EXPLOSION_RADIUS = 32;

    // Gameplay Variables
    private float gravity = GRAVITY_DEFAULT;
    private float soundVolume = SOUND_VOLUME_DEFAULT;

    public float getGravity() { return gravity; }
    public float getSoundVolume() { return soundVolume; }

    public void setGravity(float value) { this.gravity = value > GRAVITY_MAX ? GRAVITY_MAX : value < GRAVITY_MIN ? GRAVITY_MIN : value; }
    public void setSoundVolume(float value) { this.soundVolume = value > SOUND_VOLUME_MAX ? SOUND_VOLUME_MAX : value < SOUND_VOLUME_MIN ? SOUND_VOLUME_MIN : value; }

    /** Singleton Pattern */
    private Game() {
        players = new ArrayList<Player>(MAX_PLAYER_COUNT);
        for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
            players.add(new Player("DUMMY"));
        }
    }
    private static Game game;
    public static Game getInstance() {
        if (game == null) { game = new Game(); }
        return game;
    }

    public boolean isMute() { return mute; }
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
    public void enableTestMode(boolean enable) { testmode = enable; }

    public boolean getStorePlayerNames() { return storePlayerNames; }
    public void setStorePlayerNames(boolean enable){ storePlayerNames = enable; }

    public boolean isDeveloper() { return developer; }
    public void setDeveloperMode(boolean enable){ developer = enable; }

    /** Time Constants */
    public float getTimeScale() { return TIME_SCALE; }
    public float getWindScale() { return WIND_SCALE; }
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
        // TODO: Cleanup
        // Cleanup all used assets
        // Save SQL DB
        // Close SQL DB
        System.exit(0);
    }
  }