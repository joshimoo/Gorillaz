package de.tu_darmstadt.gdi1.gorillas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Georg Schmidt on 17.02.2015.
 */
public class SqlLiteDb
{
    private static String databaseFile = "data_gorillas.hsc";
    private static final boolean debug = true;
    private static Connection c = ConnectingToDatabase();

    /**
     * Constructor for a SQL-Lite database: default table "Gorillas" and db-file "data_gorillas.hsc"
     */
    public SqlLiteDb()
    {
        String table = "Gorillas";

        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PlayerName TEXT NOT NULL," +
                "NumberRounds INT NOT NULL," +
                "NumberWinRounds INT NOT NULL," +
                "NumberThrows INT NOT NULL" +
                ");";
        update(sql);
    }

    /**
     * Constructor for a SQL-Lite database: default db-file "data_gorillas.hsc"
     *
     * @param Table
     */

    public SqlLiteDb(String Table)
    {
        String table = Table;

        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PlayerName TEXT NOT NULL," +
                "NumberRounds INT NOT NULL," +
                "NumberWinRounds INT NOT NULL," +
                "NumberThrows INT NOT NULL" +
                ");";
        update(sql);
    }

    /**
     * Constructor for a SQL-Lite database
     *
     * @param Table
     * @param File
     */
    public SqlLiteDb(String Table, String File)
    {
        String table = Table;
        databaseFile = File;
        c = ConnectingToDatabase();

        String sql = "CREATE TABLE IF NOT EXISTS " + table +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PlayerName TEXT NOT NULL," +
                "NumberRounds INT NOT NULL," +
                "NumberWinRounds INT NOT NULL," +
                "NumberThrows INT NOT NULL" +
                ");";
        update(sql);
    }

    /**
     * Opens the database (SQL-Lite database file)
     *
     * @return Connection of the db
     */
    public static Connection ConnectingToDatabase()
    {
        Connection c = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        if (debug)
            System.out.println("Opened database successfully");
        return c;
    }

    /**
     * Allow update the database
     *
     * @param sql_in
     * @return boolean if update was fine
     */
    public static boolean update(String sql_in)
    {
        Statement stmt = null;
        try
        {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate(sql_in);
            stmt.close();

        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns the answer as a ArrayList
     *
     * @param sql_in
     * @return ArrayList
     */
    public static ArrayList queryArrayList(String sql_in)
    {
        ArrayList out = new ArrayList();

        Statement stmt = null;
        try
        {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(sql_in);

            ArrayList column = new ArrayList();
            // Work with the result
            while (result.next())
            {
                // Depends on the database-structure !
                try
                {
                    result.getInt("ID");
                    column.add(result.getInt("ID"));
                } catch (Exception e)
                {
                    // ID not set, that is ok
                }

                try
                {
                    result.getString("PlayerName");
                    column.add(result.getString("PlayerName"));
                } catch (Exception e)
                {
                    // ID not set, that is ok
                }

                try
                {
                    result.getInt("NumberRounds");
                    column.add(result.getInt("NumberRounds"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                    result.getInt("NumberWinRounds");
                    column.add(result.getInt("NumberWinRounds"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                    result.getDouble("WinRate");
                    column.add(result.getDouble("WinRate"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                    result.getDouble("HitRate");
                    column.add(result.getDouble("HitRate"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                /*
                try
                {
                    result.getString("Text");
                    column.add(result.getString("Text"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }
                */
                if (column.size() > 0)
                    out.add(column);
                column = new ArrayList();
            }

            stmt.close();
            result.close();
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
        out.trimToSize();
        return out;
    }

    /**
     * Method prints the result to the Console (Test only)
     *
     * @param sql_in
     * @return
     */

    public static boolean query(String sql_in)
    {
        Statement stmt = null;
        try
        {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(sql_in);

            // Verarbeiten der Daten
            while (result.next())
            {
                // Depends on the database-structure !
                int id = result.getInt("id");
                String name = result.getString("Name");
                int score = result.getInt("Score");
                String text = result.getString("Text");
                float percent = result.getFloat("Percent");

                System.out.println("ID = " + id);
                System.out.println("Name = " + name);
                System.out.println("Score = " + score);
                System.out.println("Text = " + text);
                System.out.println("Percent = " + percent);
                System.out.println();
            }

            stmt.close();
            result.close();
        } catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }
}
