package org.academia.latehours.crashdetector;

import org.academia.latehours.objects.Food;
import org.academia.latehours.snake.Snake;

/**
 * Created by cadet on 07/10/15.
 */
public class CrashDetector {

    public CrashDetector(){

    }
    public boolean collision(Snake snake, Food food){

        return snake.headPosition().equals(food.getPosition()) ? true : false;

    }
}
