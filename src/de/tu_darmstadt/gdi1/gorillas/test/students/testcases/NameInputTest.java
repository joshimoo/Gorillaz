package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterMinimal;
import de.tu_darmstadt.gdi1.gorillas.test.setup.TestGorillas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameInputTest {

    GorillasTestAdapterMinimal adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterMinimal();
        adapter.initializeGame();
        adapter.handleKeyPressN();
        adapter.rememberGameData();
    }

    @After
    public void finish() {
        adapter.stopGame();
        adapter.restoreGameData();
    }

    @Test
    public void testCorrectNameInput() {

        adapter.setPlayerNames("TestPlayer1", "TestPlayer2");
        adapter.startGameButtonPressed();

        adapter.runGame(0);
        assertTrue(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE);
    }

    @Test
    public void testFalseNameInput() {

        adapter.setPlayerNames("", "TestPlayer2");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertFalse(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE);

        adapter.setPlayerNames("", "");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertFalse(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE);

        adapter.setPlayerNames("TestPlayer", "TestPlayer");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertFalse(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMEPLAYSTATE);
    }

    @Test
    public void testErrorMessages() {

        String notEmpty = adapter.getEmptyError();
        String notEqual = adapter.getEqualError();

        adapter.setPlayerNames("TestPlayer1", "");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertEquals("", adapter.getPlayer1Error());
        assertEquals(notEmpty, adapter.getPlayer2Error());

        adapter.setPlayerNames("", "");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertEquals(notEmpty, adapter.getPlayer1Error());
        assertEquals(notEmpty, adapter.getPlayer2Error());

        adapter.setPlayerNames("TestPlayer", "TestPlayer");

        adapter.startGameButtonPressed();
        adapter.runGame(0);

        assertEquals(notEqual, adapter.getPlayer1Error());
        assertEquals(notEqual, adapter.getPlayer2Error());
    }
}
