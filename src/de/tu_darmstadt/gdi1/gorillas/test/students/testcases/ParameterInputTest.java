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
}
