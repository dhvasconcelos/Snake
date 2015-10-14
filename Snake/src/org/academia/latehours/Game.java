package org.academia.latehours;

import org.academia.latehours.crashdetector.CrashDetector;
import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.*;
import org.academia.latehours.position.Position;
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

import javax.swing.*;
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
    private Score score;
    private static int level = 0;
    private boolean play = true;
    private boolean start = false;
    private boolean pause = false;
    private Text currentScore;
    private Text currentHighscore;
    private Text currentLevel;
    private Text starting;
    private Text instructions;
    private Text levelSelected;
    private static Text selfCrossLeft;
    private static Text wallCrossLeft;
    private static int currentGameDelay;
    private static Timer gameTimer;
    private static Timer keyboardTimer;
    private MovementQueue movement;
    private Color textsColor = Color.RED;



    public void initialScreen() {
        setKeyboard();
        Rectangle screen = new Rectangle(0, 0,
                Map.getCols() * Map.getCellSize(),
                Map.getRows() * Map.getCellSize());
        screen.setColor(Color.BLACK);
        screen.fill();

        starting = new Text(Map.getCols()/2 * Map.getCellSize(), 17 * Map.getCellSize(), "SNAKE");
        starting.translate(-starting.getWidth() / 2, 0);
        starting.setColor(Color.GREEN);
        starting.draw();

        instructions = new Text(Map.getCols()/2 * Map.getCellSize(), 19 * Map.getCellSize(), "Choose the level with 0-9. Press Space to start.");
        instructions.translate(-instructions.getWidth() / 2, 0);
        instructions.setColor(Color.GREEN);
        instructions.draw();

        levelSelected = new Text(Map.getCols()/2 * Map.getCellSize(), 21 * Map.getCellSize(), "Level 0");
        levelSelected.translate(-levelSelected.getWidth() / 2, 0);
        levelSelected.setColor(Color.GREEN);
        levelSelected.draw();
    }

    public void init() {
        //if(k == null) {
        //    setKeyboard();
        //}
        starting.delete();
        instructions.delete();
        levelSelected.delete();
        currentGameDelay = 75;
        score = new Score();
        map = new Map();
        snake = new Snake();
        food = createFood();
        movement = new MovementQueue();
    }


    public void start() {
        init();
        score.setCurrentScore(0);
        score.loadHighScore();

        currentScore = new Text(0, 0, "SCORE: " + score.getCurrentScore());
        currentScore.setColor(textsColor);
        currentScore.draw();

        currentHighscore = new Text(100, 0, "HIGHSCORE: " + score.getHighScore());
        currentHighscore.setColor(textsColor);
        currentHighscore.draw();

        currentLevel = new Text(Map.getCols()/2 * Map.getCellSize(), 0, "LEVEL " + level);
        currentLevel.translate(-currentLevel.getWidth() / 2, 0);
        currentLevel.setColor(Color.RED);
        currentLevel.draw();

        selfCrossLeft = new Text(520, 0, "SNAKECROSS: " + Snake.getSelfCross());
        selfCrossLeft.setColor(textsColor);
        selfCrossLeft.draw();

        wallCrossLeft = new Text(400, 0, "WALLCROSS: " + Snake.getWallCross());
        wallCrossLeft.setColor(textsColor);
        wallCrossLeft.draw();
    }

    private void moveTimer() {
        ActionListener actionListener = e -> {
            snake.move();
            snake.setEating(false);

            foodPlacer();
            eatingCheck();
            selfCrashCheck();
            wallCrashCheck();

            if(snake.isDead()) {
                gameOverScreen();
                play = false;
                gameTimer.stop();
            }

        };
        gameTimer = new Timer(currentGameDelay, actionListener);
        gameTimer.start();
    }

    private void keyboardTimer() {
        ActionListener actionListener = e -> {
            if (movement.size() != 0) {
                movement.movementTranslator(movement.poll(), snake);
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
        currentLevel.delete();
        selfCrossLeft.delete();
        wallCrossLeft.delete();
        score.saveHighScore();

        Picture gameOver = new Picture(0, 0, "Snake/resources/gameover.jpg");
        int x = gameOver.getMaxX();
        int y = gameOver.getMaxY();
        gameOver.translate((Map.getCols() * Map.getCellSize() - x) / 2, (Map.getRows() * Map.getCellSize() - y) / 2);
        gameOver.draw();

        Text gameScore = new Text(0, 0, "YOU SCORED: " + score.getCurrentScore() + " POINTS!");
        gameScore.setColor(textsColor);
        gameScore.draw();

        Text gameSessionHighscore = new Text(0, 25, "THE CURRENT LEVEL HIGHSCORE FOR LEVEL " + level + " IS: " + score.getHighScore() + " POINTS!");
        gameSessionHighscore.setColor(textsColor);
        gameSessionHighscore.draw();
    }


    public Food createFood() {
        double probability = Math.random();
        if (probability < 0.9) {
            return Math.random() > 0.95 ? new RareFood() : new NormalFood();
        } else if (probability < 0.98){
            return Math.random() > 0.3 ? new SpeedUp() : new CrossWall();
        } else {
            return new CrossSnake();
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

    private void wallCrashCheck() {
        if (crashDetector.isWallHit(snake)) {
            if (Snake.getWallCross() <= 0 && !Snake.isSpeedUp()) {
                snake.setDead(true);
                System.out.println("You have died!");

            } else if(Snake.isSpeedUp()) {
                map.wallDelete(snake.headPosition());
                setSpeed(currentGameDelay + 25);
                Snake.setSpeedUp(false);

            } else {
                Snake.setWallCross(Snake.getWallCross() - 1);
                updateWallCrossText();
            }
        }
    }

    public static int getLevel() {
        return level;
    }

    public static void setSpeed(int delay) {
        Game.currentGameDelay = delay;
        Game.gameTimer.setDelay(delay);
        Game.keyboardTimer.setDelay(delay);
    }

    public static void updateSelfCrossText() {
        selfCrossLeft.setText("SELFCROSS: " + Snake.getSelfCross());
    }

    public static void updateWallCrossText() {
        wallCrossLeft.setText("WALLCROSS: " + Snake.getWallCross());
    }


    private void setKeyboard() {
        k = new Keyboard(this);
        KeyboardEvent[] events = new KeyboardEvent[17];

        for (int i = 0; i < events.length; i++) {
            events[i] = new KeyboardEvent();
        }

        events[0].setKey(KeyboardEvent.KEY_UP);
        events[1].setKey(KeyboardEvent.KEY_DOWN);
        events[2].setKey(KeyboardEvent.KEY_LEFT);
        events[3].setKey(KeyboardEvent.KEY_RIGHT);
        events[4].setKey(KeyboardEvent.KEY_R);
        events[5].setKey(KeyboardEvent.KEY_P);
        events[6].setKey(KeyboardEvent.KEY_SPACE);
        events[7].setKey(KeyboardEvent.KEY_1);
        events[8].setKey(KeyboardEvent.KEY_2);
        events[9].setKey(KeyboardEvent.KEY_3);
        events[10].setKey(KeyboardEvent.KEY_4);
        events[11].setKey(KeyboardEvent.KEY_5);
        events[12].setKey(KeyboardEvent.KEY_6);
        events[13].setKey(KeyboardEvent.KEY_7);
        events[14].setKey(KeyboardEvent.KEY_8);
        events[15].setKey(KeyboardEvent.KEY_9);
        events[16].setKey(KeyboardEvent.KEY_0);

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

            case KeyboardEvent.KEY_SPACE:
                try {
                    if(!start) {
                        run();
                        start = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case KeyboardEvent.KEY_0:
                level = 0;
                levelSelected.setText("Level 0");
                break;

            case KeyboardEvent.KEY_1:
                level = 1;
                levelSelected.setText("Level 1");
                break;

            case KeyboardEvent.KEY_2:
                level = 2;
                levelSelected.setText("Level 2");
                break;

            case KeyboardEvent.KEY_3:
                level = 3;
                levelSelected.setText("Level 3");
                break;

            case KeyboardEvent.KEY_4:
                level = 4;
                levelSelected.setText("Level 4");
                break;

            case KeyboardEvent.KEY_5:
                level = 5;
                levelSelected.setText("Level 5");
                break;

            case KeyboardEvent.KEY_6:
                level = 6;
                levelSelected.setText("Level 6");
                break;

            case KeyboardEvent.KEY_7:
                level = 7;
                levelSelected.setText("Level 7");
                break;

            case KeyboardEvent.KEY_8:
                level = 8;
                levelSelected.setText("Level 8");
                break;

            case KeyboardEvent.KEY_9:
                level = 9;
                levelSelected.setText("Level 9");
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

}
