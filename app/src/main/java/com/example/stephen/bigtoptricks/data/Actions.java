package com.example.stephen.bigtoptricks.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class Actions {
    public static void insert_trick(Context context, String pr, String time_trained, String description,
                                    String name, String meta, String hit, String miss, String record,
                                    String prop_type, String goal, String siteswap, String animation,
                                    String source, String difficulty, String capacity, String tutorial,
                                    String location){
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, pr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, time_trained);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, description);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, name);
        cv.put(Contract.listEntry.COLUMN_IS_META, meta);
        cv.put(Contract.listEntry.COLUMN_HIT, hit);
        cv.put(Contract.listEntry.COLUMN_MISS, miss);
        cv.put(Contract.listEntry.COLUMN_RECORD, record);
        cv.put(Contract.listEntry.COLUMN_PROP_TYPE, prop_type);
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        cv.put(Contract.listEntry.COLUMN_SITESWAP, siteswap);
        cv.put(Contract.listEntry.COLUMN_ANIMAION, animation);
        cv.put(Contract.listEntry.COLUMN_SOURCE, source);
        cv.put(Contract.listEntry.COLUMN_DIFFICULTY, difficulty);
        cv.put(Contract.listEntry.COLUMN_CAPACITY, capacity);
        cv.put(Contract.listEntry.COLUMN_TUTORIAL, tutorial);
        cv.put(Contract.listEntry.COLUMN_LOCATION, location);
        // Insert the content values via a ContentResolver
        Uri uri = context.getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }

    public static void update_trick(Context context, String id, String pr, String time_trained,
                                    String description, String name, String meta, String hit,
                                    String miss, String record, String prop_type, String goal,
                                    String siteswap, String animation, String source,
                                    String difficulty, String capacity, String tutorial,
                                    String location){
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, pr);
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, time_trained);
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, description);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, name);
        cv.put(Contract.listEntry.COLUMN_IS_META, meta);
        cv.put(Contract.listEntry.COLUMN_RECORD, record);
        cv.put(Contract.listEntry.COLUMN_HIT, hit);
        cv.put(Contract.listEntry.COLUMN_MISS, miss);
        cv.put(Contract.listEntry.COLUMN_PROP_TYPE, prop_type);
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        cv.put(Contract.listEntry.COLUMN_SITESWAP, siteswap);
        cv.put(Contract.listEntry.COLUMN_ANIMAION, animation);
        cv.put(Contract.listEntry.COLUMN_SOURCE, source);
        cv.put(Contract.listEntry.COLUMN_DIFFICULTY, difficulty);
        cv.put(Contract.listEntry.COLUMN_CAPACITY, capacity);
        cv.put(Contract.listEntry.COLUMN_TUTORIAL, tutorial);
        cv.put(Contract.listEntry.COLUMN_LOCATION, location);
        context.getContentResolver().update(Contract.listEntry.CONTENT_URI, cv, id, null);
    }
}
