package com.bigtop.stephen.bigtoptricks.data;

import android.content.ContentValues;
import android.content.Context;

public class Actions {

    // Insert a trick into the database
    public static void insert_trick(Context context, String pr, String time_trained, String description,
                                    String name, String meta, String hit, String miss, String record,
                                    String prop_type, String goal, String siteswap, String animation,
                                    String source, String difficulty, String capacity, String tutorial){
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
        cv.put(Contract.listEntry.Column_ANIMATION, animation);
        cv.put(Contract.listEntry.COLUMN_SOURCE, source);
        cv.put(Contract.listEntry.COLUMN_DIFFICULTY, difficulty);
        cv.put(Contract.listEntry.COLUMN_CAPACITY, capacity);
        cv.put(Contract.listEntry.COLUMN_TUTORIAL, tutorial);
        // Insert the content values via a ContentResolver
        context.getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }

    // Insert a trick into the database
    public static void update_trick(Context context, String id, String pr, String time_trained,
                                    String description, String name, String meta, String hit,
                                    String miss, String record, String prop_type, String goal,
                                    String siteswap, String animation, String source,
                                    String difficulty, String capacity, String tutorial){
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
        cv.put(Contract.listEntry.Column_ANIMATION, animation);
        cv.put(Contract.listEntry.COLUMN_SOURCE, source);
        cv.put(Contract.listEntry.COLUMN_DIFFICULTY, difficulty);
        cv.put(Contract.listEntry.COLUMN_CAPACITY, capacity);
        cv.put(Contract.listEntry.COLUMN_TUTORIAL, tutorial);
        context.getContentResolver().update(Contract.listEntry.CONTENT_URI, cv, id, null);
    }
}
