package de.tu_darmstadt.gdi1.gorillas.test.students.suites;

import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.GamePlayTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.HighscoreTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.MapGenerationTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GorillasTestsuiteExtended1 {

    public static Test suite() {

        TestSuite suite = new TestSuite("Student tests for Gorillas - Extended 1");
        suite.addTest(new JUnit4TestAdapter(HighscoreTest.class));
        suite.addTest(new JUnit4TestAdapter(MapGenerationTest.class));
        suite.addTest(new JUnit4TestAdapter(GamePlayTest.class));
        return suite;
    }
}
