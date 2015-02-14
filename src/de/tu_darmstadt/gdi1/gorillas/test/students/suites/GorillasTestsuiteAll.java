package de.tu_darmstadt.gdi1.gorillas.test.students.suites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GorillasTestsuiteAll {

    public static Test suite() {

        TestSuite suite = new TestSuite("All student's tests for Gorillas");

        suite.addTest(de.tu_darmstadt.gdi1.gorillas.test.students.suites.GorillasTestsuiteMinimal.suite());
        suite.addTest(de.tu_darmstadt.gdi1.gorillas.test.students.suites.GorillasTestsuiteExtended1.suite());
        suite.addTest(de.tu_darmstadt.gdi1.gorillas.test.students.suites.GorillasTestsuiteExtended2.suite());
        suite.addTest(de.tu_darmstadt.gdi1.gorillas.test.students.suites.GorillasTestsuiteExtended3.suite());

        return suite;
    }
}
