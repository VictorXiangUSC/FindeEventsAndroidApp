package com.example.hw9;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Util {
    public Util(){}
    public static String getProperty(Object input, String level, String... rest) throws JSONException {
        if (input == null)
            return "";

        JSONObject jsonInput;
        if (input instanceof JSONArray) {
            JSONArray jsonInputs = (JSONArray) input;
            if (jsonInputs.length() == 0)
                return "";
            jsonInput = jsonInputs.getJSONObject(0);
        } else if (input instanceof JSONObject) {
            jsonInput = (JSONObject) input;
        } else {
            return "";
        }

        if (rest.length == 0 && jsonInput.has(level)) {
            return jsonInput.get(level).toString();
        }

        if (rest.length > 0){
            String curKey = rest[0];
            String[] newRest = {};
            if (rest.length > 1) {
                newRest = Arrays.copyOfRange(rest, 1, rest.length);
            }
            if (jsonInput.has(curKey)) {
                return getProperty(jsonInput.get(curKey), level, newRest);
            }
         }

        return "";
    }
}
