package org.academia.latehours.position;

import org.academia.latehours.maps.Map;

/**
 * Created by cadet on 06/10/15.
 */
public class Position {


    private int row;
    private int col;

    public Position() {
        getRandomPosition();
    }

    public Position(int col, int row) {
        this.row = row;
        this.col = col;
    }


    public void getRandomPosition() {
        this.row = (int) (Math.random() * Map.getRows());
        this.col = (int) (Math.random() * Map.getCols());
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
