package org.academia.latehours;

import org.academia.latehours.crashdetector.CrashDetector;
import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.Food;
import org.academia.latehours.objects.NormalFood;
import org.academia.latehours.objects.SpecialFood;
import org.academia.latehours.position.Position;
import org.academia.latehours.snake.Directions;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;

/**
 * Created by cadet on 06/10/15.
 */

public class Game implements KeyboardHandler {

    private Map map;
    private Snake snake;
    private Food food;
    private Keyboard k;
    private CrashDetector crashDetector = new CrashDetector();
    private Score score = new Score();
    private boolean play = true;
    private Text currentScore;
    private Text currentHighscore;


    public void init() {
        setKeyboard();
        map = new Map(30, 30);
        snake = new Snake();
        food = createFood();
    }


    public void start() {
        init();
        score.setCurrentScore(0);

        currentScore = new Text(0, 0, "SCORE: " + score.getCurrentScore());
        currentScore.setColor(Color.GREEN);
        currentScore.draw();

        currentHighscore = new Text(100, 0, "HIGHSCORE: " + score.getHighScore());
        currentHighscore.setColor(Color.GREEN);
        currentHighscore.draw();
    }


    public void gameLoop() throws InterruptedException {

        while (!snake.isDead()) {

            Thread.sleep(50);
            snake.move();
            snake.setEating(false);

            if (!food.isOnField()) {
                Position position = new Position();
                while (crashDetector.isOccupied(position, snake)) {
                    position = new Position();
                }
                food = createFood();
                food.placeFood(position);
            }

            if (crashDetector.checkEating(snake, food)) {
                snake.setEating(true);
                food.removeFood();
                score.setCurrentScore(score.getCurrentScore() + food.getFoodScore());
                currentScore.setText("Score: " + score.getCurrentScore());
                if (score.getCurrentScore() > score.getHighScore()) {
                    score.setHighScore(score.getCurrentScore());
                    currentHighscore.setText("Highscore: " + score.getHighScore());
                }
                //System.out.println("Impact!");
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
        while (true) {
            if (play) {
                start();
                gameLoop();
                gameOverScreen();
                play = false;
            }
            if (!play) {
                Thread.sleep(500);
                continue;
            }
        }
    }


    public void gameOverScreen() {
        Picture gameOver = new Picture(0, 0, "Snake/resources/gameover.jpg");
        int x = gameOver.getMaxX();
        int y = gameOver.getMaxY();
        gameOver.translate((Map.getCols() * Map.getCellSize() - x) / 2, (Map.getRows() * Map.getCellSize() - y) / 2);
        gameOver.draw();

        Text gameScore = new Text(0, 0, "You scored: " + score.getCurrentScore() + " points!");
        gameScore.setColor(Color.WHITE);
        gameScore.draw();

        Text gameSessionHighscore = new Text(0, 10, "The current highscore is: " + score.getHighScore() + " points!");
        gameSessionHighscore.setColor(Color.WHITE);
        gameSessionHighscore.draw();
    }


    public Food createFood() {
         return Math.random() > 0.94 ? new SpecialFood() : new NormalFood();
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
