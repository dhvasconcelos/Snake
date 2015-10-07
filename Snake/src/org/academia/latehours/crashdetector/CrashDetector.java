package org.academia.latehours.crashdetector;

import org.academia.latehours.objects.Food;
import org.academia.latehours.snake.Snake;

import java.util.LinkedList;

/**
 * Created by cadet on 07/10/15.
 */
public class CrashDetector {

    public CrashDetector(){

    }
    public boolean checkEating(Snake snake, Food food){

        return snake.headPosition().equals(food.getPosition()) ? true : false;

    }

    public boolean selfDestruct (Snake snake) {
        LinkedList list = snake.getSnakeBody();
        boolean destruct = false;

        for (int i = 0; i < list.size() - 1; i++) {
            if(snake.headPosition().equals(snake.bodyPartPosition(i))) {
                destruct = true;
            }
        }

        return destruct;
    }
}
