package org.academia.latehours;

import org.academia.latehours.snake.Directions;
import org.academia.latehours.snake.Snake;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by cadet on 12/10/15.
 */
public class MovementQueue {
    private Queue<Directions> movements = new PriorityQueue<>();

    public void add(Directions direction) {
        movements.add(direction);
    }

    public void movementTranslator(Directions direction, Snake snake) {
        switch (direction) {
            case UP:
                if (snake.getDirection() != Directions.DOWN) {
                    snake.setDirection(Directions.UP);
                }
                break;

            case DOWN:
                if (snake.getDirection() != Directions.UP) {
                    snake.setDirection(Directions.DOWN);
                }
                break;

            case LEFT:
                if (snake.getDirection() != Directions.RIGHT) {
                    snake.setDirection(Directions.LEFT);
                }
                break;

            case RIGHT:
                if (snake.getDirection() != Directions.LEFT) {
                    snake.setDirection(Directions.RIGHT);
                }
                break;

            default:
                break;
        }
    }

    public Directions poll() {
        return movements.poll();
    }

    public int size() {
        return movements.size();
    }

    public void deleteMovements() {
        movements.clear();
    }
}

