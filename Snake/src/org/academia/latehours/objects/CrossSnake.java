package org.academia.latehours.objects;

import org.academia.latehours.Game;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.graphics.Color;

/**
 * Created by cadet on 09/10/15.
 */

public class CrossSnake extends Food implements PowerUp {

    public CrossSnake() {
        super();
        setFoodScore(50);
        setColor(Color.CYAN);
    }


    @Override
    public void powerup() {
        Snake.setSelfCross(Snake.getSelfCross() + 1);
        Game.updateSelfCrossText();
    }
}
