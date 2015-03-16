package de.tu_darmstadt.gdi1.gorillas.utils;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Georg Schmidt on 17.02.2015.
 */
public class SqlLiteDb
{
    private String databaseFile;
    private Connection c;
    private String createTableCommand;

    /**
     * Constructor for a SQL-Lite database
     *
     * @param File
     */
    public SqlLiteDb(String File)
    {
        this.databaseFile = File;
        this.c = ConnectingToDatabase();
        this.createTableCommand = "CREATE TABLE IF NOT EXISTS Config" +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Param TEXT NOT NULL," +
                "Value TEXT NOT NULL" +
                ");";
    }

    /**
     * Opens the database (SQL-Lite database file)
     *
     * @return Connection of the db
     */
    public Connection ConnectingToDatabase()
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
        return c;
    }

    /**
     * Allow update the database
     *
     * @param sql_in
     * @return boolean if update was fine
     */
    public boolean update(String sql_in)
    {
        Statement stmt;
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
    public ArrayList queryArrayList(String sql_in)
    {
        ArrayList out = new ArrayList();

        Statement stmt;
        try
        {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(sql_in);

            ArrayList column = new ArrayList();
            // Work with the result
            while (result.next())
            {
                /*
                int i = 0;
                while (i<4){
                    String value = result.getString(i++);
                    System.out.println(value);
                    if(value == null)
                        break;
                    else
                        column.add(value);
                }*/


                // Depends on the database-structure !
                try
                {
                    column.add(result.getInt("ID"));
                } catch (Exception e)
                {
                    // ID not set, that is ok
                }

                try
                {
                    column.add(result.getString("PlayerName"));
                } catch (Exception e)
                {
                    // ID not set, that is ok
                }

                try
                {
                    column.add(result.getInt("NumberRounds"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                   column.add(result.getInt("NumberWinRounds"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                    column.add(result.getDouble("WinRate"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }

                try
                {
                    column.add(result.getDouble("HitRate"));
                } catch (Exception e)
                {
                    // Score not set, that is ok
                }


                /*
                try
                {
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

    public void shutdown()
    {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkExist(String table)
    {
        String sql;
        switch (table)
        {
            case "Players":
                sql = "CREATE TABLE IF NOT EXISTS " + table +
                        " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "PlayerName TEXT NOT NULL" +
                        ");";
                break;
            case "HighScore":
                sql = "CREATE TABLE IF NOT EXISTS " + table +
                        " (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "PlayerName TEXT NOT NULL," +
                        "NumberRounds INT NOT NULL," +
                        "NumberWinRounds INT NOT NULL," +
                        "NumberThrows INT NOT NULL" +
                        ");";
                break;
            default:
                sql = createTableCommand;
                break;
        }
        update(sql);
    }

    public String getCreateTableCommand() {
        return createTableCommand;
    }

    public void setCreateTableCommand(String createTableCommand) {
        this.createTableCommand = createTableCommand;
    }
}
