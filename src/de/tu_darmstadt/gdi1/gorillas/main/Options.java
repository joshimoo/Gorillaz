package de.tu_darmstadt.gdi1.gorillas.main;

import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;

/**
 * Created by User on 13.03.2015.
 */
public class Options {
    static boolean developer = true;
    static int maxPlayerName = 10;


    /*
        Getter
     */
    public final static boolean getDeveloperMode(){return  developer;}
    public final static int getMaxPlayerName(){return  maxPlayerName;}
    public final static boolean getWind(){return  GamePlayState.getWind();}
    public final static boolean getInverseControlKeys(){return  GamePlayState.getInverseControlKeys();}


    /*
        Setter
     */
    public final static void setDeveloperMode(boolean value){  developer = value;}
    public final static void setMaxPlayerName(int value){maxPlayerName = value;}
    public final static void setWind(boolean value){GamePlayState.setWind(value);}
    public final static void setInverseControlKeys(boolean value){GamePlayState.setInverseControlKeys(value);}


    /*
        Functions
     */
    public final static void toggleMute(){ GamePlayState.toggleMute();}




}
