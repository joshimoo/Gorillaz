package de.tu_darmstadt.gdi1.gorillas.utils;

import java.util.ArrayList;

/**
 * Created by Georg Schmidt on 17.02.2015.
 */
public class SqlGorillas
{
    private SqlLiteDb db;
    private String tableHighScore;
    private String tableConfig;

    /**
     * Constructor
     * @param file
     * @param tableHighScore
     * @param tableConfig
     */
    public SqlGorillas(String file, String tableHighScore, String tableConfig)
    {
        this.tableHighScore = tableHighScore;
        this.tableConfig = tableConfig;

        // Open SQL
        db = new SqlLiteDb(file);
        db.checkExist(tableHighScore);
        db.checkExist(tableConfig);
    }

    /**
     * Inserts a new highscore
     *
     * @param PlayerName
     * @param NumberRounds Total number of rounds
     * @param NumberWinRounds Number of rounds you won
     * @param NumberThrows Total number of throws
     */
    public void insertHighScore(String PlayerName, int NumberRounds, int NumberWinRounds, int NumberThrows)
    {
        String sql = "INSERT INTO " + tableHighScore +
                     "(ID, PlayerName, NumberRounds, NumberWinRounds, NumberThrows) " +
                     "VALUES ( NULL, '" + PlayerName + "', " + NumberRounds + "," + NumberWinRounds + "," + NumberThrows + " );";
        db.update(sql);
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0=Name|1=Score]
     */
    public ArrayList getHighScore()
    {
        String sql = "SELECT PlayerName, NumberRounds, NumberWinRounds," +
                     "ROUND((CAST(NumberWinRounds as real) / NumberRounds) * 100,0)  AS WinRate, ROUND((CAST(NumberThrows as real) / NumberWinRounds),2) AS HitRate " +
                     "FROM " + tableHighScore + " ORDER BY WinRate, HitRate DESC LIMIT 0,10;";
        return db.queryArrayList(sql);
    }

    /**
     *
     */
    public void insertPlayerName(String PlayerName, int ID)
    {
        // Only for MYSQL
        //String sql = "INSERT INTO " + table +" (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "') ON DUPLICATE KEY UPDATE PlayerName='" + PlayerName + "';";

        String sql="INSERT OR REPLACE INTO " + tableConfig + " (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "');";

        db.update(sql);
    }

    public ArrayList<String> getPlayerName()
    {
        String sql = "SELECT PlayerName FROM " + tableConfig + " ORDER BY ID DESC LIMIT 0,2;";
        return db.queryArrayList(sql);
    }

    public void shutdown()
    {
        db.shutdown();
    }
}
