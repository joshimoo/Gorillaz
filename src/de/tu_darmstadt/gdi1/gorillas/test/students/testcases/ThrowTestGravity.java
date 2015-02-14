package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThrowTestGravity {

    GorillasTestAdapterExtended3 adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterExtended3();
        adapter.rememberGameData();
    }

    @After
    public void finish() {
        adapter.restoreGameData();
    }

    @Test
    public void testZeroGravity() {

        Vector2f nextPosition1 = adapter.getNextShotPosition(new Vector2f(0, 400), 2, 2, 0, 0, true, 200);
        assertTrue("Without gravity a throw should never fall down again.", nextPosition1.y < 400);
    }

    @Test
    public void testHigherGravityInfluence() {

        Vector2f nextPosition1_1 = adapter.getNextShotPosition(new Vector2f(0, 400), 45, 80, 0, 10, true, 200);
        Vector2f nextPosition1_2 = adapter.getNextShotPosition(new Vector2f(0, 400), 45, 80, 0, 20, true, 200);

        assertTrue("If the gravity is doubled a throw with apart from that same paramatrization should not fly as far as before anymore.", nextPosition1_2.y - nextPosition1_1.y > 0);
    }

    @Test
    public void testThrow1() {
        testSingleThrow(790, 160, 85, 80, 10, 18, false, 24, 3502.6610f, 3431.3062f);
    }

    @Test
    public void testThrow2() {
        testSingleThrow(95, 305, 40, 140, 12, 12, true, 19, 4298.6782f, 761.1850f);
    }

    @Test
    public void testThrow3() {
        testSingleThrow(830, 400, 0, 170, -6, 5, false, 18, -3202f, 1210f);
    }

    @Test
    public void testThrow4() {
        testSingleThrow(105, 285, 15, 190, -1, 8, true, 13, 2406.3368f, 321.7170f);
    }

    @Test
    public void testThrow5() {
        testSingleThrow(730, 190, 30, 110, 0, 7, false, 23, -1461.0443f, 776.5f);
    }

    @Test
    public void testThrow6() {
        testSingleThrow(95, 130, 115, 45, -5, 1, true, 21, -1406.8743f, -505.9609f);
    }

    @Test
    public void testThrow7() {
        testSingleThrow(790, 230, 145, 70, 4, 20, false, 27, 3796.1974f, 6435.9405f);
    }

    @Test
    public void testThrow8() {
        testSingleThrow(110, 140, 120, 135, 6, 15, true, 26, 383, 2170.2508f);
    }

    private int scaleTime(int time) {
        return (int) (time / adapter.getTimeScalingFactor());
    }

    private int scaleWind(int wind) {
        return (int) (wind / adapter.getWindScalingFactor());
    }

    private void testSingleThrow(float x0, float y0, int angle, int speed, int wind, int gravity, boolean fromLeftToRight, int time, float expectedX, float expectedY) {

        float deltaX = 0.001f;
        float deltaY = 0.001f;
        Vector2f nextPosition = adapter.getNextShotPosition(new Vector2f(x0, y0), angle, speed, scaleWind(wind), gravity, fromLeftToRight, scaleTime(time));
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree, a speed of " + speed + ", wind of " + wind + " and a grvity of " + gravity + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedX, nextPosition.x, deltaX);
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree, a speed of " + speed + ", wind of " + wind + " and a grvity of " + gravity + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedY, nextPosition.y, deltaY);
    }
}
