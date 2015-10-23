package org.academia.latehours.objects;

import org.academia.latehours.maps.Map;
import org.academia.latehours.position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

/**
 * Created by cadet on 08/10/15.
 */
public abstract class Food {

    private boolean isOnField;
    private int foodScore;
    private Position position;
    private Rectangle gameFood;
    private Color color = Color.WHITE;

    public int getFoodScore() {
        return foodScore;
    }

    public void setFoodScore(int foodScore) {
        this.foodScore = foodScore;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void placeFood(Position position) {
        this.position = position;
        gameFood = new Rectangle(position.getCol() * Map.getCellSize(),
                position.getRow() * Map.getCellSize(),
                Map.getCellSize(),
                Map.getCellSize());

        gameFood.setColor(color);
        gameFood.fill();
        setIsOnField(true);
    }

    public void removeFood() {
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

