package com.example.stephen.bigtoptricks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DisplayData extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID = 42;
    private Cursor mCursor;
    private Button mOutputButton;
    private List<Tricks> mTricks;
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        /*return new CursorLoader(this,
                Contract.listEntry.CONTENT_URI,
                null,
                Contract.listEntry.COLUMN_IS_META + "=?",
                new String[]{"no"},
                Contract.listEntry.COLUMN_TIMESTAMP);}*/
        // for now load all the elements in the database for testing purposes
        return new CursorLoader(this, Contract.listEntry.CONTENT_URI, null,
                null, null, Contract.listEntry.COLUMN_TIMESTAMP);
    }

    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        bindView(data);
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////

    private void bindView(Cursor data) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();;

        String completeDb = "Name: ___ Catch Count: ___ Time Trained: ___\n";
        int time_trained = 0;
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String goal = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_GOAL));
            String pr = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_PERSONAL_RECORD));
            String timeTrained = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
            String catchCount = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_RECORD));
            String location = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_LOCATION));

            if(!names.contains(name)) names.add(name);
            if(!locations.contains(location)) locations.add(location);


            time_trained = time_trained + Integer.parseInt(timeTrained);
            completeDb += "\u2022 " + name + " - " + catchCount + " - " + timeTrained + "\n";

            Log.d("LOG", "asdf trick name: " + name + " " + i + " " + catchCount + completeDb.length());

        }
        String trick_names = "";
        String trick_locations = "";
        for (int i = 0; i < names.size(); i++) trick_names += "\u2022 " + names.get(i) + "\n";
        for (int i = 0; i < locations.size(); i++) trick_locations += "\u2022 " + locations.get(i) + "\n";

        Log.d("LOG", "asdf trick names: " + names.toString());
        Log.d("LOG", "asdf trick locations: " + locations.toString());
        //create output string to display
        String output = "";
        output = output + "My total training time: " + Integer.toString(time_trained) + "\n\n\n";
        output = output + "Places I have juggled:\n\n" + trick_locations + "\n\n\n";
        output = output + "Tricks I have trained:\n\n" + trick_names + "\n\n\n";
        output = output + "Complete Database:\n\n" + completeDb;
        TextView database_textview = (TextView) findViewById(R.id.database_textview);
        database_textview.setText(output);
    }
}
