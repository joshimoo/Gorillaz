package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HighscoreTest {

    GorillasTestAdapterExtended1 adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterExtended1();
        adapter.rememberGameData();
        adapter.resetHighscore();
    }

    @After
    public void finish() {
        adapter.restoreGameData();
    }

    @Test
    public final void testCreateHighscoreEntry() {

        assertEquals("Highscore count should be zero after reset", 0, adapter.getHighscoreCount());

        adapter.addHighscore("PlayerOne", 4, 3, 10);

        assertEquals("Highscore count should be one after adding an entry", 1, adapter.getHighscoreCount());
        assertEquals("The playername of the highscore entry is incorrect", "PlayerOne", adapter.getNameAtHighscorePosition(0));
        assertEquals("The number of rounds played in total of the highscore entry is incorrect", 4, adapter.getRoundsPlayedAtHighscorePosition(0));
        assertEquals("The number of rounds won of the highscore entry is incorrect", 3, adapter.getRoundsWonAtHighscorePosition(0));
    }

    @Test
    public final void testSortHighscores() {

        adapter.addHighscore("PlayerOne", 4, 3, 10);
        adapter.addHighscore("PlayerTwo", 4, 1, 10);
        adapter.addHighscore("PlayerThree", 5, 3, 14);
        adapter.addHighscore("PlayerOne", 5, 2, 15);

        assertEquals("", "PlayerThree", adapter.getNameAtHighscorePosition(0));
        assertEquals("", 3, adapter.getRoundsWonAtHighscorePosition(0));
        assertEquals("", "PlayerOne", adapter.getNameAtHighscorePosition(1));
        assertEquals("", Math.round(5 / 9.0 * 100), adapter.getPercentageWonAtHighscorePosition(1));
        assertEquals("", 5, adapter.getMeanAccuracyAtHighscorePosition(1), 0.0001);
    }

    @Test
    public final void testNullAccess() {

        assertEquals("Highscore count should be zero after reset", 0, adapter.getHighscoreCount());

        adapter.addHighscore("PlayerOne", 3, 3, 3);

        assertNull("Accessing a non existent position should return null", adapter.getNameAtHighscorePosition(-1));
        assertNull("Accessing a non existent position should return null", adapter.getNameAtHighscorePosition(1));

        assertEquals("Accessing a non existent position should return null", -1, adapter.getMeanAccuracyAtHighscorePosition(-1), 0);
        assertEquals("Accessing a non existent position should return null", -1, adapter.getRoundsPlayedAtHighscorePosition(1));

        assertEquals("Accessing a non existent position should return null", -1, adapter.getMeanAccuracyAtHighscorePosition(-1), 0);
        assertEquals("Accessing a non existent position should return null", -1, adapter.getRoundsPlayedAtHighscorePosition(1));

    }
}
