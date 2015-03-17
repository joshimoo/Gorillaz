package de.tu_darmstadt.gdi1.gorillas.utils;

import de.tu_darmstadt.gdi1.gorillas.main.Game;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by Georg Schmidt on 16.03.2015.
 */
public class Database {
    protected String fileName;
    protected String tableHighScore;
    protected String tablePlayers;
    protected SqlGorillas dbSQL;

    /*
        Cache-Storage to reduce access to the harddrive
     */
    protected String[] playerNames = null;

    public Database() {
        this.fileName = Game.getInstance().getDatabaseFile();
        this.tableHighScore = "HighScore";
        this.tablePlayers = "Players";
        this.dbSQL = new SqlGorillas(this.fileName, this.tableHighScore, this.tablePlayers);
    }

    private static Database database;

    public static Database getInstance() {
        if (database == null) { database = new Database(); }
        return database;
    }

    public void savePlayerNames() {
        if (Game.getInstance().getStorePlayerNames() && (playerNames != null)) {
            int num = 0;
            for (String p : getPlayerNames()) { dbSQL.insertPlayerName(p, num++); }
            debug(0);
        }
    }

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

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public String[] createPlayerNames() {
        String[] newNames = new String[Game.getInstance().MAX_PLAYER_COUNT];
        do {
            newNames[0] = Utils.getRandomName();
            newNames[1] = Utils.getRandomName();
        } while (newNames[0].equals(newNames[1]));
        debug(2);
        return newNames;
    }

    public void setHighScore(String PlayerName, int NumberRounds, int NumberWinRounds, int NumberThrows) {
        dbSQL.insertHighScore(PlayerName, NumberRounds, NumberWinRounds, NumberThrows);
        debug(3);
    }

    public void writeToFile() {
        savePlayerNames();
        dbSQL.shutdown();
    }

    public void readFromFile() {
        String[][] temp = dbSQL.getHighScore();
        for (int i = 0; i < temp.length; i++) {
            setHighScore(temp[i][0], Integer.parseInt(temp[i][1]), Integer.parseInt(temp[i][2]), Integer.parseInt(temp[i][3]));
        }
        debug(4);

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
                default:
                    message = "";
            }
            System.out.println((char) 27 + "[30;43m" + message + (char) 27 + "[0m");
            /* Color-Codes: https://en.wikipedia.org/wiki/ANSI_escape_code#Colors */
        }
    }


    public void clearHighScore()
    {
        this.dbSQL.clearHighScore();
    }

    /*
        For Tests
     */

    public String[] getHighScore(int pos)
    {
        if(pos >= 0 && pos < getHighScore().length)
            return this.getHighScore()[pos];
        else
            return new String[0];
    }


    public double calcWinRate(int wonRounds, int totalRounds) {
        return (double) wonRounds / totalRounds;
    }

    public double calcHitRate(int totalThrows, int wonRounds) {
        return (double) totalThrows / wonRounds;
    }

}
