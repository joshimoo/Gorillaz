package de.tu_darmstadt.gdi1.gorillas.utils;

/** Static utility class */
public final class Utils {

    /** Path to the OpenGL native binaries */
    private static final String LIB_LWJGL_NATIVE = "/lib/lwjgl-2.9.1/native/";

    /** This class should never be instantiated */
    private Utils() {}

    /** This sets the Native path for lwjgl
     *  it evaluates the OS to load the appropriate native implementation library
     */
    public static void setNativePath() {
        // @formatter:off
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + LIB_LWJGL_NATIVE + "windows");
        }
        else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + LIB_LWJGL_NATIVE + "macosx");
        }
        else {
            System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + LIB_LWJGL_NATIVE + System.getProperty("os.name").toLowerCase());
        }

        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "false");
        System.err.println(System.getProperty("os.name") + ": " + System.getProperty("org.lwjgl.librarypath"));
        // @formatter:on
    }

    /** Clamps the input x between min and max */
    public static float clamp (final float min, final float x, final float max){
        return Math.max(min, Math.min(x, max));
    }

    /** [R]andom [N]ame [G]enerator */
    public static final String[] RNG = {
            "Friedolien", "Hans", "Gunther", "Mr. Mega", "Heinrich", "Franz", "Billy Willy",
            "Luci", "Jan Peters", "Teekanne", "Charlie", "Bobo", "AND-Gate", "Toastbrot",
            "Dinkelberg", "BigSmoke", "Doland", "Gooby", "Falk", "Fedor", "OR-Gate", "FlipFlop",
            "UgaUgaa", "Skrillex", "Bob Marley", "Stalin", "BrokObamer", "Putin", "Mittwoch",
            "Freitag", "Uganda", "Kony2012", "D120", "Kumar", "Jim Carry", "Jon Snow", "Dexter",
            "Me", "Me2", "Alex", "Chrisi", "Klee"
    };

    /** Return a Random name between 2 - 12 characters. */
    public static String getRandomName(){
        return RNG[ (int) (java.lang.Math.random() * RNG.length) ];
    }

}
