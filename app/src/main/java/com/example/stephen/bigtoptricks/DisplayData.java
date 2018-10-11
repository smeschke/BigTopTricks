package com.example.stephen.bigtoptricks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayData extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // Support up navigation
        (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize the loader to get data and then display it
        int LOADER_ID = 42;
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Load all the elements in the database
        return new CursorLoader(this, Contract.listEntry.CONTENT_URI, null,
                null, null, Contract.listEntry.COLUMN_TIMESTAMP);
    }

    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        bindView(data);
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////

    private void bindView(Cursor data) {
        //Create a list for names and a list for locations
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();

        // Read all the data from the db
        StringBuilder completeDb = new StringBuilder(getString(R.string.complete_db_header) + "\n");
        int time_trained = 0;
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String name = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
            String timeTrained = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
            String catchCount = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_RECORD));
            String location = data.getString(data.getColumnIndex(Contract.listEntry.COLUMN_LOCATION));

            if (!names.contains(name)) names.add(name);
            if (!locations.contains(location)) locations.add(location);


            time_trained = time_trained + Integer.parseInt(timeTrained);
            completeDb.append("\u2022 ").append(name).append(" - ").append(catchCount).append(" - ").append(timeTrained).append("\n");
        }
        StringBuilder trick_names = new StringBuilder();
        StringBuilder trick_locations = new StringBuilder();
        for (int i = 0; i < names.size(); i++) trick_names.append("\u2022 ").append(names.get(i)).append("\n");
        for (int i = 0; i < locations.size(); i++)
            trick_locations.append("\u2022 ").append(locations.get(i)).append("\n");

        // Create output string to display
        String output = "";
        output = output + getString(R.string.total_training_time) + " " + Integer.toString(time_trained) + "\n\n\n";
        output = output + getString(R.string.juggling_places) + "\n\n" + trick_locations + "\n\n\n";
        output = output + getString(R.string.tricks_i_have_trained) + "\n\n" + trick_names + "\n\n\n";
        output = output + getString(R.string.complete_database) + "\n\n" + completeDb;
        TextView database_textview = findViewById(R.id.database_textview);
        database_textview.setText(output);
    }
}
