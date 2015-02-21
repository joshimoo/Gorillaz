package de.tu_darmstadt.gdi1.gorillas.utils;

import java.util.ArrayList;

/**
 * Created by Georg Schmidt on 17.02.2015.
 */
public class SqlGorillas
{
    private SqlLiteDb db;
    private String table;

    int maxScore = 100;

    /**
     * Constructor with default table "Gorillas"
     */
    public SqlGorillas()
    {
        db = new SqlLiteDb();
        table = "Gorillas";
    }

    public SqlGorillas(String Table)
    {
        table = Table;
        db = new SqlLiteDb(table);
    }

    /**
     * Inserts a new highscore
     *
     * @param Name
     * @param Score
     */
    public void insertHighScore(String Name, int Score)
    {
        String sql = "INSERT INTO " + table +
                "(ID,Name,Score,Text,Percent) " +
                "VALUES (NULL, '" + Name + "', " + Score + ", 'NONE', " + (int) (((double) Score / maxScore) * 100) + " );";
        System.out.println(sql);
        db.update(sql);
    }

    /**
     * Gets the 10 best Players ever
     *
     * @return a String-Array [NR-Player][0=Name|1=Score]
     */
    public String[][] getHighScore()
    {
        String sql = "SELECT Name,Score FROM " + table + " ORDER BY Score DESC LIMIT 0,10;";
        ArrayList list = db.queryArrayList(sql);

        String[][] out = new String[list.size()][2];

        for (int i = 0; i < list.size(); i++)
        {
            ArrayList a = (ArrayList) list.get(i);
            out[i][0] = (String) a.get(0);
            out[i][1] = a.get(1).toString();
        }
        return out;
    }

    /**
     * For testing only
     *
     * @param args
     */
    public static void main(String[] args)
    {
        SqlGorillas db = new SqlGorillas();

        boolean newI = false;
        if (newI)
        {
            db.insertHighScore("Georg", (int) (Math.random() * 100));

            db.insertHighScore("Tami", (int) (Math.random() * 100));

            db.insertHighScore("Gabriel", (int) (Math.random() * 100));
        }

        String[][] a = db.getHighScore();
        for (int i = 0; i < a.length; i++)
        {
            System.out.println(a[i][0] + " hat " + a[i][1]);
        }
    }
}
