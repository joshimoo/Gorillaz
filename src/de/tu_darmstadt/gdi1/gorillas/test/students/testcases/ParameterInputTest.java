package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterMinimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParameterInputTest {

    GorillasTestAdapterMinimal adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterMinimal();
        adapter.rememberGameData();
        adapter.initializeGame();
        adapter.handleKeyPressN();
        adapter.setPlayerNames("TestPlayer1", "TestPlayer2");
        adapter.startGameButtonPressed();
        adapter.runGame(0);
    }

    @After
    public void finish() {
        adapter.restoreGameData();
    }

    @Test
    public void testVelocityDigitOnly() {

        adapter.fillVelocityInput('a');
        adapter.fillVelocityInput('b');
        adapter.fillVelocityInput('c');
        adapter.fillVelocityInput(' ');
        adapter.fillVelocityInput('�');
        adapter.fillVelocityInput('.');

        assertEquals(-1, adapter.getVelocityInput());
    }

    @Test
    public void testAngleDigitOnly() {

        adapter.fillAngleInput('a');
        adapter.fillAngleInput('b');
        adapter.fillAngleInput('c');
        adapter.fillAngleInput(' ');
        adapter.fillAngleInput('�');
        adapter.fillAngleInput('.');

        assertEquals(-1, adapter.getAngleInput());
    }

    @Test
    public void testVelocityMaximum() {

        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');

        assertEquals(111, adapter.getVelocityInput());

        adapter.resetPlayerWidget();

        adapter.fillVelocityInput('2');
        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');
        adapter.fillVelocityInput('1');

        assertEquals(21, adapter.getVelocityInput());
    }

    @Test
    public void testAngleMaximum() {

        adapter.fillAngleInput('1');
        adapter.fillAngleInput('1');
        adapter.fillAngleInput('1');
        adapter.fillAngleInput('1');

        assertEquals(111, adapter.getAngleInput());

        adapter.resetPlayerWidget();

        adapter.fillAngleInput('2');
        adapter.fillAngleInput('1');
        adapter.fillAngleInput('1');
        adapter.fillAngleInput('1');

        assertEquals(211, adapter.getAngleInput());
    }

    @Test
    public void testVelocityMixedInput() {
        // Valid should be true after having received 1 valid character,
        // before that it should be false and getValue should return -1
        adapter.fillVelocityInput('a'); // still invalid
        adapter.fillVelocityInput('b'); // still invalid
        adapter.fillVelocityInput('c'); // still invalid
        assertEquals(-1, adapter.getVelocityInput());

        // become valid
        adapter.fillVelocityInput('1'); // valid
        adapter.fillVelocityInput('@'); // still valid, we just ignore this character
        adapter.fillVelocityInput('1'); // still valid
        assertEquals(11, adapter.getVelocityInput());

        // Back to invalid
        adapter.resetPlayerWidget();
        adapter.fillVelocityInput('X'); // still invalid
        adapter.fillVelocityInput('2'); // valid
        adapter.fillVelocityInput('X'); // still valid
        assertEquals(2, adapter.getVelocityInput());
    }

    @Test
    public void testAngleMixedInput() {
        // Valid should be true after having received 1 valid character,
        // before that it should be false and getValue should return -1
        adapter.fillAngleInput('a'); // still invalid
        adapter.fillAngleInput('b'); // still invalid
        adapter.fillAngleInput('c'); // still invalid
        assertEquals(-1, adapter.getAngleInput());

        // become valid
        adapter.fillAngleInput('1'); // valid
        adapter.fillAngleInput('@'); // still valid, we just ignore this character
        adapter.fillAngleInput('1'); // still valid
        assertEquals(11, adapter.getAngleInput());

        // Back to invalid
        adapter.resetPlayerWidget();
        adapter.fillAngleInput('X'); // still invalid
        adapter.fillAngleInput('2'); // valid
        adapter.fillAngleInput('X'); // still valid
        assertEquals(2, adapter.getAngleInput());
    }
}
