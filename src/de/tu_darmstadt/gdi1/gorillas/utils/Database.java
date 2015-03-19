package de.tu_darmstadt.gdi1.gorillas.utils;

import de.tu_darmstadt.gdi1.gorillas.main.Game;

public class Database {
    protected String fileName;
    protected String tableHighScore;
    protected String tablePlayers;
    protected SqlGorillas dbSQL;

    /**
        Cache-Storage to reduce access to the harddrive
     */
    protected String[] playerNames = null;

    public Database() {
        this.fileName = Game.getInstance().getDatabaseFile();
        this.tableHighScore = "HighScore";
        this.tablePlayers = "Players";
        this.dbSQL = new SqlGorillas(this.fileName, this.tableHighScore, this.tablePlayers);
    }

    /**
     * Returns a instance of database
     * @return database
     */
    private static Database database;
    public static Database getInstance() {
        if (database == null) { database = new Database(); }
        return database;
    }

    /**
     * Inserts the PlayerNames to the database
     */
    public void savePlayerNames() {
        if (Game.getInstance().getStorePlayerNames() && (playerNames != null)) {
            int num = 0;
            for (String p : getPlayerNames()) { dbSQL.insertPlayerName(p, num++); }
            debug(0);
        }
    }

    /**
     * Returns a String-Array of PlayerNames from the Cache, SQL or RandomGenerator
     * @return
     */
    public String[] getPlayerNames() {
        if (!Game.getInstance().getStorePlayerNames()) {
            this.playerNames = createPlayerNames();
        }
        else if (Game.getInstance().getStorePlayerNames() && playerNames == null) {
            this.playerNames = dbSQL.getPlayerName();
            if (this.playerNames.length == 0) {
                this.playerNames = createPlayerNames();
            }
            debug(1);
        }
        return this.playerNames;
    }

    /**
     * Stores a String-Array of Playernames in the Cache
     * @param playerNames String-Array of Playernames
     */
    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    /**
     * Creates Random 2 PlayerNames not identical
     * @return String-Array of PlayerNames
     */
    public String[] createPlayerNames() {
        String[] newNames = new String[Game.getInstance().MAX_PLAYER_COUNT];
        do {
            newNames[0] = Utils.getRandomName();
            newNames[1] = Utils.getRandomName();
        } while (newNames[0].equals(newNames[1]));
        debug(2);
        return newNames;
    }

    /**
     * Saves a HighScore to the SQL-DB
     * @param PlayerName    PlayerName
     * @param NumberRounds  Number of rounds
     * @param NumberWinRounds   Number of won rounds
     * @param NumberThrows      Number of throws
     */
    public void setHighScore(String PlayerName, int NumberRounds, int NumberWinRounds, int NumberThrows) {
        dbSQL.insertHighScore(PlayerName, NumberRounds, NumberWinRounds, NumberThrows);
        debug(3);
    }

    /**
     * Saves the PlayerNames to the SQL-Database
     */
    public void writeToFile() {
        savePlayerNames();
        dbSQL.shutdown();
    }

    /**
     * Loads the PlayerNames from SQL
     */
    public void readFromFile() {
        /*
        String[][] temp = dbSQL.getHighScore();
        for (int i = 0; i < temp.length; i++) {
            setHighScore(temp[i][0], Integer.parseInt(temp[i][1]), Integer.parseInt(temp[i][2]), Integer.parseInt(temp[i][3]));
        }
        debug(4);
        */

        // Also read PlayerNames
        getPlayerNames();
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0 = Name | 1 = NumberRounds | 2 = NumberWinRounds| 3 = WinRate| 4 = HitRate]
     */
    public String[][] getHighScore() {
        return dbSQL.getHighScore();
    }

    /**
     * Shows the Debug-Messages depending on the ID-Number
     * @param id ID-Number
     */
    private void debug(int id) {
        if (Game.getInstance().getDebug()) {
            String message;
            switch (id) {
                case 0:
                    message = "Schreibe zu File: Namen";
                    break;
                case 1:
                    message = "Lese von File: Namen";
                    break;
                case 2:
                    message = "Erzeuge Namen";
                    break;
                case 3:
                    message = "Schreibe zu File: HighScore";
                    break;
                case 4:
                    message = "Lese von File: HighScore";
                    break;
                case 5:
                    if(dbSQL.getValue("ConfigSaved").equals("1"))
                        message = "Loading config form database";
                    else
                        message = "No config to load form database";
                    break;
                default:
                    message = "";
            }
            System.out.println((char) 27 + "[30;43m" + message + (char) 27 + "[0m");
            /* Color-Codes: https://en.wikipedia.org/wiki/ANSI_escape_code#Colors */
        }
    }

    /**
     * Clear the stores HighScores
     */
    public void clearHighScore()
    {
        this.dbSQL.clearHighScore();
    }

    /**
     * Clear the whole database
     */
    public void clearDatabase()
    {
        this.dbSQL.clearDatabase();
    }

    /**
     * Clear the whole database and reset the PlayerNames in Database
     */
    public void clearDatabaseAndResetPlayerNames()
    {
        playerNames = null;
        this.dbSQL.clearDatabase();
    }

    /**
     * Return the HighScore on position pos
     * @param pos Position from 0
     * @return String-Array of the HighScore-Values
     */
    public String[] getHighScore(int pos)
    {
        if(pos >= 0 && pos < getHighScore().length)
            return this.getHighScore()[pos];
        else
            return new String[0];
    }

    /**
     * Calculates the WinRate
     * @param wonRounds
     * @param totalRounds
     * @return WinRate
     */
    public double calcWinRate(int wonRounds, int totalRounds) {
        return (double) wonRounds / totalRounds;
    }

    /**
     * Calculates the HitRate
     * @param totalThrows
     * @param wonRounds
     * @return HitRate
     */
    public double calcHitRate(int totalThrows, int wonRounds) {
        return (double) totalThrows / wonRounds;
    }

    /**
     * Store a String-Value
     * @param id    String-ID
     * @param value Value to store
     */
    public void setValue(String id, String value) {
        dbSQL.setValue(id,value);
    }

    /**
     * Get a String-Value
     * @param id    String-ID
     * @return String-Value
     */
    public String getValue(String id) {
        return dbSQL.getValue(id);
    }

    public int getDisplayWidth()
    {
        String get = getValue("DisplayWidth");
        if(get.isEmpty())
            return 800;
        else
            return decodeInt(get);
    }

    public void setDisplayWidth( int width)
    {
        setValue("DisplayWidth", encodeInt(width));
    }

    public int getDisplayHeight()
    {
        String get = getValue("DisplayHeight");
        if(get.isEmpty())
            return 600;
        else
            return decodeInt(get);
    }

    public void setDisplayHeight( int Height)
    {
        setValue("DisplayHeight", encodeInt(Height));
    }

    public int getCanvasWidth()
    {
        String get = getValue("CanvasWidth");
        if(get.isEmpty())
            return 1024;
        else
            return decodeInt(get);
    }

    public void setCanvasWidth( int width)
    {
        setValue("CanvasWidth", encodeInt(width));
    }

    public int getCanvasHeight()
    {
        String get = getValue("CanvasHeight");
        if(get.isEmpty())
            return 1024;
        else
            return decodeInt(get);
    }

    public void setCanvasHeight( int Height)
    {
        setValue("CanvasHeight", encodeInt(Height));
    }

    /**
     Saves the settings:
     - Wind
     - InverseControlKeys
     - Gravity
     - SoundVolume
     - StorePlayerNames
     - Mute
     - Debug
     - TestMode
     - DeveloperMode
     */
    public void saveConfigToFile()
    {
        Game gameInstance = Game.getInstance();

        dbSQL.setValue("ConfigSaved", "1");

        // Boolean
        dbSQL.setValue("Wind", encodeBoolean(gameInstance.getWind()));
        dbSQL.setValue("Mute", encodeBoolean(gameInstance.isMute()));
        dbSQL.setValue("InverseControlKeys", encodeBoolean(gameInstance.getInverseControlKeys()));
        dbSQL.setValue("StorePlayerNames", encodeBoolean(gameInstance.getStorePlayerNames()));
        dbSQL.setValue("Debug", encodeBoolean(gameInstance.getDebug()));
        dbSQL.setValue("Developer", encodeBoolean(gameInstance.isDeveloperMode()));

        //Number
        dbSQL.setValue("Gravity", encodeFloat(gameInstance.getGravity()));
        dbSQL.setValue("SoundVolume", encodeFloat(gameInstance.getSoundVolume()));
    }

    /**
     Loads the settings:
     - Wind
     - InverseControlKeys
     - Gravity
     - SoundVolume
     - StorePlayerNames
     - Mute
     - Debug
     - TestMode
     - DeveloperMode
     */
    public void restoreConfigFromFile() {
        if (dbSQL.getValue("ConfigSaved").equals("1")) {
            Game gameInstance = Game.getInstance();

            // Boolean
            gameInstance.setWind(decodeBoolean(dbSQL.getValue("Wind")));
            gameInstance.setMute(decodeBoolean(dbSQL.getValue("Mute")));
            gameInstance.setInverseControlKeys(decodeBoolean(dbSQL.getValue("InverseControlKeys")));
            gameInstance.setStorePlayerNames(decodeBoolean(dbSQL.getValue("StorePlayerNames")));
            gameInstance.setDebug(decodeBoolean(dbSQL.getValue("Debug")));
            gameInstance.setDeveloperMode(decodeBoolean(dbSQL.getValue("Developer")));

            //Number
            gameInstance.setGravity(decodeFloat(dbSQL.getValue("Gravity")));
            gameInstance.setSoundVolume(decodeFloat(dbSQL.getValue("SoundVolume")));
        }

        debug(5);
    }

    /**
     * Converts String to boolean
     * @param in    String
     * @return      boolean
     */
    private static boolean decodeBoolean(String in)
    {
        return in.equals("1");

    }

    /**
     * Converts boolean to String
     * @param in    boolean
     * @return      String
     */
    private static String encodeBoolean(boolean in)
    {
        return in ? "1" : "0";
    }

    /**
     * Converts String to Float
     * @param in    String
     * @return      Float
     */
    private static float decodeFloat(String in)
    {
        return Float.parseFloat(in);

    }

    /**
     * Converts Float to String
     * @param in    Float
     * @return      String
     */
    private static String encodeFloat(float in)
    {
        return Float.toString(in);
    }

    /**
     * Converts String to Double
     * @param in    String
     * @return      Double
     */
    private static double decodeDouble(String in)
    {
        return Double.parseDouble(in);

    }

    /**
     * Converts Double to String
     * @param in    Double
     * @return      String
     */
    private static String encodeDouble(double in)
    {
        return Double.toString(in);
    }

    /**
     * Converts String to Double
     * @param in    String
     * @return      Double
     */
    private static int decodeInt(String in)
    {
        return Integer.parseInt(in);

    }

    /**
     * Converts Double to String
     * @param in    Double
     * @return      String
     */
    private static String encodeInt(int in)
    {
        return Integer.toString(in);
    }

    /**
     * Outputs all current settings of the game that will stored to the database
     * by the function "saveConfigToFile()" and some more.
     */
    public static void debugOutputAllSettings()
    {
            Game gameInstance = Game.getInstance();

            String list = "Current settings stored by \"saveConfigToFile()\" and some more:\n\n" +
            "Wind = " + encodeBoolean(gameInstance.getWind()) + "\n" +
            "Mute = " + encodeBoolean(gameInstance.isMute()) + "\n" +
            "InverseControlKeys = " + encodeBoolean(gameInstance.getInverseControlKeys()) + "\n" +
            "StorePlayerNames = " + encodeBoolean(gameInstance.getStorePlayerNames()) + "\n" +
            "Debug = " + encodeBoolean(gameInstance.getDebug()) + "\n" +
            "TestMode = " + encodeBoolean(gameInstance.isTestMode()) + "\n" +
            "Developer = " + encodeBoolean(gameInstance.isDeveloperMode()) + "\n" +
            "Gravity = " + encodeFloat(gameInstance.getGravity()) + "\n" +
            "SoundVolume = " + encodeFloat(gameInstance.getSoundVolume()) + "\n" +

            // MORE
            "ANGLE_DEFAULT = " + gameInstance.ANGLE_DEFAULT + "\n" +
            "SPEED_DEFAULT = " + gameInstance.SPEED_DEFAULT + "\n" +
            "SUN_FROM_TOP = " + gameInstance.SUN_FROM_TOP + "\n";

            System.out.println(list);
    }
}
