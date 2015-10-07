package org.academia.latehours;

import org.academia.latehours.maps.Map;
import org.academia.latehours.objects.Food;
import org.academia.latehours.snake.Directions;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

import java.util.LinkedList;

/**
 * Created by cadet on 06/10/15.
 */
public class Game implements KeyboardHandler{
    Map map;
    Snake snake;
    Food food;
    Keyboard k;
    

    public void init() {
        setKeyboard();
        map = new Map(50, 20);
        snake = new Snake();
        food = new Food();
    }

    public void start() throws InterruptedException {

        while(true){

            if(!food.isOnField()) {
                food.createFood();
            }
            Thread.sleep(50);
            snake.move();
            if(!food.isOnField()) {
                food.createFood();
            }

        }
    }

    private void setKeyboard() {
        k = new Keyboard(this);
        KeyboardEvent[] events = new KeyboardEvent[4];

        for (int i = 0; i < events.length; i++) {
            events[i] = new KeyboardEvent();
        }

        events[0].setKey(KeyboardEvent.KEY_UP);
        events[1].setKey(KeyboardEvent.KEY_DOWN);
        events[2].setKey(KeyboardEvent.KEY_LEFT);
        events[3].setKey(KeyboardEvent.KEY_RIGHT);


        for (KeyboardEvent event : events) {
            event.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            k.addEventListener(event);
        }

    }


    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_UP:
                if (snake.getDirection() != Directions.DOWN) {
                    snake.setDirection(Directions.UP);
                }
                break;

            case KeyboardEvent.KEY_DOWN:
                if (snake.getDirection() != Directions.UP) {
                    snake.setDirection(Directions.DOWN);
                }
                break;

            case KeyboardEvent.KEY_LEFT:
                if (snake.getDirection() != Directions.RIGHT) {
                    snake.setDirection(Directions.LEFT);
                }
                break;

            case KeyboardEvent.KEY_RIGHT:
                if (snake.getDirection() != Directions.LEFT) {
                    snake.setDirection(Directions.RIGHT);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }
}
