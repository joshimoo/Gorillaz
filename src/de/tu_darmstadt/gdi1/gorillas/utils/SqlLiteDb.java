package de.tu_darmstadt.gdi1.gorillas.utils;

import java.sql.*;
import java.util.ArrayList;

public class SqlLiteDb {
    private String databaseFile;
    private String createTableCommand;
    private Connection c;

    /**
     * Constructor for a SQL-Lite database
     *
     * @param File
     */
    public SqlLiteDb(String File) {
        this.databaseFile = File;
        this.c = ConnectingToDatabase();
        // String to create a default table
        this.createTableCommand = "CREATE TABLE IF NOT EXISTS Config " +
                "(" +
                "ID TEXT PRIMARY KEY NOT NULL," +
                "Value TEXT NOT NULL" +
                ");";
    }

    /**
     * Opens the database (SQL-Lite database file)
     *
     * @return Connection of the db
     */
    public Connection ConnectingToDatabase() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
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
    public boolean update(String sql_in) {
        Statement stmt;
        try {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            stmt.executeUpdate(sql_in);
            stmt.close();
        } catch (Exception e) {
            if(e.getMessage().equals("database is locked"))
                System.err.println("Please close all Games to solve a database conflict [ database is locked ] ! [ SqlLiteDb -> update(String) ]");
            else
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
    public ArrayList queryArrayList(String sql_in) {
        ArrayList out = new ArrayList();

        Statement stmt;
        try {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(sql_in);

            ArrayList column = new ArrayList();
            // Work with the result
            while (result.next()) {
                // Depends on the database-structure !

                try {
                    column.add(result.getInt("ID"));
                } catch (Exception e) {
                    // ID not set, that is ok
                }

                try {
                    column.add(result.getString("PlayerName"));
                } catch (Exception e) {
                    // PlayerName not set, that is ok
                }

                try {
                    column.add(result.getInt("NumberRounds"));
                } catch (Exception e) {
                    // NumberRounds not set, that is ok
                }

                try {
                    column.add(result.getInt("NumberWinRounds"));
                } catch (Exception e) {
                    // NumberWinRounds not set, that is ok
                }

                try {
                    column.add(result.getInt("NumberThrows"));
                } catch (Exception e) {
                    // NumberWinRounds not set, that is ok
                }

                try {
                    column.add(result.getInt("WinRate"));
                } catch (Exception e) {
                    // WinRate not set, that is ok
                }

                try {
                    column.add(result.getDouble("HitRate"));
                } catch (Exception e) {
                    // HitRate not set, that is ok
                }

                if (column.size() > 0) { out.add(column); }
                column = new ArrayList();
            }
            stmt.close();
            result.close();
        } catch (Exception e) {
            if(e.getMessage().equals("database is locked"))
                System.err.println("Please close all Games to solve a database conflict [ database is locked ] ! [ SqlLiteDb -> queryArrayList(String) ]");
            else
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

            return null;
        }
        out.trimToSize();
        return out;
    }

    /**
     * Load String-Value by sql-command with one String-Result named "Value"
     * @param sql_in SQL-Command
     * @return Value
     */
    public String getValue(String sql_in) {
        Statement stmt;
        try {
            // c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet result = stmt.executeQuery(sql_in);

            if (result.next()) {
                return result.getString("Value");
            }
            else {
                return "";
            }
        }catch (Exception e)
        {
            System.err.println(this.getClass().getName()+ " getValue(String) has failed !");
        }
        return "";
    }

    /**
     * Close the db-connection
     */
    public void shutdown() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the table exist
     * @param table Name of the table
     */
    public void checkExist(String table) {
        String sql;
        switch (table) {
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
                sql = getCreateTableCommand();
                break;
        }
        update(sql);
    }

    /**
     * Provides a Standard-Table-Create-String
     * @return like "CREATE TABLE ..."
     */
    public String getCreateTableCommand() {
        return createTableCommand;
    }

    /**
     * Set a Standard-Table-Create-String
     * @param createTableCommand  like "CREATE TABLE ..."
     */
    public void setCreateTableCommand(String createTableCommand) {
        this.createTableCommand = createTableCommand;
    }
}
