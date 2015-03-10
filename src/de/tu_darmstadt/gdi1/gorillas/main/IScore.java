package de.tu_darmstadt.gdi1.gorillas.main;

/**
 * Defines an Interface for a ScoreManager
 */
public interface IScore {
    void getScore();
    void setScore();
    void incScore();
    void decScore();
    void resetScore();
    void loadScore();
    void saveScore();
}
