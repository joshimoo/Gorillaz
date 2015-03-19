package de.tu_darmstadt.gdi1.gorillas.test.adapter;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Map;
import de.tu_darmstadt.gdi1.gorillas.test.setup.TestGorillas;
import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;
import de.tu_darmstadt.gdi1.gorillas.utils.Database;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class GorillasTestAdapterExtended1 extends GorillasTestAdapterMinimal {

    // Caches our Map, till we are ready to start it.
    private Map map;
    Database db = Database.getInstance();
    public GorillasTestAdapterExtended1() {
        super();
    }

    @Override
    public void rememberGameData() {
        super.rememberGameData();
    }

    @Override
    public void restoreGameData() {
        super.restoreGameData();
    }

    /**
     * This method should create a new RANDOM map. The map should consist of the
     * coordinates of the left and the right gorilla and the upper left edges of
     * the buildings of the skyline. The gorilla positions should mark the
     * center of each gorilla. The buildings of the skyline are rectangular, and
     * not higher than <code>frameHeight</code> minus 100.
     * <p>
     * Important: The y coordinate of the gorillas and the buildings should be
     * denoted with the y axis pointing downwards, as slick demands this.
     * <p>
     * The left gorilla has to be placed on the first, second or third building
     * from the left. Accordingly, the right gorilla has to be placed on the
     * first, second or third building from the right.
     * <p>
     * Gorillas should be placed in the middle of a building and stand on the
     * building.
     * <p>
     * The map should be remembered as the current one. Hence,
     * {@link #getBuildingCoordinates()}, {@link #getLeftGorillaCoordinate()}
     * and {@link #getRightGorillaCoordinate()} should after an invocation of
     * this method return the values of the newly created map.
     * <p>
     * The wind should not blow at all in the map.
     *
     * @param frameWidth  the width of the frame/screen of the gorillas game
     * @param frameHeight the height of the frame/screen of the gorillas game
     */
    public void createRandomMap(int frameWidth, int frameHeight, int gorillaWidth, int gorillaHeight) {
        map = Map.createRandomMap(frameWidth, frameHeight, gorillaWidth, gorillaHeight);
        Game.getInstance().setWind(false);
    }

    /**
     * creates a map, which is NOT RANDOM based on the given parameters
     *
     * @param paneWidth              the width of the frame/window/pane of the game
     * @param paneHeight             the height of the frame/window/pane of the game
     * @param yOffsetCity            the top y offset of the city
     * @param buildingCoordinates    the building coordinates of the city skyline
     * @param leftGorillaCoordinate  the coordinate of the left gorilla
     * @param rightGorillaCoordinate the coordinate of the right gorilla
     */
    public void createCustomMap(int paneWidth, int paneHeight, int yOffsetCity, ArrayList<Vector2f> buildingCoordinates, Vector2f leftGorillaCoordinate, Vector2f rightGorillaCoordinate) {
        // We assume all of these values are set correctly no null values
        map = Map.createMap(paneWidth, paneHeight, yOffsetCity, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
    }

    /**
     * the current, which was created with {@link #createRandomMap(int, int, int, int)} of
     * {@link #createCustomMap(int, int, int, ArrayList, Vector2f, Vector2f)}
     * should be set as current map in the game, if the game is in GamePlayState
     */
    public void startCurrrentMap() {
        // If we are in GamePlayState and we want to load a new map
        // We need to recreate the city
        if(getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            GamePlayState state = (GamePlayState) getStateBasedGame().getCurrentState();
            state.loadMap(map);
        }
    }

    /**
     * should return the building coordinates of the current map
     *
     * @return all the upper left corner's coordinates of the buildings of the
     * current map, ordered from left to right
     */
    public ArrayList<Vector2f> getBuildingCoordinates() {
        // Requires that a map was created in one of the testcases,
        // otherwise map will be null, during gameplay we are guaranteed to have a map created
        return map.getBuildings();
    }

    /**
     * should return the coordinate of the left gorilla in the current map
     *
     * @return the center coordinate of the left gorilla
     */
    public Vector2f getLeftGorillaCoordinate() {
        return map.getLeftGorillaCoordinate();
    }

    /**
     * should return the coordinate of the right gorilla in the current map
     *
     * @return the center coordinate of the right gorilla
     */
    public Vector2f getRightGorillaCoordinate() {
        return map.getRightGorillaCoordinate();
    }

    /**
     * should return the frameWidth, which was given to create the current map
     *
     * @return the frameWidth which was used to create the current map
     */
    public float getMapFrameWidth() {
        return map.getMapFrameWidth();
    }

    /**
     * should return the frameHeight, which was given to create the current map
     *
     * @return the frameHeight which was used to create the current map
     */
    public float getMapFrameHeight() {
        return map.getMapFrameHeight();
    }

    /**
     * should return the gorillaHeight, which was given to create the current
     * map
     *
     * @return the gorillaHeight which was used to create the current map
     */
    public float getGorillaHeight() {
        return map.getGorillaHeight();
    }

    /**
     * should return the gorillaWidth, which was given to create the current map
     *
     * @return the gorillaWidth which was used to create the current map
     */
    public float getGorillaWidth() {
        return map.getGorillaWidth();
    }

    /**
     * adds a highscore to the highscore list
     *
     * @param name           the name of the player
     * @param numberOfRounds the number of rounds played
     * @param roundsWon      the number of rounds the player has one
     * @param bananasThrown  the number of bananas the player has thrown
     */
    public void addHighscore(String name, int numberOfRounds, int roundsWon, int bananasThrown) {
        db.setHighScore(name,numberOfRounds,roundsWon,bananasThrown);
    }

    /**
     * Resets the highscores. Alle entries are deleted and @see
     * {@link #getHighscoreCount()} should return 0.
     */
    public void resetHighscore() {
        db.clearHighScore();
    }

    /**
     * Returns the numnber of highscore entries currently stored.
     *
     * @return number of highscore entries
     */
    public int getHighscoreCount() {
        return db.getHighScore().length;
    }

    /**
     * Returns the player name of the highscore entry at the passed position.
     * The best highscore should be at position 0. See the specification in the
     * task assignment for the definition of best highscore. Positions that are
     * invalid should return null.
     *
     * @param position position of the highscore entry
     * @return playername of the highscore entry at the passed position or null
     * if position is invalid
     */
    public String getNameAtHighscorePosition(int position) {
        String[] score = db.getHighScore(position);
        if(score.length != 0)
            return score[0];
        else
            return null;
    }

    /**
     * Returns the number of rounds played in total of the highscore entry a the
     * passed position The best highscore should be at position 0. See the
     * specification in the task assignment for the definition of best
     * highscore. Positions that are invalid should return -1.
     *
     * @param position position of the highscore entry
     * @return number of rounds played in total of the highscore entry at the
     * passed position or -1 if position is invalid
     */
    public int getRoundsPlayedAtHighscorePosition(int position) {
        String[] score = db.getHighScore(position);
        if(score.length >= position) {
            return Integer.parseInt(score[1]);
        }
        else
            return -1;
    }

    /**
     * Returns the number of rounds won of the highscore entry a the passed
     * position The best highscore should be at position 0. See the
     * specification in the task assignment for the definition of best
     * highscore. Positions that are invalid should return -1.
     *
     * @param position position of the highscore entry
     * @return number of rounds won of the highscore entry at the passed
     * position or -1 if position is invalid
     */
    public int getRoundsWonAtHighscorePosition(int position) {
        String[]score = db.getHighScore(position);
        if(score.length >= position) {
            return Integer.parseInt(score[2]);
        }
        else
            return -1;
    }

    /**
     * Returns the percentage of rounds won of the highscore entry a the passed
     * position The best highscore should be at position 0. See the
     * specification in the task assignment for the definition of best
     * highscore. Positions that are invalid should return -1.
     *
     * @param position position of the highscore entry
     * @return percentage of rounds won of the highscore entry at the passed
     * position or -1 if position is invalid
     */
    public int getPercentageWonAtHighscorePosition(int position) {
        String[] score = db.getHighScore(position);

        if(score.length >= position) {
            return Integer.parseInt(score[3]);
        }
        else
            return -1;
    }

    /**
     * Returns the mean accuracy of the highscore entry a the passed position
     * The best highscore should be at position 0. See the specification in the
     * task assignment for the definition of best highscore. Positions that are
     * invalid should return -1.
     *
     * @param position position of the highscore entry
     * @return mean accuracy of the highscore entry at the passed position or -1
     * if position is invalid
     */
    public double getMeanAccuracyAtHighscorePosition(int position) {
        String[]score = db.getHighScore(position);
        if(position < score.length && position > 0) {
            return Double.parseDouble(score[4]);
        }
        else
            return -1;
    }

    /**
     * if the game is in the GamePlayState, this method should return the
     * current score of player one
     *
     * @return the current score of player one or -1 if the game is not in the
     * GamePlayState
     */
    public int getPlayer1Score() {
        if(gorillas.getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            return  Game.getInstance().getPlayer(0).getWin();
        }

        return -1;
    }

    /**
     * if the game is in the GamePlayState, this method should return the
     * current score of player two
     *
     * @return the current score of player two or -1 if the game is not in the
     * GamePlayState
     */
    public int getPlayer2Score() {
        if(gorillas.getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            return  Game.getInstance().getPlayer(1).getWin();
        }

        return -1;
    }

    /**
     * if the game is in the GamePlayState, this method should return whether it
     * is the turn of player one
     * <p>
     * If it is the turn of a player is decided on the fact if the player is
     * currently able to parameterize a shot.
     *
     * @return true if it is the turn of player one, false if it is the turn of
     * player two or the game is not in GamePlayState or it is not the
     * turn of anyone
     */
    public boolean isPlayer1Turn() {
        if(gorillas.getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            return  Game.getInstance().getPlayer(0) == Game.getInstance().getActivePlayer();
            // TODO: Turn of anyone does that mean if we are currently having an active banana?
        }

        return false;
    }

    /**
     * if the game is in the GamePlayState, this method should return whether it
     * is the turn of player two
     * <p>
     * If it is the turn of a player is decided on the fact if the player is
     * currently able to parameterize a shot.
     *
     * @return true if it is the turn of player two, false if it is the turn of
     * player one or the game is not in GamePlayState or it is not the
     * turn of anyone
     */
    public boolean isPlayer2Turn() {
        if(gorillas.getCurrentStateID() == TestGorillas.GAMEPLAYSTATE) {
            return  Game.getInstance().getPlayer(1) == Game.getInstance().getActivePlayer();
            // TODO: Turn of anyone does that mean if we are currently having an active banana?
        }

        return false;
    }
}
