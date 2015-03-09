package de.tu_darmstadt.gdi1.gorillas.main;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

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

    // TODO: consider whether we need, an extra scale if we are doing everything in m/s
    // If we design everything on m/s, we can then define a scale of 1 m == X pixels
    // Or we have custom scales, for our individual components of 1m == x pixels for WIND
    //public static final float MS_TO_S = 1.0f / 1000; // TODO: this would be best, but let's see if we can do so with our tests
    public static final float MS_TO_S = 1;

    // TODO: Refactor, rethink where todo the scaling and unit conversion
    // TODO: Experiment, with this value
    // Original values where FPS dependent
    // (0.6 / 2) * w * fps --> // 0.3 * w * fps
    // where w[-15, 15), fps = 120
    // 0.3 * 7.5 * 120 --> // 2.5 * 120 = 300 pixel pro sekunde
    // maybe 1m = 20 pixels, which would lead to 150 pixels/s at 7.5 m/s
    private static final float WIND_SCALE = 0.6f;
    private static final float TIME_SCALE = 1 / 400f;
    private static final float ROTATION_DRAG = 0.02f;

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


    /** Runtime Debug Setting */
    private boolean debug = false;
    public Boolean getDebug() { return debug; }
    public void setDebug(boolean debuging) { debug = debuging; }

    /** Time Constants */
    public static float getTimeScale() { return TIME_SCALE; }
    public static float getWindScale() { return WIND_SCALE; }
    public static float getRotationFactor() { return ROTATION_DRAG; }

    /** We are using a RingBuffer for Player handling
     * One of the reasons we are doing things this way,
     * is in case we do want to try todo network play
     **/
    public final int MAX_PLAYER_COUNT = 2;
    private List<Player> players = new ArrayList<>(MAX_PLAYER_COUNT);

    private int activePlayer = 0;
    public Player getActivePlayer() { return players.get(activePlayer); }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = players.indexOf(activePlayer); }
    public Player toogleNextPlayerActive() {
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

    // TODO: if you get tired of typing: Game.getInstance().instanceMethodName()
    // Use the Refactoring Plugin if possible,
    // Create a static Wrapper that does this call, // static Player getPlayer(int num) {return Game.getInstance()._getPlayer(num); }
    // if you do this for every method you can put them as private and rename with an underscore
    // It's a lot of boilerplate, but it allows a bunch of flexibility, you could move the internal data, to other classes
    // you could load the data from file.
}