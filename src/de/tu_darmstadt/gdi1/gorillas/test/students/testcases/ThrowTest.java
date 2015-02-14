package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterMinimal;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThrowTest {

    GorillasTestAdapterMinimal adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterMinimal();
    }

    @Test
    public void testLeftToRight() {

        // test with a throw starting from x=0 and y=400, which has a starting
        // angle of 60 degree and a speed of 80
        Vector2f nextPosition1 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 200);
        assertTrue("A shot, which is thrown from left to right, has to move in positive x direction", nextPosition1.x > 0);
        Vector2f nextPosition2 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 400);
        assertTrue("A shot, which is thrown from left to right, has to be further right after 200 ms passed. (If there is no wind.)", nextPosition2.x > nextPosition1.x);
        Vector2f nextPosition3 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 30000);
        assertTrue("After 30 seconds the shot should definitly have fallen below its starting y value", nextPosition3.y > 400);

    }

    @Test
    public void testRightToLeft() {

        // test with a throw starting from x=600 and y=400, which has a starting
        // angle of 60 degree and a speed of 80
        Vector2f nextPosition1 = adapter.getNextShotPosition(new Vector2f(600, 400), 60, 80, false, 200);
        assertTrue("A shot, which is thrown from right to left, has to move in negative x direction", nextPosition1.x < 600);
        Vector2f nextPosition2 = adapter.getNextShotPosition(new Vector2f(600, 400), 60, 80, false, 400);
        assertTrue("A shot, which is thrown from right to left, has to be further left after 200 ms passed. (If there is no wind.)", nextPosition2.x < nextPosition1.x);
        Vector2f nextPosition3 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 30000);
        assertTrue("After 30 seconds the shot should definitly have fallen below its starting y value", nextPosition3.y > 400);
    }

    @Test
    public void testThrow1() {
        testSingleThrow(60, 300, 60, 40, true, 13, 320, 694.6668f);
    }

    @Test
    public void testThrow2() {
        testSingleThrow(750, 400, 70, 65, false, 15, 416.5304f, 608.7997f);
    }

    @Test
    public void testThrow3() {
        testSingleThrow(100, 450, 45, 180, true, 25, 3281.9805f, 393.0195f);
    }

    @Test
    public void testThrow4() {
        testSingleThrow(800, 250, 45, 170, false, 26, -2325.4120f, 504.5880f);
    }

    @Test
    public void testThrow5() {
        testSingleThrow(50, 350, 89, 200, true, 10, 84.9048f, -1149.6954f);
    }

    @Test
    public void testThrow6() {
        testSingleThrow(950, 200, 92, 180, false, 11, 1019.1010f, -1173.7938f);
    }

    @Test
    public void testThrow7() {
        testSingleThrow(950, 280, 360, 50, true, 10, 1450, 780);
    }

    @Test
    public void testThrow8() {
        testSingleThrow(60, 250, 360, 45, false, 10, -390, 750);
    }

    private int scaleTime(int time) {
        return (int) (time / adapter.getTimeScalingFactor());
    }

    private void testSingleThrow(float x0, float y0, int angle, int speed, boolean fromLeftToRight, int time, float expectedX, float expectedY) {

        float deltaX = 0.001f;
        float deltaY = 0.001f;
        Vector2f nextPosition = adapter.getNextShotPosition(new Vector2f(x0, y0), angle, speed, fromLeftToRight, scaleTime(time));
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree and a speed of " + speed + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedX, nextPosition.x, deltaX);
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree and a speed of " + speed + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedY, nextPosition.y, deltaY);
    }
}
