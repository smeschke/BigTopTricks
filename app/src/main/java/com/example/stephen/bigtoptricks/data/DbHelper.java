package com.example.stephen.bigtoptricks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    // Some of the code (like the onUpgrade method) is adapted from lesson T07.06 (guest list)
    private static final String DATABASE_NAME = "mydb.db";
    private static final int DATABASE_VERSION = 27;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String create_table =
                "CREATE TABLE " + Contract.listEntry.TABLE_NAME + " (" +
                        Contract.listEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Contract.listEntry.COLUMN_PERSONAL_RECORD + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_TIME_TRAINED + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_TRICK_DESCRIPTION + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_TRICK_NAME + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_GOAL + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_IS_META + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_HIT + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_MISS + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_PROP_TYPE + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_RECORD + " TEXT NOT NULL, " +
                        Contract.listEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        "); ";

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.listEntry.TABLE_NAME);
        onCreate(db);
    }
}
