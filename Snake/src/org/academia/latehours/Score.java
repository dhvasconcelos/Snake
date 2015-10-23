package org.academia.latehours;

import java.io.*;
import java.net.URL;

/**
 * Created by cadet on 07/10/15.
 */
public class Score {
    private int currentScore;
    private int highScore = 0;
    private String file;

    public Score() {
        file = new String("highscore" + Game.getLevel() + Game.getGameDifficulty() + ".txt");
    }

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

    public void saveHighScore() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(new Integer(highScore).toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHighScore() {
        try {
            URL url = getClass().getResource(file.startsWith("/") ? file : "/" + file);

            if(url != null) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream()));
                String score = buffer.readLine();
                highScore = Integer.parseInt(score);

            } else {
                BufferedReader buffer = new BufferedReader(new FileReader(file));
                String score = buffer.readLine();
                highScore = Integer.parseInt(score);
            }

        } catch (IOException e) {

            try {
                FileWriter writer = new FileWriter(file);
                writer.write(new Integer(0).toString());
                writer.close();
                loadHighScore();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}


