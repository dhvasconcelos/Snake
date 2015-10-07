package org.academia.latehours;

import org.academia.latehours.crashdetector.CrashDetector;
import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.Food;
import org.academia.latehours.snake.Directions;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.LinkedList;

/**
 * Created by cadet on 06/10/15.
 */
public class Game implements KeyboardHandler{
    Map map;
    Snake snake;
    Food food;
    Keyboard k;
    CrashDetector crashDetector = new CrashDetector();
    Score score = new Score();
    boolean play = true;

    

    public void init() {
        setKeyboard();
        map = new Map(30, 30);
        snake = new Snake();
        food = new Food();
    }

    public void start() throws InterruptedException {
        init();
        score.currentScore = 0;

        Text currentScore = new Text(0, 0, "Score: " + score.currentScore);
        currentScore.setColor(Color.WHITE);
        currentScore.draw();

        Text currentHighscore = new Text (0, 10, "Highscore: " + score.getHighScore());
        currentHighscore.setColor(Color.WHITE);
        currentHighscore.draw();

        while (!snake.isDead()) {

            Thread.sleep(50);
            snake.move();
            snake.setEating(false);
            if (!food.isOnField()) {
                food.createFood();
            }
            if (crashDetector.checkEating(snake, food)) {
                snake.setEating(true);
                food.removeFood();
                score.currentScore += food.getFoodScore();
                currentScore.setText("Score: " + score.currentScore);
                if(score.currentScore > score.getHighScore()) {
                    score.setHighScore(score.currentScore);
                    currentHighscore.setText("Highscore: " + score.getHighScore());
                }
                System.out.println("Impact!");
            }
            if (crashDetector.selfDestruct(snake)) {
                snake.setDead(true);
                currentScore.delete();
                currentHighscore.delete();
                System.out.println("You have died!");
            }
        }

    }

    public void run() throws InterruptedException {
        while(true) {
            if(play) {
                start();
                gameOverScreen();
                play = false;
            }
            if(!play) {
                Thread.sleep(500);
                continue;
            }
        }
    }

    public void gameOverScreen() {
        Picture gameOver = new Picture(0, 0, "Snake/resources/gameover.jpg");
        int x = gameOver.getMaxX();
        int y = gameOver.getMaxY();
        gameOver.translate((Map.getCols() * Map.getCellSize() - x)/2, (Map.getRows() * Map.getCellSize() - y)/2);
        gameOver.draw();

        Text gameScore = new Text (0, 0, "You scored: " + score.currentScore + " points!");
        gameScore.setColor(Color.WHITE);
        gameScore.draw();

        Text gameSessionHighscore = new Text(0, 10, "The current highscore is: " + score.getHighScore() + " points!");
        gameSessionHighscore.setColor(Color.WHITE);
        gameSessionHighscore.draw();
    }

    private void setKeyboard() {
        k = new Keyboard(this);
        KeyboardEvent[] events = new KeyboardEvent[5];

        for (int i = 0; i < events.length; i++) {
            events[i] = new KeyboardEvent();
        }

        events[0].setKey(KeyboardEvent.KEY_UP);
        events[1].setKey(KeyboardEvent.KEY_DOWN);
        events[2].setKey(KeyboardEvent.KEY_LEFT);
        events[3].setKey(KeyboardEvent.KEY_RIGHT);
        events[4].setKey(KeyboardEvent.KEY_R);


        for (KeyboardEvent event : events) {
            event.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            k.addEventListener(event);
        }

    }


    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_UP:
                if (snake.getDirection() != Directions.DOWN) {
                    snake.setDirection(Directions.UP);
                }
                break;

            case KeyboardEvent.KEY_DOWN:
                if (snake.getDirection() != Directions.UP) {
                    snake.setDirection(Directions.DOWN);
                }
                break;

            case KeyboardEvent.KEY_LEFT:
                if (snake.getDirection() != Directions.RIGHT) {
                    snake.setDirection(Directions.LEFT);
                }
                break;

            case KeyboardEvent.KEY_RIGHT:
                if (snake.getDirection() != Directions.LEFT) {
                    snake.setDirection(Directions.RIGHT);
                }
                break;

            case KeyboardEvent.KEY_R:
                play = true;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }
}
