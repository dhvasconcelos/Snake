package org.academia.latehours.objects;

import org.academia.latehours.maps.Map;
import org.academia.latehours.position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

/**
 * Created by cadet on 12/10/15.
 */
public class Wall {

    private Rectangle representation;
    private Position position;


    public Wall(Position position) {
        this.position = position;
        this.representation = new Rectangle(position.getCol() * Map.getCellSize(),
                position.getRow() * Map.getCellSize(),
                Map.getCellSize(),
                Map.getCellSize());
        representation.setColor(Color.WHITE);
        representation.fill();
    }

    public Position getPosition() {
        return position;
    }

    public void deleteRepresentation() {
        representation.delete();
    }

}
