package de.tu_darmstadt.gdi1.gorillas.utils;

import java.util.ArrayList;

public class SqlGorillas {
    private SqlLiteDb db;
    private String tableHighScore;
    private String tablePlayer;
    private String tableConfig;

    /**
     * Constructor
     *
     * @param file
     * @param tableHighScore
     * @param tablePlayer
     */
    public SqlGorillas(String file, String tableHighScore, String tablePlayer) {
        this.tableHighScore = tableHighScore;
        this.tablePlayer = tablePlayer;
        this.tableConfig = "Config";

        // Open SQL
        db = new SqlLiteDb(file);
        db.checkExist(tableHighScore);
        db.checkExist(tablePlayer);
        db.checkExist(tableConfig);
    }

    /**
     * Inserts a new HighScore
     *
     * @param PlayerName      PlayerName
     * @param NumberRounds    Total number of rounds
     * @param NumberWinRounds Number of rounds you won
     * @param NumberThrows    Total number of throws
     */
    public void insertHighScore(String PlayerName, int NumberRounds, int NumberWinRounds, int NumberThrows) {
        ArrayList former = getHighScore(PlayerName);

        String ID = "NULL";
        if(former.size()>0) {
            ID = former.get(0).toString();
            NumberRounds += (int) former.get(2);
            NumberWinRounds += (int) former.get(3);
            NumberThrows += (int) former.get(4);
        }

        String sql = "INSERT OR REPLACE INTO " + tableHighScore +
                "(ID, PlayerName, NumberRounds, NumberWinRounds, NumberThrows) " +
                "VALUES ( "+ ID + ", '" + PlayerName + "', " + NumberRounds + "," + NumberWinRounds + "," + NumberThrows + " );";
        db.update(sql);
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0 = Name | 1 = NumberRounds | 2 = NumberWinRounds | 3 = WinRate | 4 = HitRate]
     */
    public String[][] getHighScore() {
        String sql = "SELECT PlayerName, NumberRounds, NumberWinRounds," +
                "CAST(ROUND((CAST(NumberWinRounds AS real) * 100 / NumberRounds),0) AS int) AS WinRate," +
                "ROUND((CAST(NumberThrows as real) / NumberWinRounds),2) AS HitRate " +
                "FROM " + tableHighScore + " ORDER BY WinRate DESC, HitRate DESC LIMIT 0,10;";
        ArrayList list = db.queryArrayList(sql);

        String[][] out = new String[list.size()][5];

        for (int i = 0; i < list.size(); i++) {
            ArrayList resultList = (ArrayList) list.get(i);
            out[i][0] = (String) resultList.get(0);
            out[i][1] = resultList.get(1).toString(); // int
            out[i][2] = resultList.get(2).toString(); // int
            out[i][3] = resultList.get(3).toString(); // int
            out[i][4] = resultList.get(4).toString(); // double
        }
        return out;
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0 = ID | 1 = Name | 2 = NumberRounds | 3 = NumberWinRounds | 4 = NumberThrows  | 5 = WinRate | 6 = HitRate]
     */
    public ArrayList getHighScore(String name) {
        String sql = "SELECT ID, PlayerName, NumberRounds, NumberWinRounds, NumberThrows," +
                "CAST(ROUND((CAST(NumberWinRounds AS real) * 100 / NumberRounds),0) AS int) AS WinRate," +
                "ROUND((CAST(NumberThrows as real) / NumberWinRounds),2) AS HitRate " +
                "FROM " + tableHighScore + " WHERE PlayerName='" + name + "';";
        ArrayList result = db.queryArrayList(sql);
        if(result.size()>0) {
            return (ArrayList) result.get(0);
        }
        else
        {
            return new ArrayList();
        }
    }

    /**
     * Store PlayerName to Database
     * @param PlayerName PlayerName
     * @param ID    ID for ordering the names
     */
    public void insertPlayerName(String PlayerName, int ID) {
        // Only for MYSQL
        //String sql = "INSERT INTO " + table +" (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "') ON DUPLICATE KEY UPDATE PlayerName='" + PlayerName + "';";

        String sql = "INSERT OR REPLACE INTO " + tablePlayer + " (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "');";

        db.update(sql);
    }

    /**
     * Returns a String-Array with all PlayerNames
     * @return all PlayerNames
     */
    public String[] getPlayerName() {
        String sql = "SELECT PlayerName FROM " + tablePlayer + " ORDER BY ID ASC LIMIT 0,2;";
        ArrayList list = db.queryArrayList(sql);

        String[] out = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            ArrayList resultList = (ArrayList) list.get(i);
            out[i] = (String) resultList.get(0);
        }
        return out;
    }

    /**
     * Clears the HighScore-Storage
     */
    public void clearHighScore()
    {
        String sql = "DROP TABLE " + tableHighScore + ";";
        db.update(sql);
        db.checkExist(tableHighScore);
    }

    /**
     * Store a String-Value
     * @param id    String-ID
     * @param value Value to store
     */
    public void setValue(String id, String value) {
        String sql;
        if(value.isEmpty()) {
            sql = "DELETE FROM " + tableConfig + " WHERE ID='" + id + "';";
        }
        else {
            sql = "INSERT OR REPLACE INTO " + tableConfig + " (ID, Value) VALUES ('" + id + "', '" + value + "');";
        }
        db.update(sql);
    }

    /**
     * Get a String-Value
     * @param id    String-ID
     * @return String-Value
     */
    public String getValue(String id) {
        String sql = "SELECT Value FROM " + tableConfig + " WHERE ID='" + id + "';";
        return db.getValue(sql);
    }

    /**
     * Close the DB-Connection
     */
    public void shutdown() {
        db.shutdown();
    }
}
