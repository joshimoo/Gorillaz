package de.tu_darmstadt.gdi1.gorillas.main;

// Maybe Player should be an entity?
// Or Take care of gorrilla?
public class Player {

    public static final int MAX_HEALTH = 100;

    private final String name;
    private int health;
    private int score;
    private int lastSpeed, lastAngle;
    private int numberOfWins = 0;
    private int numberOfThrows = 0;
    private int totalNumberOfThrows = 0;

    /** Create a new Player with the given name, a score of zero and MAX_HEALTH */
    public Player(final String name){
        this.name   = name;
        this.health = MAX_HEALTH;
        this.score  = 0;
        this.lastSpeed = 80;
        this.lastAngle = 60;
    }

    /** Return the Players name */
    public String getName(){
        return this.name;
    }

    /** Clamps Health between 0 ans MAX_HEALTH */
    private void setHealth(final int n){
        this.health = Math.min(Math.max(0, n), MAX_HEALTH);
    }

    /** Decrease the Players Health by n */
    public void damage(final int n){
        if(n > 0)
            setHealth(this.health + n);
    }

    /** Increase the Players health by n. */
    public void heal(final int n){
        if(n > 0)
            setHealth(this.health + n);
    }

    /** Returns the Players current health. [0, MAX_HEALTH]*/
    public int getHealth(){
        return this.health;
    }

    /** Add n amount to the current Player score. */
    public void addScore(final int n){
        if(n > 0)
            this.score += n;
    }

    /** Returns the current Playerscore */
    public int getScore(){
        //TODO: Score = numberOfWins
        score = numberOfWins;
        
        return this.score;
    }

    /** Sets the last speed value of the players throw */
    public void setLastSpeed(int speed) {
        if (speed > 200) lastSpeed = 200;
        else if (speed < 0) lastSpeed = 0;
        else lastSpeed = speed;
    }

    /** Sets the last angle value of the players throw */
    public void setLastAngle(int angle)
    {
        if (angle > 360) lastAngle = 360;
        else if(angle < 0) lastAngle = 0;
        else lastAngle =angle;
    }

    /** Gets the last speed value of the players throw */
    public int getLastSpeed() {
        return lastSpeed;
    }

    /** Gets the last angle value of the players throw */
    public int getLastAngle()
    {
        return lastAngle;
    }

    /** Increase number of throws */
    public void setThrow() { numberOfThrows += 1; }

    /** Get number of throws */
    public int getThrow() { return numberOfThrows;}

    /** Get total number of throws and resets current number*/
    public int getTotalThrows() { return totalNumberOfThrows + numberOfThrows;}

    /** Reset the number of throws  to 0 */
    public void resetThrow() {
        totalNumberOfThrows += numberOfThrows;
        numberOfThrows = 0;
    }

    /** Increase number of wins about the value of number */
    public void setWin() { numberOfWins += 1; }

    /** Get number of throws */
    public int getWin() { return numberOfWins;}
}
