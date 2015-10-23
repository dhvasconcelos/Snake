package org.academia.latehours.crashdetector;

import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.Food;
import org.academia.latehours.objects.Wall;
import org.academia.latehours.position.Position;
import org.academia.latehours.snake.Snake;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by cadet on 07/10/15.
 */
public class CrashDetector {


    public boolean checkEating(Snake snake, Food food) {

        return snake.headPosition().equals(food.getPosition());

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

    public boolean isWallHit(Snake snake) {
        ArrayList<Wall> walls = Map.getWalls();
        boolean wallHit = false;

        for (int i = 0; i < walls.size(); i++) {
            if (snake.headPosition().equals(walls.get(i).getPosition())) {
                wallHit = true;
                break;
            }
        }

        return wallHit;
    }

    public boolean isOccupied(Position position, Snake snake) {
        LinkedList list = snake.getSnakeBody();
        ArrayList<Wall> walls = Map.getWalls();
        boolean isOccupied = false;

        for (int i = 0; i < list.size(); i++) {
            if (position.equals(snake.bodyPartPosition(i))) {
                isOccupied = true;
                break;
            }
        }

        for (int j = 0; j < walls.size(); j++) {
            if (position.equals(walls.get(j).getPosition())) {
                isOccupied = true;
                break;
            }
        }

        return isOccupied;
    }


}
