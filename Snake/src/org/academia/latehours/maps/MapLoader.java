package org.academia.latehours.maps;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by cadet on 13/10/15.
 */
public class MapLoader {

    FileReader mapReader;

    public MapLoader(String filePath) {
        try {
            mapReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int[][] fileRead() {
        int[][] mapInt = new int[Map.getCols()][Map.getRows()];
        for (int i = 0; i < Map.getRows(); i++) {
            for (int j = 0; j < Map.getCols(); j++) {
                try {
                    int character = mapReader.read();
                    if(character != 10) {
                        mapInt[j][i] = character;
                    } else {
                        j--;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapInt;
    }

}
