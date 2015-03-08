package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GamePlayTest {

    GorillasTestAdapterExtended1 adapter;
    private ArrayList<Vector2f> buildingCoordinates;
    private Vector2f leftGorillaCoordinate;
    private Vector2f rightGorillaCoordinate;

    @Before
    public void setUp() {

        adapter = new GorillasTestAdapterExtended1();

        // just one building -> flat city
        buildingCoordinates = new ArrayList<Vector2f>();
        buildingCoordinates.add(new Vector2f(0, 570));

        // Gorillas should have a width of 37 and a height of 42 in testing
        // mode. (This is the size of the given gorilla image.)
        // That is why the gorilla y coordinate in this case has to be 549.
        leftGorillaCoordinate = new Vector2f(50, 549);
        rightGorillaCoordinate = new Vector2f(950, 549);

        adapter.rememberGameData();

        proceedToGameplayState();
    }

    private void proceedToGameplayState() {

        adapter.stopGame();

        // put in player names and enter the GameplayState
        adapter.initializeGame();
        adapter.handleKeyPressN();
        adapter.setPlayerNames("TestPlayer1", "TestPlayer2");
        adapter.startGameButtonPressed();
        adapter.runGame(0);
    }

    @After
    public void finish() {
        adapter.stopGame();
        adapter.restoreGameData();
    }

    @Test
    public void testGorillaHit() {

        // create a map, with a flat city.
        adapter.createCustomMap(1000, 600, 100, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
        adapter.startCurrrentMap();

        testLeftHitRight();

        testRightHitLeft();
    }

    private void testLeftHitRight() {

        adapter.fillAngleInput('4');
        adapter.fillAngleInput('5');

        adapter.fillVelocityInput('9');
        adapter.fillVelocityInput('5');

        adapter.shootButtonPressed();

        // let the shot fly, it should definitely hit the right gorilla
        for (int i = 0; i < 500; i += 1) {
            // NOTE: adapter.runGame(i); should be:
            adapter.runGame(2);
        }

        assertEquals("The right gorilla was hit, so the score of player one should be 1.", 1, adapter.getPlayer1Score());

        adapter.runGame(2000);

        adapter.runGame(0);

        assertTrue("Player one hit player 2. 2000 ms afterwards it should be player two's turn.", adapter.isPlayer2Turn());
    }

    private void testRightHitLeft() {

        adapter.createCustomMap(1000, 600, 100, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
        adapter.startCurrrentMap();

        adapter.fillAngleInput('4');
        adapter.fillAngleInput('5');

        adapter.fillVelocityInput('9');
        adapter.fillVelocityInput('5');

        adapter.shootButtonPressed();

        // let the shot fly, it should definitely hit the left gorilla now
        for (int i = 0; i < 500; i += 1) {
            adapter.runGame(2);
        }

        assertEquals("The left gorilla was hit, so the score of player two should be 1.", 1, adapter.getPlayer2Score());
    }

    @Test
    public void testCityHit() {

        proceedToGameplayState();

        // construct city in the shape of a "T" rotated 180 degree
        buildingCoordinates.add(new Vector2f(500, 0));
        buildingCoordinates.add(new Vector2f(510, 0));

        adapter.createCustomMap(1000, 600, 100, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
        adapter.startCurrrentMap();

        adapter.fillAngleInput('4');
        adapter.fillAngleInput('5');

        adapter.fillVelocityInput('9');
        adapter.fillVelocityInput('5');

        adapter.shootButtonPressed();

        // let the shot fly, it should definitely hit the city in the middle of
        // the screen.
        // It should not hit the gorilla anymore.
        for (int i = 0; i < 500; i += 1) {
            adapter.runGame(2);
        }

        assertEquals("The right gorilla was not hit because the shot was blocked by the city, so the score of player one should be 0.", 0, adapter.getPlayer1Score());
    }

    @Test
    public void testLeaveScreen() {

        proceedToGameplayState();

        buildingCoordinates = new ArrayList<Vector2f>();
        // just one building -> flat city
        buildingCoordinates.add(new Vector2f(0, 570));

        // create a map, with a flat city.
        adapter.createCustomMap(1000, 600, 100, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
        adapter.startCurrrentMap();

        adapter.fillAngleInput('4');
        adapter.fillAngleInput('5');

        adapter.fillVelocityInput('2');
        adapter.fillVelocityInput('0');
        adapter.fillVelocityInput('0');

        adapter.shootButtonPressed();

        // let the shot fly, it should definitely leave the screen on the right
        for (int i = 0; i < 500; i += 1) {
            adapter.runGame(2);
        }

        adapter.runGame(2000);

        assertTrue("The shot left the screen on the right. 2000 ms afterwards it should definitely be player two's turn.", adapter.isPlayer2Turn());
    }
}
