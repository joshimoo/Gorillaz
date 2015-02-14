package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterMinimal;
import de.tu_darmstadt.gdi1.gorillas.test.setup.TestGorillas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NewGameTest {

    GorillasTestAdapterMinimal adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterMinimal();
    }

    @After
    public void finish() {
        adapter.stopGame();
    }

    @Test
    public void testNewGame() {

        // Testet, ob das Spiel aus dem MainMenuState, wenn "n" gedr�ckt wird,
        // in den GameSetupState wechselt, wie es in der Aufgabenstellung unter
        // "Neues Spiel starten" vorgegeben ist.
        adapter.initializeGame();
        assertTrue(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.MAINMENUSTATE);
        adapter.handleKeyPressN();
        assertTrue(adapter.getStateBasedGame().getCurrentStateID() == TestGorillas.GAMESETUPSTATE);
    }
}
