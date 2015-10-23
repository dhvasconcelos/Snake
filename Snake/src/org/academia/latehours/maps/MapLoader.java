package org.academia.latehours.maps;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by cadet on 13/10/15.
 */
public class MapLoader {

    InputStreamReader mapReader;

    public MapLoader(String filePath) {

        try {
            URL url = getClass().getResource(filePath.startsWith("/") ? filePath : "/" + filePath);

            if (url != null) {
                mapReader = new InputStreamReader(url.openStream());

            } else {
                mapReader = new FileReader(filePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[][] fileRead() {
       // if(System.getProperty("os.name").matches("Mac.*")) {
            int[][] mapInt = new int[Map.getCols()][Map.getRows()]; //use in mac
            for (int i = 0; i < Map.getRows(); i++) {
                for (int j = 0; j < Map.getCols(); j++) {
                    try {
                        int character = mapReader.read();
                        if (character != 10) {
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

        /*} else {
            int[][] mapInt = new int[Map.getCols() + 1][Map.getRows() + 1];
            for (int i = 0; i <= Map.getRows(); i++) {
                for (int j = 0; j <= Map.getCols(); j++) {
                    try {
                        int character = mapReader.read();
                        if (character != 10) {
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
        }*/
    }

}
