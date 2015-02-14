package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended2;
import de.tu_darmstadt.gdi1.gorillas.test.setup.TestGorillas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SunAstonishedTest {

    GorillasTestAdapterExtended2 adapter;
    private ArrayList<Vector2f> buildingCoordinates;
    private Vector2f leftGorillaCoordinate;
    private Vector2f rightGorillaCoordinate;

    @Before
    public void setUp() {

        adapter = new GorillasTestAdapterExtended2();

        buildingCoordinates = new ArrayList<Vector2f>();
        buildingCoordinates.add(new Vector2f(0, 570));

        // Gorillas should have a width of 37 and a height of 42 in testing
        // mode. (This is the size of the given gorilla image.)
        // That is why the gorilla y coordinate in this case has to be 549.
        leftGorillaCoordinate = new Vector2f(50, 549);
        rightGorillaCoordinate = new Vector2f(950, 549);

        adapter.rememberGameData();
    }

    @After
    public void finish() {
        adapter.stopGame();
        adapter.restoreGameData();
    }

    @Test
    public void testSunAstonished() {

        adapter.initializeGame();
        adapter.handleKeyPressN();
        adapter.setPlayerNames("TestPlayer1", "TestPlayer2");
        adapter.startGameButtonPressed();

        adapter.runGame(0);
        assertTrue(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE);

        adapter.createCustomMap(1000, 600, 100, buildingCoordinates, leftGorillaCoordinate, rightGorillaCoordinate);
        adapter.startCurrrentMap();

        adapter.fillAngleInput('6');
        adapter.fillAngleInput('0');

        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('0');

        adapter.shootButtonPressed();

        assertTrue("The sun should not smile at the before the shot flew through it.", !adapter.isSunAstonished());

        // let the shot fly, it should definitely hit the sun
        for (int i = 0; i < 500; i += 1) {
            adapter.runGame(2);
        }

        assertTrue("The sun should be astonished if a shot flew through it.", adapter.isSunAstonished());
    }
}
