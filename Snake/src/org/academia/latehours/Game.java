package org.academia.latehours;

import org.academia.latehours.crashdetector.CrashDetector;
import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.*;
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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private boolean pause = false;
    private Text currentScore;
    private Text currentHighscore;
    private static Text selfCrossLeft;
    private static int currentGameDelay = 75;
    private static Timer gameTimer;
    private static Timer keyboardTimer;
    private MovementQueue movement;


    public void init() {
        if(k == null) {
            setKeyboard();
        }
        map = new Map();
        snake = new Snake();
        food = createFood();
        movement = new MovementQueue();
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

        selfCrossLeft = new Text(500, 0, "SNAKECROSS: " + Snake.getSelfCross());
        selfCrossLeft.setColor(Color.GREEN);
        selfCrossLeft.draw();
    }

    private void moveTimer() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snake.move();
                snake.setEating(false);

                foodPlacer();
                eatingCheck();
                selfCrashCheck();

                if(snake.isDead()) {
                    gameOverScreen();
                    play = false;
                    gameTimer.stop();
                }

            }
        };
        gameTimer = new Timer(currentGameDelay, actionListener);
        gameTimer.start();
    }

    private void keyboardTimer() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(movement.size() != 0) {
                    movement.movementTranslator(movement.poll(), snake);
                }
            }
        };
        keyboardTimer = new Timer(currentGameDelay, actionListener);
        keyboardTimer.start();
    }


    public void run() throws InterruptedException {
        start();
        moveTimer();
        keyboardTimer();
    }


    public void gameOverScreen() {
        currentScore.delete();
        currentHighscore.delete();
        selfCrossLeft.delete();

        Picture gameOver = new Picture(0, 0, "Snake/resources/gameover.jpg");
        int x = gameOver.getMaxX();
        int y = gameOver.getMaxY();
        gameOver.translate((Map.getCols() * Map.getCellSize() - x) / 2, (Map.getRows() * Map.getCellSize() - y) / 2);
        gameOver.draw();

        Text gameScore = new Text(0, 0, "YOU SCORED: " + score.getCurrentScore() + " POINTS!");
        gameScore.setColor(Color.GREEN);
        gameScore.draw();

        Text gameSessionHighscore = new Text(0, 25, "THE CURRENT HIGHSCORE IS: " + score.getHighScore() + " POINTS!");
        gameSessionHighscore.setColor(Color.GREEN);
        gameSessionHighscore.draw();
    }


    public Food createFood() {
        if (Math.random() < 0.9) {
            return Math.random() > 0.95 ? new RareFood() : new NormalFood();
        } else {
            return Math.random() > 0.3 ? new SpeedUp() : new CrossSnake();
        }
    }


    private void foodPlacer() {
        if (!food.isOnField()) {
            Position position = new Position();
            while (crashDetector.isOccupied(position, snake)) {
                position = new Position();
            }
            food = createFood();
            food.placeFood(position);
        }
    }


    private void eatingCheck() {
        if (crashDetector.checkEating(snake, food)) {
            snake.setEating(true);
            food.removeFood();
            score.setCurrentScore(score.getCurrentScore() + food.getFoodScore());
            currentScore.setText("SCORE: " + score.getCurrentScore());

            if (food instanceof PowerUp) {
                ((PowerUp) food).powerup();
            } else {
                if (currentGameDelay < 75) {
                    setSpeed(75);
                    Snake.setSpeedUp(false);
                }
            }

            if (score.getCurrentScore() > score.getHighScore()) {
                score.setHighScore(score.getCurrentScore());
                currentHighscore.setText("HIGHSCORE: " + score.getHighScore());
            }
        }
    }


    private void selfCrashCheck() {
        if (crashDetector.selfDestruct(snake)) {
            if (Snake.getSelfCross() <= 0) {
                snake.setDead(true);
                System.out.println("You have died!");
            } else {
                Snake.setSelfCross(Snake.getSelfCross() - 1);
                updateSelfCrossText();
            }
        }
    }

    public static void setSpeed(int delay) {
        Game.currentGameDelay = delay;
        Game.gameTimer.setDelay(delay);
        Game.keyboardTimer.setDelay(delay);
    }

    public static void updateSelfCrossText() {
        selfCrossLeft.setText("SELFCROSS: " + Snake.getSelfCross());
    }

    private void setKeyboard() {
        k = new Keyboard(this);
        KeyboardEvent[] events = new KeyboardEvent[6];

        for (int i = 0; i < events.length; i++) {
            events[i] = new KeyboardEvent();
        }

        events[0].setKey(KeyboardEvent.KEY_UP);
        events[1].setKey(KeyboardEvent.KEY_DOWN);
        events[2].setKey(KeyboardEvent.KEY_LEFT);
        events[3].setKey(KeyboardEvent.KEY_RIGHT);
        events[4].setKey(KeyboardEvent.KEY_R);
        events[5].setKey(KeyboardEvent.KEY_P);

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
                    MovementQueue.add(Directions.UP);
                }
                break;

            case KeyboardEvent.KEY_DOWN:
                if (snake.getDirection() != Directions.UP) {
                    MovementQueue.add(Directions.DOWN);
                }
                break;

            case KeyboardEvent.KEY_LEFT:
                if (snake.getDirection() != Directions.RIGHT) {
                    MovementQueue.add(Directions.LEFT);
                }
                break;

            case KeyboardEvent.KEY_RIGHT:
                if (snake.getDirection() != Directions.LEFT) {
                    MovementQueue.add(Directions.RIGHT);
                }
                break;

            case KeyboardEvent.KEY_R:
                if(!play) {
                    start();
                    moveTimer();
                    play = true;
                }
                break;

            case KeyboardEvent.KEY_P:
                if(!pause) {
                    keyboardTimer.stop();
                    gameTimer.stop();
                    pause = true;
                } else {
                    keyboardTimer.start();
                    gameTimer.start();
                    setSpeed(currentGameDelay);
                    pause = false;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

}
