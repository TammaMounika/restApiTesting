package org.automation.support;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Util {

    public static JSONObject readJsonFile(String path) {
        if(path == null) 
            return null;
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = null;
        try {
            //Read JSON file
            File file = new File("src/test/resources/" + path);
            FileReader reader = new FileReader(file.getAbsolutePath());
            obj = (JSONObject) jsonParser.parse(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONArray convertStringToJsonArray(String input) {
        JSONParser jsonParser = new JSONParser();
        JSONArray obj = null;
        try {
            obj = (JSONArray) jsonParser.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    
    public static JSONObject filterJSONArray(JSONArray jsonArray,String key, String value) {
        if(jsonArray == null || key == null || value == null)
            return null;
        for (int n = 0; n < jsonArray.size(); n++) {
            JSONObject obj = (JSONObject) jsonArray.get(n);
            if (obj.get(key).equals(value)) {
                return obj;
            }
        }
        return null;
    }
}
