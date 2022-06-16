package com.mygdx;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonFileManager {

    public static void writeMapToJson(int[][] map, String fileName) {
        try {
            // create a writer
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("core/src/com/mygdx/json/" + fileName + ".json"));

            // create customer object
            JsonObject file = new JsonObject();
            // add customer payment methods
            JsonArray jsonMap = new JsonArray();

            jsonMap.addAll(Arrays.asList(map));
            file.put("map", jsonMap);

            // write JSON to file
            Jsoner.serialize(file, writer);

            // close the writer
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static int[][] readMapFromJson(String fileName) {
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("core/src/com/mygdx/json/" + fileName + ".json"));

            // create parser
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);

            // read payment method
            JsonArray outerArr = (JsonArray) parser.get("map");
            int[][] map = new int[outerArr.size()][((JsonArray) outerArr.get(0)).size()];
            for (int i = 0; i < outerArr.size(); i++) {
                JsonArray innerArr = (JsonArray) outerArr.get(i);

                for (int j = 0; j < innerArr.size(); j++) {

                    int integer = ((BigDecimal) innerArr.get(j)).intValue();
                    map[i][j] = integer;
                }
            }

            // close reader
            reader.close();
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String[] getAllMapNames() {
        File folder = new File("core/src/com/mygdx/json");
        File[] listOfFiles = folder.listFiles();
        List<String> mapNames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                String mapName = (fileName.split(".json"))[0];
                mapNames.add(mapName);
            } 
        }
        String[] mapNamesArr = (String[]) mapNames.toArray(new String[mapNames.size()]);
        return mapNamesArr;
    }

    public static void main(String[] args) {
        // int[][] arr1 = new int[][] { { 0, 0, 1 }, { 0, 0, 1 } };
        // // writeMapToJson(arr1, "Test");
        // int[][] read = readMapFromJson("Test");
        // System.out.println(Arrays.deepToString(read));

        String[] mapNames = getAllMapNames();
        // String[] array = (String[]) mapNames.toArray(new String[mapNames.size()]);
        System.out.println(Arrays.toString(mapNames));
    }

}
