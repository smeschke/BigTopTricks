package com.example.stephen.bigtoptricks;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stephen.bigtoptricks.data.Contract;

public class AddTrick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trick);

        Button addTrickButton = (Button) findViewById(R.id.button_add_trick_to_database);
        addTrickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_db();
            }
        });
    }

    public void add_to_db(){
        EditText trickNameEditText = (EditText) findViewById(R.id.edit_text_trick_name);
        EditText trickDescriptionEditText = (EditText) findViewById(R.id.edit_text_trick_description);
        EditText goalCatchesEditText = (EditText) findViewById(R.id.edit_text_goal_catches);
        String trickName = trickNameEditText.getText().toString();
        String trickDescription = trickDescriptionEditText.getText().toString();
        String goal = goalCatchesEditText.getText().toString();

        // Fill content values with trick attributes
        ContentValues cv = new ContentValues();
        cv.put(Contract.listEntry.COLUMN_PERSONAL_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_TIME_TRAINED, "0");
        cv.put(Contract.listEntry.COLUMN_TRICK_DESCRIPTION, trickDescription);
        cv.put(Contract.listEntry.COLUMN_TRICK_NAME, trickName);
        cv.put(Contract.listEntry.COLUMN_IS_RECORD, "no");
        cv.put(Contract.listEntry.COLUMN_RECORD, "0");
        cv.put(Contract.listEntry.COLUMN_GOAL, goal);
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(Contract.listEntry.CONTENT_URI, cv);

        // Inform the user that the trick has been added to the DB
        Toast.makeText(this, "add to db "+ uri.toString(), Toast.LENGTH_SHORT).show();

        // Reset the hints on the edittext boxes
        trickNameEditText.setHint("Enter Trick Name...");
        trickDescriptionEditText.setHint("Enter Trick Description (optional)...");
        goalCatchesEditText.setHint("Goal (#catches, #reps, time, etc...");
    }
}
