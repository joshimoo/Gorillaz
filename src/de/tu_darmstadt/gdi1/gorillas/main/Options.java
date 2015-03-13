package de.tu_darmstadt.gdi1.gorillas.main;

import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;

/**
 * Created by User on 13.03.2015.
 */
public class Options {
    public final static int getMaxPlayerName(){return  10;}
    public final static void toggleMute(){ GamePlayState.toggleMute();}
    public final static boolean getWind(){return  GamePlayState.getWind();}
    public final static void setWind(boolean value){GamePlayState.setWind(value);}
    public final static boolean getInverseControlKeys(){return  GamePlayState.getInverseControlKeys();}
    public final static void setInverseControlKeys(boolean value){GamePlayState.setInverseControlKeys(value);}
}
