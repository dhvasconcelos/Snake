package org.academia.latehours.objects;

import org.academia.latehours.Game;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.graphics.Color;

/**
 * Created by cadet on 13/10/15.
 */
public class CrossWall extends Food implements PowerUp {

    public CrossWall() {
        super();
        setFoodScore(50);
        setColor(Color.GRAY);
    }


    @Override
    public void powerup() {
        Snake.setWallCross(Snake.getWallCross() + 1);
        Game.updateWallCrossText();
    }
}
