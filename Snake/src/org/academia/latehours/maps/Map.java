package org.academia.latehours.maps;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

/**
 * Created by cadet on 06/10/15.
 */
public class Map {

    private static int rows = 30;
    private static int cols = 30;
    private static int cellSize = 20;
    private Rectangle gameBoard;

    public Map() {
        this.gameBoard = new Rectangle(0, 0, cols * cellSize, rows * cellSize);
        this.gameBoard.setColor(Color.BLACK);
        this.gameBoard.fill();
    }

    public Map(int cols, int rows) {
        this.rows = rows;
        this.cols = cols;
        this.gameBoard = new Rectangle(0, 0, cols * cellSize, rows * cellSize);
        this.gameBoard.setColor(Color.BLACK);
        this.gameBoard.fill();
    }

    public static int getCols() {
        return cols;
    }

    public static int getRows() {
        return rows;
    }

    public static int getCellSize() {
        return cellSize;
    }

}
