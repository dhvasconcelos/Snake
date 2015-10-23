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
    private Picture screen;
    private Picture gameOverPicture;
    private Picture levelPicture;
    private Picture pausePicture;
    private boolean play = true;
    private boolean start = false;
    private boolean pause = false;
    private boolean fromInitialScreen;
    private boolean inInitialScreen;
    private boolean snakeStarted;
    private static String gameDifficulty = "Easy";
    private Text currentScore;
    private Text currentHighscore;
    private Text currentLevel;
    private Text starting;
    private Text inicialInstructions;
    private Text levelSelected;
    private Text difficultyLevelSelected;
    private Text pauseInstructions;
    private static Text selfCrossLeft;
    private static Text wallCrossLeft;
    private Text gameScore;
    private Text levelHighscore;
    private static int currentGameDelay;
    private static int initialGameDelay = 100;
    private static Timer gameTimer;
    private static Timer keyboardTimer;
    private MovementQueue movement;
    private Color textsColor = Color.RED;


    public void initialScreen() {
        if (k == null) {
            setKeyboard();
        }

        snakeStarted = true;
        fromInitialScreen = true;
        inInitialScreen = true;
        screen = new Picture(0, 0, "Snake/resources/hardcore-snake.png");
        screen.draw();

        starting = new Text(Map.getCols() / 2 * Map.getCellSize() + 20, 23 * Map.getCellSize(), "PRESS SPACE TO FOR LEVEL SELECTION");
        starting.translate(-starting.getWidth() / 2, 0);
        starting.setColor(Color.GREEN);
        starting.draw();

    }

    public void levelSelectionScreen() {
        snakeStarted = false;
        level = 0;

        inicialInstructions = new Text(Map.getCols() / 2 * Map.getCellSize() + 20, 19 * Map.getCellSize(), "Choose the level with 0-9. Choose difficulty with E, M or H.");
        inicialInstructions.translate(-inicialInstructions.getWidth() / 2, 0);
        inicialInstructions.setColor(Color.GREEN);
        inicialInstructions.draw();

        levelSelected = new Text(Map.getCols() / 2 * Map.getCellSize() + 50, 23 * Map.getCellSize(), "Level " + level + ": Classic");
        levelSelected.translate(-levelSelected.getWidth() / 2, 0);
        levelSelected.setColor(Color.GREEN);
        levelSelected.draw();

        difficultyLevelSelected = new Text(Map.getCols() / 2 * Map.getCellSize() + 50, 25 * Map.getCellSize(), "Difficulty: " + gameDifficulty);
        difficultyLevelSelected.translate(-difficultyLevelSelected.getWidth() / 2, 0);
        difficultyLevelSelected.setColor(Color.GREEN);
        difficultyLevelSelected.draw();

        levelPicture = new Picture(5 * Map.getCellSize(), 21 * Map.getCellSize(), "Snake/resources/Level" + level + ".png");
        levelPicture.draw();
    }

    public void deleteInitialScreen() {
        inicialInstructions.delete();
        levelSelected.delete();
        difficultyLevelSelected.delete();
        screen.delete();
        levelPicture.delete();
    }

    public void init() {
        //if(k == null) {
        //    setKeyboard();
        //}
        inInitialScreen = false;
        if (fromInitialScreen) {
            deleteInitialScreen();
            fromInitialScreen = false;
        }
        currentGameDelay = initialGameDelay;
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

        currentLevel = new Text(Map.getCols() / 2 * Map.getCellSize(), 0, "LEVEL " + level);
        currentLevel.translate(-currentLevel.getWidth() / 2, 0);
        currentLevel.setColor(Color.RED);
        currentLevel.draw();

        selfCrossLeft = new Text(520, 0, "SNAKECROSS: " + Snake.getSelfCross());
        selfCrossLeft.setColor(textsColor);
        selfCrossLeft.draw();

        wallCrossLeft = new Text(400, 0, "WALLCROSS: " + Snake.getWallCross());
        wallCrossLeft.setColor(textsColor);
        wallCrossLeft.draw();

        pauseInstructions = new Text(0, 600, "PRESS P TO PAUSE");
        pauseInstructions.setColor(textsColor);
        pauseInstructions.draw();
    }

    private void moveTimer() {
        ActionListener actionListener = e -> {
            snake.move();
            snake.setEating(false);

            foodPlacer();
            eatingCheck();
            selfCrashCheck();
            wallCrashCheck();

            if (snake.isDead()) {
                gameOver();
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

    public void run() {
        start();
        moveTimer();
        keyboardTimer();
    }

    public void gameOver() {
        currentScore.delete();
        currentHighscore.delete();
        currentLevel.delete();
        selfCrossLeft.delete();
        wallCrossLeft.delete();
        gameTimer.stop();
        keyboardTimer.stop();
        score.saveHighScore();
        movement.deleteMovements();
        pauseInstructions.delete();
        play = false;
        gameOverScreen();
    }

    public void gameOverScreen() {

        gameOverPicture = new Picture(0, 0, "Snake/resources/gameover.png");
        int x = gameOverPicture.getMaxX();
        int y = gameOverPicture.getMaxY();
        gameOverPicture.translate((Map.getCols() * Map.getCellSize() - x) / 2, (Map.getRows() * Map.getCellSize() - y) / 2);
        gameOverPicture.draw();

        gameScore = new Text(Map.getCols() / 2 * Map.getCellSize(), 0, "YOU SCORED: " + score.getCurrentScore() + " POINTS!");
        gameScore.translate(-gameScore.getWidth() / 2, 0);
        gameScore.setColor(textsColor);
        gameScore.draw();

        levelHighscore = new Text(Map.getCols() / 2 * Map.getCellSize(), 25, "THE HIGHSCORE FOR LEVEL " + level + " IS: " + score.getHighScore() + " POINTS!");
        levelHighscore.translate(-levelHighscore.getWidth() / 2, 0);
        levelHighscore.setColor(textsColor);
        levelHighscore.draw();

    }

    public void deleteGameOverScreen() {
        gameOverPicture.delete();
        gameScore.delete();
        levelHighscore.delete();
    }


    public Food createFood() {
        double probability = Math.random();
        if (probability < 0.9) {
            return Math.random() > 0.95 ? new RareFood() : new NormalFood();
        } else if (probability < 0.98) {
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
                if (currentGameDelay < initialGameDelay) {
                    setSpeed(initialGameDelay);
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

            } else if (Snake.isSpeedUp()) {
                map.wallDelete(snake.headPosition());
                setSpeed(initialGameDelay);
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

    public static int getInitialGameDelay() {
        return initialGameDelay;
    }

    public static String getGameDifficulty() {
        return gameDifficulty;
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
        KeyboardEvent[] events = new KeyboardEvent[21];

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
        events[17].setKey(KeyboardEvent.KEY_M);
        events[18].setKey(KeyboardEvent.KEY_H);
        events[19].setKey(KeyboardEvent.KEY_E);
        events[20].setKey(KeyboardEvent.KEY_Q);

        for (KeyboardEvent event : events) {
            event.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            k.addEventListener(event);
        }
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_UP:
                if(!snakeStarted) {
                    if (snake.getDirection() != Directions.DOWN && play) {
                        movement.add(Directions.UP);
                    }
                }
                break;

            case KeyboardEvent.KEY_DOWN:
                if(!snakeStarted) {
                    if (snake.getDirection() != Directions.UP && play) {
                        movement.add(Directions.DOWN);
                    }
                }
                break;

            case KeyboardEvent.KEY_LEFT:
                if(!snakeStarted) {
                    if (snake.getDirection() != Directions.RIGHT && play) {
                        movement.add(Directions.LEFT);
                    }
                }
                break;

            case KeyboardEvent.KEY_RIGHT:
                if(!snakeStarted) {
                    if (snake.getDirection() != Directions.LEFT && play) {
                        movement.add(Directions.RIGHT);
                    }
                }
                break;

            case KeyboardEvent.KEY_R:
                if (!play) {
                    play = true;
                    map.mapDelete();
                    deleteGameOverScreen();
                    run();
                }
                break;

            case KeyboardEvent.KEY_P:
                if (play && !snakeStarted) {
                    if (!pause) {
                        keyboardTimer.stop();
                        gameTimer.stop();
                        pausePicture = new Picture(0, 0, "Snake/resources/paused.png");
                        pausePicture.draw();
                        pause = true;
                    } else {
                        keyboardTimer.start();
                        gameTimer.start();
                        setSpeed(currentGameDelay);
                        pausePicture.delete();
                        pause = false;
                    }
                }
                break;

            case KeyboardEvent.KEY_SPACE:
                if (snakeStarted) {
                    starting.delete();
                    levelSelectionScreen();

                } else if (!start && !snakeStarted) {
                    start = true;
                    run();
                }

                if (pause) {
                    gameOver();
                    pause = false;
                    start = false;
                    play = true;
                    map.mapDelete();
                    deleteGameOverScreen();
                    initialScreen();
                }

                if (start && !play && !snakeStarted) {
                    start = false;
                    play = true;
                    map.mapDelete();
                    deleteGameOverScreen();
                    initialScreen();
                }
                break;

            case KeyboardEvent.KEY_0:
                if(inInitialScreen && !snakeStarted) {
                    level = 0;
                    levelSelected.setText("Level " + level + ": Classic");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_1:
                if(inInitialScreen && !snakeStarted) {
                    level = 1;
                    levelSelected.setText("Level " + level + ": Classic walls");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_2:
                if(inInitialScreen && !snakeStarted) {
                    level = 2;
                    levelSelected.setText("Level " + level + ": Target Acquired");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_3:
                if(inInitialScreen && !snakeStarted) {
                    level = 3;
                    levelSelected.setText("Level " + level + ": Squared");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_4:
                if(inInitialScreen && !snakeStarted) {
                    level = 4;
                    levelSelected.setText("Level " + level + ": Pointers");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_5:
                if(inInitialScreen && !snakeStarted) {
                    level = 5;
                    levelSelected.setText("Level " + level + ": ZigZag");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_6:
                if(inInitialScreen && !snakeStarted) {
                    level = 6;
                    levelSelected.setText("Level " + level + ": Mindfuck");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_7:
                if(inInitialScreen && !snakeStarted) {
                    level = 7;
                    levelSelected.setText("Level " + level + ": X Marks the Spot");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_8:
                if(inInitialScreen && !snakeStarted) {
                    level = 8;
                    levelSelected.setText("Level " + level + ": Starry Night");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_9:
                if(inInitialScreen && !snakeStarted) {
                    level = 9;
                    levelSelected.setText("Level " + level + ": Equals");
                    levelPicture.load("Snake/resources/Level" + level + ".png");
                }
                break;

            case KeyboardEvent.KEY_E:
                if(inInitialScreen && !snakeStarted) {
                    initialGameDelay = 100;
                    difficultyLevelSelected.setText("Difficulty: Easy");
                    gameDifficulty = "Easy";
                }
                break;

            case KeyboardEvent.KEY_M:
                if(inInitialScreen && !snakeStarted) {
                    initialGameDelay = 75;
                    difficultyLevelSelected.setText("Difficulty: Medium");
                    gameDifficulty = "Medium";
                }
                break;

            case KeyboardEvent.KEY_H:
                if(inInitialScreen && !snakeStarted) {
                    initialGameDelay = 50;
                    difficultyLevelSelected.setText("Difficulty: Hardcore");
                    gameDifficulty = "Hardcore";
                }
                break;

            case KeyboardEvent.KEY_Q:
                System.exit(1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
    }

}
