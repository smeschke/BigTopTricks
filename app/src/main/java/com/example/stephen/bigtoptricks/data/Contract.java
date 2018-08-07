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
        /*
        * The database contains two very similar types of objects.
        *
        * The first type is the trick description type. This contains the name
        * and some details about the trick that the user entered:
        *     trick name
        *     trick description
        *     goal catches
        *     is record: no
        *
        * The second type is the trick record. This type comes from when a user
        * is training, and has some training data to record, so this contains:
        *     time trained
        *     catches achieved
        *     is record: yes
        * */
        public static final String COLUMN_IS_RECORD = "is_record";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
