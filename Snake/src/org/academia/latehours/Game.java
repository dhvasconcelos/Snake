package org.academia.latehours;

import org.academia.latehours.maps.Map;
import org.academia.latehours.snake.Directions;
import org.academia.latehours.snake.Snake;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

/**
 * Created by cadet on 06/10/15.
 */
public class Game implements KeyboardHandler{
    Map map;
    Snake snake;
    Keyboard k;

    public void init() {
        setKeyboard();
        map = new Map(50, 20);
        snake = new Snake();
    }

    public void start() throws InterruptedException {

        while(true){

            Thread.sleep(50);


            snake.move();


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
                snake.setDirection(Directions.UP);
                break;

            case KeyboardEvent.KEY_DOWN:
                snake.setDirection(Directions.DOWN);
                break;

            case KeyboardEvent.KEY_LEFT:
                snake.setDirection(Directions.LEFT);
                break;

            case KeyboardEvent.KEY_RIGHT:
                snake.setDirection(Directions.RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }
}
