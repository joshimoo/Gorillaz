package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended2;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThrowTestWind {

    GorillasTestAdapterExtended2 adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterExtended2();
    }

    @Test
    public void testVerticalDistortion() {

        Vector2f nextPosition1 = adapter.getNextShotPosition(new Vector2f(0, 400), 90, 80, -15, true, 1000);
        assertTrue("With very strong wind from right to left a vertical thow has to be distorted to the left.", nextPosition1.x < 0);

        Vector2f nextPosition2 = adapter.getNextShotPosition(new Vector2f(0, 400), 90, 80, 15, true, 1000);
        assertTrue("With very strong wind from left to right a vertical thow has to be distorted to the right.", nextPosition2.x > 0);
    }

    @Test
    public void testGoingFurther() {

        Vector2f nextPosition1_1 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, 15, true, 1000);
        Vector2f nextPosition1_2 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 1000);
        assertTrue("A throw to the right with 60 degree starting angle and a speed of 80 has to move further right if there is strong wind from left to right compared to no wind at all.", nextPosition1_1.x > nextPosition1_2.x);

        Vector2f nextPosition2_1 = adapter.getNextShotPosition(new Vector2f(600, 400), 60, 80, -15, false, 1000);
        Vector2f nextPosition2_2 = adapter.getNextShotPosition(new Vector2f(600, 400), 60, 80, false, 1000);
        assertTrue("A throw to the left with 60 degree starting angle and a speed of 80 has to move further left if there is strong wind from right to left compared to no wind at all.", nextPosition2_1.x - nextPosition2_2.x < 0);
    }

    @Test
    public void testGoingShorter() {

        Vector2f nextPosition1_1 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, -15, true, 1000);
        Vector2f nextPosition1_2 = adapter.getNextShotPosition(new Vector2f(0, 400), 60, 80, true, 1000);
        assertTrue("A throw to the right with 60 degree starting angle and a speed of 80 has to move less right if there is strong wind from right to left compared to no wind at all.", nextPosition1_1.x - nextPosition1_2.x < 0);

        Vector2f nextPosition2_1 = adapter.getNextShotPosition(new Vector2f(600, 200), 60, 80, 15, false, 1000);
        Vector2f nextPosition2_2 = adapter.getNextShotPosition(new Vector2f(600, 200), 60, 80, false, 1000);
        assertTrue("A throw to the left with 60 degree starting angle and a speed of 80 has to move less left if there is strong wind from left to right compared to no wind at all.", nextPosition2_1.x - nextPosition2_2.x > 0);
    }

    @Test
    public void testThrow1() {
        testSingleThrow(850, 200, 85, 100, 12, false, 24, 4096.8262f, 689.1327f);
    }

    @Test
    public void testThrow2() {
        testSingleThrow(75, 330, 90, 110, 15, true, 19, 2782.5f, 45);
    }

    @Test
    public void testThrow3() {
        testSingleThrow(880, 180, 0, 40, -4, false, 18, -488, 1800);
    }

    @Test
    public void testThrow4() {
        testSingleThrow(90, 310, 0, 50, -3, true, 13, 486.5f, 1155);
    }

    @Test
    public void testThrow5() {
        testSingleThrow(750, 200, 88, 80, -10, false, 23, -1959.2151f, 1006.1209f);
    }

    @Test
    public void testThrow6() {
        testSingleThrow(80, 150, 89, 90, -14, true, 21, -2974.0150f, 465.2879f);
    }

    @Test
    public void testThrow7() {
        testSingleThrow(850, 180, 130, 70, 2, false, 27, 2793.8686f, 2377.1760f);
    }

    @Test
    public void testThrow8() {
        testSingleThrow(120, 160, 145, 110, 4, true, 26, -870.7748f, 1899.5714f);
    }

    private int scaleTime(int time) {
        return (int) (time / adapter.getTimeScalingFactor());
    }

    private int scaleWind(int wind) {
        return (int) (wind / adapter.getWindScalingFactor());
    }

    private void testSingleThrow(float x0, float y0, int angle, int speed, int wind, boolean fromLeftToRight, int time, float expectedX, float expectedY) {

        float deltaX = 0.001f;
        float deltaY = 0.001f;
        Vector2f nextPosition = adapter.getNextShotPosition(new Vector2f(x0, y0), angle, speed, scaleWind(wind), fromLeftToRight, scaleTime(time));
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree, a speed of " + speed + " and wind of " + wind + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedX, nextPosition.x, deltaX);
        assertEquals("A shot, which is thrown from (" + x0 + ", " + y0 + ") with an angle of " + angle + " degree, a speed of " + speed + " and wind of " + wind + ", has to be at (" + expectedX + ", " + expectedY + ") after " + time + " ms.", expectedY, nextPosition.y, deltaY);
    }

}
