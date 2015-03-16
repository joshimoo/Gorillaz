package de.tu_darmstadt.gdi1.gorillas.utils;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Player;

import java.util.ArrayList;

/**
 * Created by User on 16.03.2015.
 */
public class Database {
    protected String fileName;
    protected String tableHighScore;
    protected String tablePlayers;
    protected SqlGorillas db;


    /*
        Cache-Storage to reduce access to the harddrive
     */
    protected ArrayList<String> playerNames = null;
    protected ArrayList<ArrayList> highScore = null;


    public Database() {
        this.fileName = Game.getInstance().getDatabaseFile();
        this.tableHighScore = "HighScore";
        this.tablePlayers = "Players";
        this.db = new SqlGorillas(this.fileName, this.tableHighScore, this.tablePlayers);
        this.highScore = new ArrayList<>(200);
    }

    private static Database database;
    public static Database getInstance() {
        if (database == null) { database = new Database(); }
        return database;
    }

    public void savePlayerNames()
    {
        if(Game.getInstance().getStorePlayerNames()) {
            int num = 0;
            for(Player p : Game.getInstance().getPlayers())
                db.insertPlayerName(p.getName(),num++);
            db.shutdown();
        }
    }

    public String[] getPlayerNames()
    {
        if (Game.getInstance().getStorePlayerNames()) {
            if(playerNames == null)
            {
                this.playerNames = db.getPlayerName();
            }
        }
        else
        {
            playerNames = new ArrayList<String>(Game.getInstance().MAX_PLAYER_COUNT);
            for (int i = 0; i < Game.getInstance().MAX_PLAYER_COUNT; i++) {
                String randomName = Utils.getRandomName();
                if(playerNames.contains(randomName)) {
                    playerNames.add(randomName);
                }
                else
                {
                    i--;
                }
            }
        }
        String[] players = new String[Game.getInstance().MAX_PLAYER_COUNT];
        this.playerNames.toArray(players);
        return players;
    }

    public void setHighScore(String PlayerName, int NumberRounds, int NumberWinRounds, int NumberThrows)
    {
        ArrayList player = new ArrayList(4);
        player.add(PlayerName);
        player.add(NumberRounds);
        player.add(NumberWinRounds);
        player.add(NumberThrows);
        highScore.add(player);
    }

    public void writeToFile()
    {
        for(ArrayList l : highScore)
            db.insertHighScore((String) l.get(0),(Integer) l.get(1),(Integer)  l.get(2),(Integer) l.get(3));
        savePlayerNames();
    }

    public void readFromFile()
    {
        ArrayList<ArrayList> highScores = db.getHighScore();
        for(ArrayList list: highScores)
        highScore.add(list);

        // Also read PlayerNames
        getPlayerNames();
    }

    public String[][] getHighScore()
    {
        String[][] highScores = new String[this.highScore.size()][4];
        this.highScore.toArray(highScores);
        return highScores;
    }
}
