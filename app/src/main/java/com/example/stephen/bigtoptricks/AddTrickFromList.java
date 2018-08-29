package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

import java.util.ArrayList;

public class AddTrickFromList extends AppCompatActivity implements mAdapter.mAdapterOnClickHandler{

    //initialize variables for resources from strings.xml
    private ArrayList<String> pattern_list = new ArrayList<>();
    private String[] patterns;
    //initialize an adapter and recyclerView
    private mAdapter mAdapter;
    private RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick_from_list);

        //get resources from strings.xml
        Resources res = getResources();
        patterns = res.getStringArray(R.array.patterns);
        Log.d("LOG", "asdf patterns length: " + patterns.length);
        for (int i = 0; i < patterns.length; i++){
            Log.d("LOG", "asdf " + patterns[i]);
            pattern_list.add(patterns[i]);
        }
        Log.d("LOG", "asdf patterns length: " + pattern_list.size());

        //code for recycler view
        mList = (RecyclerView) findViewById(R.id.recycler_view_siteswap_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(layoutManager);
        mList.setHasFixedSize(true);
        mAdapter = new mAdapter(this, this, pattern_list);
        mList.setAdapter(mAdapter);
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ START ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @Override
    public void onClick(final int position) {
        Log.d("LOG", "asdf ID: " + position + " " + patterns[position]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Goal for Trick:");
        // Set up the input
        final EditText goal = new EditText(this);
        goal.setHint("Goal Catches...");
        builder.setView(goal);
        // Show the keyboard
        goal.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String goalString = goal.getText().toString();
                add_to_db(pattern_list.get(position), "no description for this trick",
                        goalString);
                Toast.makeText(getApplicationContext(), pattern_list.get(position)+" has been added to the DB",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ END ONCLICK METHOD @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public void add_to_db(String trickName, String trickDescription, String goal){
        Log.d("LOG", "asdf Goal: " + goal);
        // Fill content values with trick attributes
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, "0");
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, trickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, trickName);
        cv.put(Contract.listEntry.COLUMN_IS_RECORD, "no");
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        cv.put(Contract.listEntry.COLUMN_RECORD, "0");
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);
    }
}
