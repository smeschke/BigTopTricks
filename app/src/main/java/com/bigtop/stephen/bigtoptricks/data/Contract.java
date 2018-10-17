package com.bigtop.stephen.bigtoptricks.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    // Authority --> Which Content Provider to access?
    private static final String AUTHORITY = "com.bigtop.stephen.bigtoptricks";
    // Base content URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Paths for accessing data
    private static final String PATH_MOVIES = "tricks";

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
        public static final String COLUMN_RECORD = "record";
        public static final String COLUMN_IS_META = "is_meta";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_SITESWAP = "siteswap";
        public static final String COLUMN_CAPACITY = "capacity";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_TUTORIAL = "tutorial";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String Column_ANIMATION = "animation";
    }
}
