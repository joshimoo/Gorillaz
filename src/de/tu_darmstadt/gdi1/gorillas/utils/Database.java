package de.tu_darmstadt.gdi1.gorillas.utils;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
    protected String[] playerNames = null;
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
        if(Game.getInstance().getStorePlayerNames() && playerNames != null) {
            int num = 0;
            for(Player p : Game.getInstance().getPlayers())
                db.insertPlayerName(p.getName(),num++);
            db.shutdown();
        }
    }

    public String[] getPlayerNames()
    {
        if(!Game.getInstance().getStorePlayerNames()) {
            this.playerNames = createPlayerNames();
        }
        else if(Game.getInstance().getStorePlayerNames())
        {
            playerNames = db.getPlayerName();
            if(playerNames == null)
            {
                this.playerNames = createPlayerNames();
            }
        }

        return this.playerNames;

    }

    public String[] createPlayerNames()
    {
        String[] newNames = new String[Game.getInstance().MAX_PLAYER_COUNT];
        do{
            newNames[0] = Utils.getRandomName();
            newNames[1] = Utils.getRandomName();
        }while(newNames[0].equals(newNames[1]));
        return newNames;
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
