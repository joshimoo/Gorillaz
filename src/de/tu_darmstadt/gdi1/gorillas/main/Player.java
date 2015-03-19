package de.tu_darmstadt.gdi1.gorillas.main;

/** Represents a Player in the Game */
public class Player {
    
    private final String name;
    private int lastSpeed, lastAngle;
    private int numberOfWins;
    private int numberOfThrows;
    private int totalNumberOfThrows;

    /** Create a new Player with the given name, a score of zero and MAX_HEALTH */
    public Player(final String name){
        this.name      = name;
        this.lastSpeed = Game.SPEED_DEFAULT;
        this.lastAngle = Game.ANGLE_DEFAULT;
    }

    /** Returns the Players name */
    public String getName(){
        return this.name;
    }

    /** Set the last speed value of the players throw */
    public void setLastSpeed(int speed) {
        if (speed >= 200)
            lastSpeed = 200;
        else if (speed <= 0)
            lastSpeed = 0;
        else
            lastSpeed = speed;
    }

    /** Set the last angle value of the players throw */
    public void setLastAngle(int angle) {
        if (angle >= 360)
            lastAngle = 360;
        else if(angle <= 0)
            lastAngle = 0;
        else
            lastAngle = angle;
    }

    /** Reset the number of throws  to 0 */
    public void resetThrow() {
        totalNumberOfThrows += numberOfThrows;
        numberOfThrows = 0;
    }

    /** Returns the last speed value of the players throw */
    public int getLastSpeed() { return lastSpeed;  }

    /** Returns the last angle value of the players throw */
    public int getLastAngle() { return lastAngle; }

    /** Increase number of throws */
    public void setThrow() { numberOfThrows += 1; }

    /** Returns the number of throws */
    public int getThrow() { return numberOfThrows;}

    /** Returns the total number of throws */
    public int getTotalThrows() { return totalNumberOfThrows + numberOfThrows;}

    /** Increase number of wins about the value of number */
    public void setWin() { numberOfWins += 1; }

    /** Returns the number of throws  */
    public int getWin() { return numberOfWins;}

}
