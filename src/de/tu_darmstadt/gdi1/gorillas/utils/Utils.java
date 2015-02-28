package de.tu_darmstadt.gdi1.gorillas.utils;

public final class Utils {

    private static final String LIB_LWJGL_NATIVE = "/lib/lwjgl-2.9.1/native/";

    /** This class should never be instantiated */ private Utils() {}

    /**
     * This sets the Native path for lwjgl
     * it evaluates the OS to load the appropriate native implementation library
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

    /** [R]andom [N]ame [G]enerator */
    public static final String[] RNG = {
        "Friedolien", "Hans", "Gunter", "Mr. Mega", "Heinrich", "Franz", "Billy Willy",
        "Luci", "Jan Peters", "Teekanne", "Charlie", "Bobo", "AND-Gate", "Toastbrot",
        "Dinkelberg", "BigSmoke", "Doland", "Gooby", "Falk", "Fedor"
    };
    // TODO: More funny names

    /** Return a Random name between 4 - 12 characters. */
    public static String getRandomName(){
        return RNG[ (int) (java.lang.Math.random() * RNG.length) ];
    }

}
