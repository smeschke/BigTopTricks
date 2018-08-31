package com.example.stephen.bigtoptricks;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;

public class TrickDetail extends AppCompatActivity {
    public String mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String trickName = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_NAME);
            String trickPr = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_PR);
            String trickGoal = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_GOAL);
            String trickDescription = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_DESCRIPTION);
            String timeTrained = getIntent().getExtras().getString(TrickFragment.ARG_TIME_TRAINED);
            String records = getIntent().getExtras().getString(TrickFragment.ARG_RECORD_ID);
            String propType = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_PROP_TYPE);
            String hits = getIntent().getExtras().getString(TrickFragment.ARG_HITS);
            String misses = getIntent().getExtras().getString(TrickFragment.ARG_MISSES);
            mId = getIntent().getExtras().getString(TrickFragment.ARG_TRICK_ID);

            Bundle arguments = new Bundle();
            arguments.putString(TrickFragment.ARG_TIME_TRAINED, timeTrained);
            arguments.putString(TrickFragment.ARG_TRICK_DESCRIPTION, trickDescription);
            arguments.putString(TrickFragment.ARG_TRICK_GOAL, trickGoal);
            arguments.putString(TrickFragment.ARG_TRICK_PR, trickPr);
            arguments.putString(TrickFragment.ARG_TRICK_NAME, trickName);
            arguments.putString(TrickFragment.ARG_HITS, hits);
            arguments.putString(TrickFragment.ARG_MISSES, misses);
            arguments.putString(TrickFragment.ARG_TRICK_ID, mId);
            arguments.putString(TrickFragment.ARG_TRICK_PROP_TYPE, propType);
            arguments.putString(TrickFragment.ARG_RECORD_ID, records);
            TrickFragment fragment = new TrickFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    //++++++++++++++++++++++++++++++++ START THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    // This allows the user to switch between favorites, popular, or highest rated
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.menu_remove_trick){
            remove_trick(Integer.parseInt(mId));
            finish();
            Toast.makeText(this, "Trick Removed from DB", Toast.LENGTH_LONG).show();
        }
        if (itemThatWasClickedId == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this,
                    MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //++++++++++++++++++++++++++++++++ END THREE BUTTONS OPTIONS +++++++++++++++++++++++++++++++++++

    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; START DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    public void remove_trick(int id) {
        // Build uri with the movie json that needs to be deleted
        Uri uri = Contract.listEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
        // Delete single row
        int test = getContentResolver().delete(uri, null, null);
        Log.d("LOG", "asdf trick removed: " + test);
    }
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; END DB METHODS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
}
