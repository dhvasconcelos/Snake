package org.academia.latehours.maps;

import org.academia.latehours.Game;
import org.academia.latehours.objects.Wall;
import org.academia.latehours.position.Position;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;

import java.util.ArrayList;

/**
 * Created by cadet on 06/10/15.
 */
public class Map {

    private static int rows = 31;
    private static int cols = 31;
    private static int cellSize = 20;
    private Rectangle gameBoard;
    private static ArrayList<Wall> walls;
    private MapLoader mapLoader;

    public Map() {
        this.gameBoard = new Rectangle(0, 0, cols * cellSize, rows * cellSize);
        this.gameBoard.setColor(Color.BLACK);
        this.gameBoard.fill();
        mapLoader = new MapLoader("Snake/resources/map" + Game.getLevel() + ".txt");
        wallCreator();
    }

    public Map(int cols, int rows) {
        this.rows = rows;
        this.cols = cols;
        this.gameBoard = new Rectangle(0, 0, cols * cellSize, rows * cellSize);
        this.gameBoard.setColor(Color.BLACK);
        this.gameBoard.fill();
    }

    public void wallCreator() {
        this.walls = new ArrayList<>();
        int[][] wallLocations = mapLoader.fileRead();
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (wallLocations[j][i] == '#') {
                    Position position = new Position(j, i);
                    walls.add(new Wall(position));
                }
            }
        }
    }

    public void wallDelete(Position position) {
        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).getPosition().equals(position)) {
                walls.get(i).deleteRepresentation();
                walls.remove(i);
            }
        }
    }

    public void mapDelete() {
        gameBoard.delete();
        for (int i = 0; i < walls.size(); i++) {
            walls.remove(i);
        }
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

    public static ArrayList<Wall> getWalls() {
        return walls;
    }

    public static void deleteWalls() {
        walls = null;
    }
}
