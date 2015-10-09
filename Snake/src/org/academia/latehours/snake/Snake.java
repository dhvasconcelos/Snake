package org.academia.latehours.snake;

import org.academia.latehours.maps.Map;
import org.academia.latehours.position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

import java.util.LinkedList;


/**
 * Created by cadet on 06/10/15.
 */

public class Snake {

    private LinkedList<BodyParts> snakeBody;
    private Directions direction = Directions.LEFT;
    private boolean eating;
    private boolean dead;
    private static boolean speedUp;
    private static int selfCross;


    public Snake() {
        snakeBody = new LinkedList<>();
        BodyParts snakeHead = new BodyParts(Map.getCols() / 2, Map.getRows() / 2);

        for (int i = 3; i > 0; i--) {
            BodyParts bodyPart = new BodyParts(snakeHead.position.getCol() + i,
                    snakeHead.position.getRow());

            //System.out.println("For bodyPart " + bodyPart.position);
            snakeBody.add(bodyPart);
        }

        snakeBody.add(snakeHead);

    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setEating(boolean eating) {
        this.eating = eating;
    }

    public boolean isEating() {
        return eating;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int snakeSize() {
        return snakeBody.size();
    }

    public void move() {
        if (dead) {
            return;
        }

        if (!eating) {
            removeBodyPart();
        }
        addNewBodyPart(direction);
    }

    private void removeBodyPart() {

        snakeBody.get(0).bodyDrawing.delete();
        snakeBody.removeFirst();

    }

    private void addNewBodyPart(Directions direction) {
        BodyParts bodyPart;

        switch (direction) {

            case LEFT:
                bodyPart = new BodyParts((Map.getCols() + headPosition().getCol() - 1) % Map.getCols(),
                        headPosition().getRow());
                break;

            case RIGHT:
                bodyPart = new BodyParts((headPosition().getCol() + 1) % Map.getCols(),
                        headPosition().getRow());
                break;

            case UP:
                bodyPart = new BodyParts(headPosition().getCol(),
                        (Map.getRows() + headPosition().getRow() - 1) % Map.getRows());
                break;

            case DOWN:
                bodyPart = new BodyParts(headPosition().getCol(),
                        (headPosition().getRow() + 1) % Map.getRows());
                break;

            default:
                bodyPart = new BodyParts(headPosition().getCol() - 1,
                        headPosition().getRow());
                break;
        }

        //System.out.println(bodyPart.position);
        snakeBody.add(bodyPart);
    }


    private class BodyParts {
        private Position position;
        private Rectangle bodyDrawing;

        public BodyParts(int col, int row) {
            this.position = new Position(col, row);
            bodyDrawing = new Rectangle(position.getCol() * Map.getCellSize(),
                    position.getRow() * Map.getCellSize(),
                    Map.getCellSize(),
                    Map.getCellSize());

            Color color = speedUp ? Color.RED : Color.GREEN;
            bodyDrawing.setColor(color);
            bodyDrawing.fill();
        }

    }

    public Position headPosition() {
        int size = snakeSize() - 1;
        return new Position(snakeBody.get(size).position.getCol(),
                snakeBody.get(size).position.getRow());
    }

    public Position bodyPartPosition(int index) {
        return new Position(snakeBody.get(index).position.getCol(),
                snakeBody.get(index).position.getRow());
    }

    public static int getSelfCross() {
        return selfCross;
    }

    public static void setSelfCross(int selfCross) {
        Snake.selfCross = selfCross;
    }

    public static boolean isSpeedUp() {
        return speedUp;
    }

    public static void setSpeedUp(boolean speedUp) {
        Snake.speedUp = speedUp;
    }

    public LinkedList<BodyParts> getSnakeBody() {
        return snakeBody;
    }


}
