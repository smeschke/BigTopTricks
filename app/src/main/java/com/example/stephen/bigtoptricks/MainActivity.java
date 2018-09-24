package com.example.stephen.bigtoptricks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.stephen.bigtoptricks.addTricks.AddTrickFromList;
import com.example.stephen.bigtoptricks.data.Contract;

import static com.example.stephen.bigtoptricks.TrickDetail.ARG_TRICK_OBJECT;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MyTrainingDbAdapter.ItemClickListener {

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

        String name = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_NAME));
        String time_trained = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TIME_TRAINED));
        String goal = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_GOAL));
        String pr = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_PERSONAL_RECORD));
        String description = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TRICK_DESCRIPTION));
        String id = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry._ID));
        String prop_type = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_PROP_TYPE));
        String record = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_RECORD));
        String hit = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_HIT));
        String miss = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_MISS));
        String animation = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_ANIMAION));
        String siteswap = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_SITESWAP));
        String source = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_SOURCE));
        String difficulty = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_DIFFICULTY));
        String capacity = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_CAPACITY));
        String tutorial = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_TUTORIAL));
        String meta = mCursor.getString(mCursor.getColumnIndex(Contract.listEntry.COLUMN_IS_META));

        Tricks tricks = new Tricks(pr, time_trained, description, name, meta, hit, miss, record, prop_type, goal,
                siteswap, animation, source, difficulty, capacity, tutorial, id);
        Intent toTrickDetail = new Intent(MainActivity.this, TrickDetail.class);
        toTrickDetail.putExtra(ARG_TRICK_OBJECT, tricks);
        startActivity(toTrickDetail);
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    ///////////////////////////////////START CURSOR LOADER METHODS /////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
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
        if (data.getCount()==0) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.coordinator),
                    "Welcome! Press 'Add' to get started...", Snackbar.LENGTH_LONG);
            mySnackbar.show();
        }

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
        if (itemThatWasClickedId == R.id.menu_show_db) {
            Intent toShowDb = new Intent(this, DisplayData.class);
            startActivity(toShowDb);
        }
        return super.onOptionsItemSelected(item);
    }
    //++++++++++++++++++++++++++++++++ END THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++++

    public void addTrick(View view) {
        Intent toAddFromListActivity = new Intent(this, AddTrickFromList.class);
        startActivity(toAddFromListActivity);
    }
}