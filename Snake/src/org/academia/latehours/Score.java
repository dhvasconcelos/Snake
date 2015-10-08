package org.academia.latehours;

/**
 * Created by cadet on 07/10/15.
 */
public class Score {
    private int currentScore;
    private int highScore = 0;

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
