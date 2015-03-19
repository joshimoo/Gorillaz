package de.tu_darmstadt.gdi1.gorillas.test.students.testcases;

import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended1;
import de.tu_darmstadt.gdi1.gorillas.test.adapter.GorillasTestAdapterExtended2;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class MapGenerationTest {

    GorillasTestAdapterExtended1 adapter;

    @Before
    public void setUp() {
        adapter = new GorillasTestAdapterExtended2();
        adapter.createRandomMap(1000, 600, 50, 50);
    }

    @Test
    public void testBuildingsValidity() {

        ArrayList<Vector2f> buildingCoordinates = adapter.getBuildingCoordinates();

        for (int i = 1; i < buildingCoordinates.size(); i++) {

            Vector2f lastBuildingCoordinate = buildingCoordinates.get(i - 1);
            Vector2f currentBuildingCoordinate = buildingCoordinates.get(i);

            float lastBuildingX = lastBuildingCoordinate.x;
            float currentBuildingX = currentBuildingCoordinate.x;

            float lastBuildingY = lastBuildingCoordinate.y;
            float currentBuildingY = currentBuildingCoordinate.y;

            assertTrue("The buidlings should be ordered from left to right. The right neighbour" + " of a building has to have a bigger x value than the building.", lastBuildingX < currentBuildingX);

            if (i == 1) {
                assertTrue("The first building should not be higher than frameHight - 100.", lastBuildingY < adapter.getMapFrameHeight());
            }
            assertTrue("The buildings should not be higher than frameHight - 100.", currentBuildingY < adapter.getMapFrameHeight());
        }

        assertTrue("There should be at least six buildings.", buildingCoordinates.size() >= 6);
    }

    @Test
    public void testGorillaValidity() {

        ArrayList<Vector2f> buildingCoordinates = adapter.getBuildingCoordinates();

        Vector2f leftGorillaCoordinate = adapter.getLeftGorillaCoordinate();
        Vector2f rightGorillaCoordinate = adapter.getRightGorillaCoordinate();

        int thirdLastIndex = buildingCoordinates.size() - 3;

        float forthBuildingX = buildingCoordinates.get(3).x;
        float thirdLastBuildingX = buildingCoordinates.get(thirdLastIndex).x;

        assertTrue("The left gorilla should be placed on either the first, second or third building.", leftGorillaCoordinate.x < forthBuildingX);
        assertTrue("The right gorilla should be placed on either the first, second or third building from the right.", rightGorillaCoordinate.x > thirdLastBuildingX);

        int leftGorillaBuilding = 0;
        int rightGorillaBuilding = buildingCoordinates.size() - 1;

        // get index of the buildings the gorillas are placed on
        for (int i = 1; i < buildingCoordinates.size(); i++) {

            Vector2f lastBuildingCoordinate = buildingCoordinates.get(i - 1);
            Vector2f currentBuildingCoordinate = buildingCoordinates.get(i);

            float lastBuildingX = lastBuildingCoordinate.x;
            float currentBuildingX = currentBuildingCoordinate.x;

            if (leftGorillaCoordinate.x <= currentBuildingX && leftGorillaCoordinate.x >= lastBuildingX) {
                leftGorillaBuilding = i - 1;
            }

            if (rightGorillaCoordinate.x <= currentBuildingX && rightGorillaCoordinate.x >= lastBuildingX) {
                rightGorillaBuilding = i - 1;
            }
        }

        // this tests whether the gorillas really stand on their buildings;
        // there must'nt be a gap between the gorilla and the building.
        assertTrue("The left gorilla should really stand on a building.", leftGorillaCoordinate.y == buildingCoordinates.get(leftGorillaBuilding).y - adapter.getGorillaHeight() / 2);
        assertTrue("The right gorilla should really stand on a building.", rightGorillaCoordinate.y == buildingCoordinates.get(rightGorillaBuilding).y - adapter.getGorillaHeight() / 2);

        // this test whether the gorillas stand in the middle of their buildings
        float leftGorillaBuildingWidth = buildingCoordinates.get(leftGorillaBuilding + 1).x - buildingCoordinates.get(leftGorillaBuilding).x;
        float expectedLeftGorillaX = buildingCoordinates.get(leftGorillaBuilding).x + leftGorillaBuildingWidth / 2;

        float rightGorillaBuildingWidth;
        if (!(rightGorillaBuilding == buildingCoordinates.size() - 1)) {
            rightGorillaBuildingWidth = buildingCoordinates.get(rightGorillaBuilding + 1).x - buildingCoordinates.get(rightGorillaBuilding).x;
        }
        else {
            rightGorillaBuildingWidth = adapter.getMapFrameWidth() - buildingCoordinates.get(rightGorillaBuilding).x;
        }

        float expectedRightGorillaX = buildingCoordinates.get(rightGorillaBuilding).x + rightGorillaBuildingWidth / 2;

        // TODO: Their still is a bug, on the right player, a rounding error in this test
        assertTrue("The left gorilla should stand exactly in the middle of its building.", leftGorillaCoordinate.x == expectedLeftGorillaX);
        assertTrue("The right gorilla should stand exactly in the middle of its building.", rightGorillaCoordinate.x == expectedRightGorillaX);
    }
}
