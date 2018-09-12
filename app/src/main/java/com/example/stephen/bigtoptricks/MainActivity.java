package com.example.stephen.bigtoptricks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MyTrainingDbAdapter.ItemClickListener {

    // Create a string of json to pass around
    private MyTrainingDbAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Cursor mCursor;
    private final int LOADER_ID = 42;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create layout manager and bind it to the recycler view
        mRecyclerView = findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new MyTrainingDbAdapter(this);
        // Start listening for clicks
        mAdapter.setClickListener(this);
        // Set adapter to mRecyclerView
        mRecyclerView.setAdapter(mAdapter);
        // Initialize loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ START ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public void onItemClick(int position) {

        mCursor.moveToPosition(position);

        String trickName = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
        String timeTrained = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
        String trickGoal = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_GOAL));
        String trickPr = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_PERSONAL_RECORD));
        String trickDescription = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_DESCRIPTION));
        String id = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry._ID));
        String propType = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_PROP_TYPE));
        String records = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_RECORD));
        String hits = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_HIT));
        String misses = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_MISS));

        Log.d("LOG", "---------> asdf ID: " + id + " Trick Name: " + trickName);
        Intent toTrickDetail = new Intent(MainActivity.this, TrickDetail.class);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_ID, id);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_NAME, trickName);
        toTrickDetail.putExtra(TrickFragment.ARG_TIME_TRAINED, timeTrained);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_DESCRIPTION, trickDescription);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_PR, trickPr);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_PROP_TYPE, propType);
        toTrickDetail.putExtra(TrickFragment.ARG_TRICK_GOAL, trickGoal);
        toTrickDetail.putExtra(TrickFragment.ARG_RECORD_ID, records);
        toTrickDetail.putExtra(TrickFragment.ARG_HITS, hits);
        toTrickDetail.putExtra(TrickFragment.ARG_MISSES, misses);
        startActivity(toTrickDetail);
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        /*return new CursorLoader(this,Contract.listEntry.CONTENT_URI,null,
                null,null, Contract.listEntry.COLUMN_TIMESTAMP);*/
        return new CursorLoader(this,
                Contract.listEntry.CONTENT_URI,
                null,
                Contract.listEntry.COLUMN_IS_META + "=?",
                new String[]{"yes"},
                Contract.listEntry.COLUMN_TIMESTAMP);
    }

    // When loading is finished, swap in the new data
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter.swapCursor(data);
    }

    // Reset the loader
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    /////////////////////////////////// END CURSOR LOADER METHODS //////////////////////////////////

    //++++++++++++++++++++++++++++++++ START THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // This allows the user to switch between favorites, popular, or highest rated
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.menu_add_trick) {
            Intent toAddNewTrickActivity = new Intent(this, AddTrick.class);
            startActivity(toAddNewTrickActivity);
        }
        if (itemThatWasClickedId == R.id.menu_add_from_list) {
            Intent toAddFromListActivity = new Intent(this, AddTrickFromList.class);
            startActivity(toAddFromListActivity);
        }
        if (itemThatWasClickedId == R.id.menu_show_db) {
            Intent toShowDb = new Intent(this, DisplayData.class);
            startActivity(toShowDb);
        }
        if (itemThatWasClickedId == R.id.menu_help)
            Toast.makeText(this, "Menu help is not implemented yet.", Toast.LENGTH_SHORT).show();
        if (itemThatWasClickedId == R.id.menu_settings)
            Toast.makeText(this, "Menu settings is not implemented yet.", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    //++++++++++++++++++++++++++++++++ END THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++++
}