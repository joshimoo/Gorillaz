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
    public static final int INGAMEWIN       = 8;

    /** Singleton Pattern */
    private Game() {}
    private static Game game;
    public static Game getInstance() {
        if (game == null) { game = new Game(); }
        return game;
    }


    /** Runtime Debug Setting */
    private boolean debug = false;
    public Boolean getDebug() { return debug; }
    public void setDebug(boolean debuging) { debug = debuging; }


    /** We are using a RingBuffer for Player handling
     * One of the reasons we are doing things this way,
     * is in case we do want to try todo network play
     **/
    public final int MAX_PLAYER_COUNT = 2;
    private List<Player> players = new ArrayList<>(MAX_PLAYER_COUNT);

    private int activePlayer = 0;
    public Player getActivePlayer() { return players.get(activePlayer); }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = players.indexOf(activePlayer); }
    public Player toogleNextPlayerActive() { return players.get(++activePlayer % MAX_PLAYER_COUNT); }

    public List<Player> getPlayers() {return players; }
    //public  ListIterator<Player> getPlayers() { return players.listIterator(); } // Iterator is the safe way, but it's just to much boilerplate even for me !_!

    public Player getPlayer(int num){
        if (players != null && num < players.size()) { return players.get(num); }
        else { throw new IllegalArgumentException(String.format("There is no Player at Index: %d currently there are %d players", num, players.size())); }
    }

    private void setPlayer(int num, Player player) {
        if (players != null && num < players.size()) {
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
    // Create a static Wrapper that does this call,
    // if you do this for every method you can put them as private and rename with an underscore
    // It's a lot of boilerplate, but it allows a bunch of flexibility, you could move the internal data, to other classes
    // you could load the data from file.
    // static Player getPlayer(int num) {return Game.getInstance()._getPlayer(num); }
}


