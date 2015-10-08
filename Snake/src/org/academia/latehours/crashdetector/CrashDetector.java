package org.academia.latehours.crashdetector;

import org.academia.latehours.objects.Food;
import org.academia.latehours.position.Position;
import org.academia.latehours.snake.Snake;

import java.util.LinkedList;

/**
 * Created by cadet on 07/10/15.
 */
public class CrashDetector {


    public boolean checkEating(Snake snake, Food food) {

        return snake.headPosition().equals(food.getPosition()) ? true : false;

    }

    public boolean selfDestruct(Snake snake) {
        LinkedList list = snake.getSnakeBody();
        boolean selfDestruct = false;

        for (int i = 0; i < list.size() - 1; i++) {
            if (snake.headPosition().equals(snake.bodyPartPosition(i))) {
                selfDestruct = true;
                break;
            }
        }

        return selfDestruct;
    }

    public boolean isOccupied(Position position, Snake snake) {
        LinkedList list = snake.getSnakeBody();
        boolean isOccupied = false;

        for (int i = 0; i < list.size() - 1; i++) {
            if (position.equals(snake.bodyPartPosition(i))) {
                isOccupied = true;
                break;
            }
        }

        return isOccupied;
    }
}
