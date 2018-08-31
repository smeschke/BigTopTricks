package com.example.stephen.bigtoptricks.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    // Authority --> Which Content Provider to access?
    public static final String AUTHORITY = "com.example.stephen.bigtoptricks";
    // Base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Paths for accessing data
    public static final String PATH_MOVIES = "tricks";

    public static final class listEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "mylist";
        public static final String COLUMN_TRICK_NAME = "trick_name";
        public static final String COLUMN_TRICK_DESCRIPTION = "trick_description";
        public static final String COLUMN_TIME_TRAINED = "time_trained";
        public static final String COLUMN_PERSONAL_RECORD = "personal_record";
        public static final String COLUMN_GOAL = "goal";
        public static final String COLUMN_HIT = "hit";
        public static final String COLUMN_MISS = "miss";
        public static final String COLUMN_PROP_TYPE = "propType";
        // if entry is not meta, column record contains the number of catches
        // if entry is meta, column contains a list of all record a,b,c,d...
        public static final String COLUMN_RECORD = "record";
        public static final String COLUMN_IS_META = "is_meta";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
