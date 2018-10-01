package com.example.stephen.bigtoptricks.addTricks;


import android.util.Log;

import com.example.stephen.bigtoptricks.Tricks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    // Takes the big json string that contains all the tricks, and returns the length.
    public static int getNumberOfTricks(String public_json_string) throws JSONException {
        JSONArray jsonArray = new JSONArray(public_json_string);
        return jsonArray.length();
    }

    // Takes the big json string that contains all the tricks, and returns trick object
    // Note: data is validated by parseLimitedObjects()
    public static Tricks parseIndividualTrickToOBject(String public_json_string, int position) {
        JSONArray jsonArray;
        String name = "";
        String capacity = "";
        String siteswap = "";
        String animation = "";
        String tutorial = "";
        String difficulty = "";
        String description = "";
        String source = "";
        try {
            jsonArray = new JSONArray(public_json_string);
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            name = jsonObject.getString("trick_name");
            animation = jsonObject.getString("animation");
            siteswap = jsonObject.getString("siteswap");
            difficulty = jsonObject.getString("difficulty");
            capacity = jsonObject.getString("capacity");
            description = jsonObject.getString("description");
            tutorial = jsonObject.getString("tutorial");
            source = jsonObject.getString("source");
        } catch (JSONException e) {
            Log.d("LOG", "fromMe: Error in json parsing");
        }

        // Create a new trick object, and set the parameters
        Tricks trick = new Tricks();
        trick.setDescription(description);
        trick.setName(name);
        trick.setAnimation(animation);
        trick.setSource(source);
        trick.setDifficulty(difficulty);
        trick.setCapacity(capacity);
        trick.setTutorial(tutorial);
        trick.setSiteswap(siteswap);
        return trick;
    }

    // Takes the big json string that contains all the tricks, a list of objects
    public static ArrayList<Tricks> parseLimitedObjects(String public_json_string) throws JSONException {
        ArrayList<Tricks> tricks_list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for (int position = 0; position < num_tricks; position++) {
            // Try to parse the trick - validate the data
            try {
                Tricks tricks = new Tricks();
                JSONObject jsonObject = jsonArray.getJSONObject(position);
                String name = jsonObject.getString("trick_name");
                String difficulty = jsonObject.getString("difficulty");
                String capacity = jsonObject.getString("capacity");
                String source = jsonObject.getString("source");
                tricks.setName(name);
                tricks.setCapacity(capacity);
                tricks.setDifficulty(difficulty);
                tricks.setSource(source);
                tricks_list.add(tricks);
            } catch (Exception e) {
                Log.d("LOG", "fromMe: Trick failed to parse correctly.");
            }
        }
        return tricks_list;
    }
}
