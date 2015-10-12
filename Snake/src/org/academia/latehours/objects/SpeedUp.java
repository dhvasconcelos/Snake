package org.academia.latehours.objects;

import org.academia.latehours.Game;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.graphics.Color;

/**
 * Created by cadet on 09/10/15.
 */
public class SpeedUp extends Food implements PowerUp {

    public SpeedUp() {
        super();
        setFoodScore(50);
        setColor(Color.RED);
    }

    @Override
    public void powerup() {
        Game.setSpeed(50);
        Snake.setSpeedUp(true);
    }

}
