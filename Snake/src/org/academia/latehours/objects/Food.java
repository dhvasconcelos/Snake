package org.academia.latehours.objects;

import org.academia.latehours.maps.Map;
import org.academia.latehours.position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

/**
 * Created by cadet on 06/10/15.
 */
public class Food {

    private boolean isOnField;
    private int foodScore = 10;
    Position position;
    Rectangle gameFood;

    public int getFoodScore() {
        return foodScore;
    }

    public void createFood() {
        position = new Position();
        gameFood = new Rectangle(position.getCol() * Map.getCellSize(),
                position.getRow() * Map.getCellSize(),
                Map.getCellSize(),
                Map.getCellSize());

        gameFood.setColor(Color.WHITE);
        gameFood.fill();
        setIsOnField(true);
    }

    public void removeFood(){
        gameFood.delete();
        setIsOnField(false);

    }

    public boolean isOnField() {
        return isOnField;
    }

    public void setIsOnField(boolean isOnField) {
        this.isOnField = isOnField;
    }

    public Position getPosition() {
        return position;
    }
}
