package com.example.stephen.bigtoptricks.addTricks;


import android.content.Context;
import android.util.Log;

import com.example.stephen.bigtoptricks.Tricks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class JsonUtils {

    /* Takes the big json string that contains all the tricks,
       and returns an array list containing all the trick names.*/
    public static ArrayList<String> getTrickNames(String public_json_string) throws JSONException {
        ArrayList<String> trick_names = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for(int position=0; position<num_tricks; position++){
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String trick_name = jsonObject.getString("trick_name");
            trick_names.add(trick_name);
        }
        return trick_names;
    }

    public static ArrayList<String> getTrickCapicities(String public_json_string) throws JSONException {
        ArrayList<String> trick_names = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for(int position=0; position<num_tricks; position++){
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String trick_name = jsonObject.getString("capacity");
            trick_names.add(trick_name);
        }
        return trick_names;
    }

    public static ArrayList<String> getTrickDifficulties(String public_json_string) throws JSONException {
        ArrayList<String> trick_names = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for(int position=0; position<num_tricks; position++){
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String trick_name = jsonObject.getString("difficulty");
            trick_names.add(trick_name);
        }
        return trick_names;
    }

    public static ArrayList<String> getTrickSources(String public_json_string) throws JSONException {
        ArrayList<String> trick_names = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for(int position=0; position<num_tricks; position++){
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String trick_name = jsonObject.getString("source");
            trick_names.add(trick_name);
        }
        return trick_names;
    }

    /* Takes the big json string that contains all the tricks,
   and returns the length.*/
    public static int getNumberOfTricks(String public_json_string) throws JSONException {
        JSONArray jsonArray = new JSONArray(public_json_string);
        int json_lenth = jsonArray.length();
        return json_lenth;
    }

    /* Takes the big json string that contains all twenty movies,
       and parses out one individual movie.*/
    public static ArrayList<String> parseIndividualTrick(String public_json_string, int position) {
        ArrayList<String> trick_details = new ArrayList<>();
        JSONArray jsonArray;
        String trick_name = "";
        String capacity = "";
        String siteswap = "";
        String animation = "";
        String tutorial = "";
        String difficulty = "";
        String video_url = "";
        String description = "";
        String source = "";
        try {
            jsonArray = new JSONArray(public_json_string);
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            trick_name = jsonObject.getString("trick_name");
            animation = jsonObject.getString("animation");
            siteswap = jsonObject.getString("siteswap");
            difficulty = jsonObject.getString("difficulty");
            capacity = jsonObject.getString("capacity");
            description = jsonObject.getString("description");
            tutorial = jsonObject.getString("tutorial");
            source = jsonObject.getString("source");
        } catch (JSONException e) {
            Log.d("LOG", "asdf error in json parsing");
        }
        trick_details.add(trick_name);
        trick_details.add(capacity);
        trick_details.add(siteswap);
        trick_details.add(animation);
        trick_details.add(tutorial);
        trick_details.add(difficulty);
        trick_details.add(tutorial);
        trick_details.add(description);
        trick_details.add(source);
        return trick_details;
    }

    /* Takes the big json string that contains all twenty movies,
   and parses out one individual movie.*/
    public static Tricks parseIndividualTrickToOBject(String public_json_string, int position) {
        ArrayList<String> trick_details = new ArrayList<>();
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
            Log.d("LOG", "asdf error in json parsing");
        }
        trick_details.add(name);
        trick_details.add(capacity);
        trick_details.add(siteswap);
        trick_details.add(animation);
        trick_details.add(tutorial);
        trick_details.add(difficulty);
        trick_details.add(tutorial);
        trick_details.add(description);
        trick_details.add(source);

        Tricks tricks = new Tricks("0", "0", description, name, "yes", "0",
                "0", "0", "0", "0", siteswap, animation, source,
                difficulty, capacity, tutorial, "0");
        return tricks;
    }

    public static Tricks parseLimitedDetails(String public_json_string, int position) {
        JSONArray jsonArray;
        String name = "";
        String capacity = "";
        String difficulty = "";
        String source = "";
        try {
            jsonArray = new JSONArray(public_json_string);
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            name = jsonObject.getString("trick_name");
            difficulty = jsonObject.getString("difficulty");
            capacity = jsonObject.getString("capacity");
            source = jsonObject.getString("source");
        } catch (JSONException e) {
            Log.d("LOG", "asdf error in json parsing");
        }
        Tricks tricks = new Tricks();
        tricks.setName(name);
        tricks.setCapacity(capacity);
        tricks.setDifficulty(difficulty);
        tricks.setSource(source);
        return tricks;
    }

    public static ArrayList<Tricks> parseLimitedObjects(String public_json_string) throws JSONException {
        ArrayList<Tricks> tricks_list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(public_json_string);
        int num_tricks = getNumberOfTricks(public_json_string);
        for(int position=0; position<num_tricks; position++){
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
        }
        return tricks_list;
    }


}
