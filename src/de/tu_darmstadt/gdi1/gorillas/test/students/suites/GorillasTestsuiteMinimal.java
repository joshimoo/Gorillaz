package de.tu_darmstadt.gdi1.gorillas.test.students.suites;

import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.NameInputTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.NewGameTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.ParameterInputTest;
import de.tu_darmstadt.gdi1.gorillas.test.students.testcases.ThrowTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GorillasTestsuiteMinimal {

    public static Test suite() {

        TestSuite suite = new TestSuite("Student tests for Gorillas - Minimal");
        suite.addTest(new JUnit4TestAdapter(ThrowTest.class));
        suite.addTest(new JUnit4TestAdapter(ParameterInputTest.class));
        suite.addTest(new JUnit4TestAdapter(NewGameTest.class));
        suite.addTest(new JUnit4TestAdapter(NameInputTest.class));
        return suite;
    }
}
