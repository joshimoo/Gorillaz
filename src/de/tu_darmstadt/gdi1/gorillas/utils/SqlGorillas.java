package de.tu_darmstadt.gdi1.gorillas.utils;

import java.util.ArrayList;

/**
 * Created by Georg Schmidt on 17.02.2015.
 */
public class SqlGorillas
{
    private String file;
    private SqlLiteDb db;
    private String table;

    /**
     * Constructor with default table "Gorillas"
     */
    public SqlGorillas()
    {
        file = "data_gorillas.hsc";
        table = "Gorillas";
        db = new SqlLiteDb(file ,table);
    }

    /**
     * Constructor with parameter
     * @param File  Database filename
     * @param Table Name of the table
     */
    public SqlGorillas(String File, String Table)
    {
        file = File;
        table = Table;
        db = new SqlLiteDb(File,table);
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
        String sql = "INSERT INTO " + table +
                     "(ID, PlayerName, NumberRounds, NumberWinRounds, NumberThrows) " +
                     "VALUES ( NULL, '" + PlayerName + "', " + NumberRounds + "," + NumberWinRounds + "," + NumberThrows + " );";
        db.update(sql);
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0=Name|1=Score]
     */
    public String[][] getHighScore()
    {
        String sql = "SELECT PlayerName, NumberRounds, NumberWinRounds," +
                     "ROUND((CAST(NumberWinRounds as real) / NumberRounds) * 100,0)  AS WinRate, ROUND((CAST(NumberThrows as real) / NumberWinRounds),2) AS HitRate " +
                     "FROM " + table + " ORDER BY WinRate, HitRate DESC LIMIT 0,10;";
        ArrayList list = db.queryArrayList(sql);

        String[][] out = new String[list.size()][5];

        for (int i = 0; i < list.size(); i++)
        {
            ArrayList resultList = (ArrayList) list.get(i);
            out[i][0] = (String) resultList.get(0);
            out[i][1] = resultList.get(1).toString();
            out[i][2] = resultList.get(2).toString(); // int
            out[i][3] = resultList.get(3).toString(); // double
            out[i][4] = resultList.get(4).toString(); // double
        }
        return out;
    }

    /**
     *
     */
    public void insertPlayerName(String PlayerName, int ID)
    {
        // Only for MYSQL
        //String sql = "INSERT INTO " + table +" (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "') ON DUPLICATE KEY UPDATE PlayerName='" + PlayerName + "';";

        String sql="INSERT OR REPLACE INTO " + table + " (ID, PlayerName) VALUES (" + ID + ", '" + PlayerName + "');";

        db.update(sql);
    }

    public String[] getPlayerName()
    {
        String sql = "SELECT PlayerName FROM " + table + " ORDER BY ID DESC LIMIT 0,2;";
        ArrayList list = db.queryArrayList(sql);

        String[] out = new String[list.size()];

        for (int i = 0; i < list.size(); i++)
        {
            ArrayList resultList = (ArrayList) list.get(i);
            out[i] = (String) resultList.get(0);
        }
        return out;
    }

    /**
     * For testing only
     * Shows current highscores
     * @param args
     */
    public static void main(String[] args)
    {
        SqlGorillas db = new SqlGorillas();

        String[][] highScore_list = db.getHighScore();

        for (int i = 0; i < highScore_list.length; i++)
        {
            for (int j = 0; j < 5; j++) {
                System.out.print(highScore_list[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public void shutdown()
    {
        db.shutdown();
    }
}
