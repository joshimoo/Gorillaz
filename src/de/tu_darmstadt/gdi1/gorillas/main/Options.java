package de.tu_darmstadt.gdi1.gorillas.main;

import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;

/**
 * Created by User on 13.03.2015.
 */
public class Options {
    private boolean developer;
    private int maxPlayerName;

    public Options()
    {
        developer = true;
        maxPlayerName = 10;
    }

    private static Options options;
    public static Options getInstance() {
        if (options == null) { options = new Options(); }
        return options;
    }

    /*
        Getter
     */
    public static boolean getDeveloperMode(){return  getInstance().developer;}
    public static int getMaxPlayerName(){return  getInstance().maxPlayerName;}
    public static boolean getWind(){return  GamePlayState.getWind();}
    public static boolean getInverseControlKeys(){return  GamePlayState.getInverseControlKeys();}


    /*
        Setter
     */
    public static void setDeveloperMode(boolean value){  getInstance().developer = value;}
    public static void setMaxPlayerName(int value){getInstance().maxPlayerName = value;}
    public static void setWind(boolean value){GamePlayState.setWind(value);}
    public static void setInverseControlKeys(boolean value){GamePlayState.setInverseControlKeys(value);}


    /*
        Functions
     */
    public static void toggleMute(){ GamePlayState.toggleMute();}
}
