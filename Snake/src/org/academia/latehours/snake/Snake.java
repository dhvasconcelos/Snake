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

    LinkedList<BodyParts> snakeBody;
    Directions direction = Directions.UP;
    boolean eating;

    public Snake() {
        snakeBody = new LinkedList<>();
        BodyParts snakeHead = new BodyParts(Map.getCols()/2, Map.getRows()/2);

        for (int i = 3; i > 0; i--) {
            BodyParts bodyPart = new BodyParts(snakeHead.position.getCol() + i, snakeHead.position.getRow());
            System.out.println("For bodyPart " + bodyPart.position);
            snakeBody.add(bodyPart);
        }

        snakeBody.add(snakeHead);

    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public void setEating(boolean eating) {
        this.eating = eating;
    }

    public void move() {
        addNewBodyPart();
    }

    private void removeBodyPart() {

        snakeBody.get(0).bodyDrawing.delete();
        snakeBody.removeFirstOccurrence(snakeBody.get(0));

    }

    private void addNewBodyPart() {
        if (!eating) {
            removeBodyPart();
        }

        BodyParts bodyPart;

        switch (direction) {

            case LEFT:
                bodyPart = new BodyParts((Map.getCols() + headPosition().getCol() - 1) % Map.getCols(), headPosition().getRow());
                break;

            case RIGHT:
                bodyPart = new BodyParts((headPosition().getCol() + 1) % Map.getCols(), headPosition().getRow());
                break;

            case UP:
                bodyPart = new BodyParts(headPosition().getCol(), (Map.getRows() + headPosition().getRow() - 1) % Map.getRows());
                break;

            case DOWN:
                bodyPart = new BodyParts(headPosition().getCol(), (headPosition().getRow() + 1) % Map.getRows());
                break;

            default:
                bodyPart = new BodyParts(headPosition().getCol() - 1,headPosition().getRow());
                break;
        }

        System.out.println(bodyPart.position);
        snakeBody.add(bodyPart);
    }


    static class BodyParts {
        Position position;
        Rectangle bodyDrawing;

        public BodyParts(int col, int row) {
            this.position = new Position (col,row);
            bodyDrawing = new Rectangle(position.getCol() * Map.getCellSize(), position.getRow() * Map.getCellSize(), Map.getCellSize(), Map.getCellSize());
            bodyDrawing.setColor(Color.GREEN);
            bodyDrawing.fill();
        }
    }

    public Position headPosition(){
        int size = snakeBody.size() - 1;
        return new Position(snakeBody.get(size).position.getCol(),snakeBody.get(size).position.getRow());
    }






}
